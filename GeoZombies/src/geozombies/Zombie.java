package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeometryUtils;
import repast.simphony.space.graph.Network;
import repast.simphony.util.ContextUtils;

/**
 * @author Eric Tatara
 * @author Nick Collier
 * @author Jonathan Ozik
 * 
 */
public class Zombie {

	// Search zone agent list that defines the search radius area for this Zombie
	protected List<ZoneAgent> searchZoneAgents;
	
	protected ZoneAgent activeSearchZone;
	
	private boolean moved;
	
	GeometryFactory fac = new GeometryFactory();
	
		
	@ScheduledMethod(start = 0)
	public void init(){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		// Generate a list of search zone agents
		Coordinate center = geography.getGeometry(this).getCoordinate();
		List<Geometry> searchZones = ZombieUtils.generateSearchZones(geography, center, 6);
		searchZoneAgents = new ArrayList<ZoneAgent>();
		
		for (Geometry geom : searchZones){
			ZoneAgent zoneAgent = new ZoneAgent();
			zoneAgent.setVisible(true);
			searchZoneAgents.add(zoneAgent);
			context.add(zoneAgent);
			geography.move(zoneAgent, geom);
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		double searchDistance = 5000; // meters
		List nearHumanList = ZombieUtils.getObjectsWithinDistance(this, 
				Human.class, searchDistance);
		
		ZoneAgent zoneWithMostHumans = null;
		int maxCount = -1;
		
		if (activeSearchZone != null){
			maxCount = activeSearchZone.lookForHumans(nearHumanList).size();
			zoneWithMostHumans = activeSearchZone;
		}
		
		for (ZoneAgent zone : searchZoneAgents){	
			zone.setActive(false);
			
			List<Human> foundHumans = zone.lookForHumans(nearHumanList);
			
			if(foundHumans.size() > maxCount){
				zoneWithMostHumans = zone;
				maxCount = foundHumans.size();
				activeSearchZone = zoneWithMostHumans;
			}
		}
		
		if (maxCount > 0){
			moveTowards(activeSearchZone);
		}
		else{
//			randomWalk();			
		}
		activeSearchZone.setActive(true);
		infect();
	}

	protected void randomWalk(){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");

		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		geography.moveByDisplacement(this, RandomHelper.nextDoubleFromTo(-0.0001, 0.0001), 
				RandomHelper.nextDoubleFromTo(-0.0001, 0.0001));
		
		updateSearchZone(currentPosisition);
	}
	
	protected void updateSearchZone(Coordinate lastPosition){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		double x = (currentPosisition.x - lastPosition.x);
		double y = (currentPosisition.y - lastPosition.y);
		
		for (ZoneAgent zone : searchZoneAgents){
			geography.moveByDisplacement(zone, x, y);	
		}
		
	}
	
	public void moveTowards(ZoneAgent zone) {
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Point zoneCenter = geography.getGeometry(zone).getCentroid();
		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		double moveSlowDown = 10;
		double x = (zoneCenter.getX() - currentPosisition.x) / moveSlowDown;
		double y = (zoneCenter.getY() - currentPosisition.y) / moveSlowDown;
		
		geography.moveByDisplacement(this, x, y);
		
		moved = true;
		
		updateSearchZone(currentPosisition);
	}

	public void infect() {
		
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		// The infection radius is smaller than the search radius
	  double infectRadius = 1500;  // meters	
	  
	  List humans = ZombieUtils.getObjectsWithinDistance(this, Human.class, 
	  		infectRadius);
			  
		if (humans.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, humans.size() - 1);
			Human human = (Human)humans.get(index);

			// Get the human geometry to re-use it as the spawned zombie location
			Geometry humanloc = geography.getGeometry(human);
			
			context.remove(human);
			Zombie childZombie = new Zombie();
			context.add(childZombie);
		
			geography.move(childZombie, humanloc);
			childZombie.init();
			
			Network net = (Network)context.getProjection("infection network");
			net.addEdge(new InfectionNetworkEdge(this, childZombie));
		}
	}
}
