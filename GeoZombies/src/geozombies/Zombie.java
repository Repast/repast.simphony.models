/**
 * 
 */
package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
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
	
	public Zombie() {
	}
	
	/**
	 * Generates a list of polygon geometries that defines the search area around
	 * a Zombie.  This implementation creates a set of "pie slices" within a 
	 * circle around the Zombie. 
	 * 
	 * @param center the current Zombie location coordinate
	 * @return a list of polygon geometries
	 */
	protected List<Geometry> generateSearchZones(Geography geography, Coordinate center){
		List<Geometry> geomList = new ArrayList<Geometry>();
		
		// Radius of the search circle area
		double radius = 0.1;  // in degree lat/lon
		
		// Number of search circle area divisions (pie slices)
		double numDivisions = 6;
		
		// The radian interval used to divide the search circle area
		double interval = 2 * Math.PI / numDivisions;
		
		GeometricShapeFactory gsf = new GeometricShapeFactory();
		
		int numArcPoints = 10; // Num points in the arc (precision)
		
		gsf.setCentre(center);
		gsf.setSize(radius); 
		gsf.setNumPoints(numArcPoints); 

		// Generate a polygon geometry for each circle pie slice
		for (int i=0; i < numDivisions; i++){
			LineString arc = gsf.createArc(i*interval, interval);
			
			Coordinate[] coords = new Coordinate[numArcPoints + 2];
			
			coords[0] = center;
			coords [numArcPoints + 2 - 1] = center;
			
			for (int j=0; j<numArcPoints; j++){
				coords[j+1] = arc.getCoordinateN(j);
			}
	
			geomList.add(fac.createPolygon(coords));
		}
		return geomList;
	}

		
	@ScheduledMethod(start = 0)
	public void init(){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		// Generate a list of search zone agents
		Coordinate center = geography.getGeometry(this).getCoordinate();
		List<Geometry> searchZones = generateSearchZones(geography, center);
		searchZoneAgents = new ArrayList<ZoneAgent>();
		
		for (Geometry geom : searchZones){
			ZoneAgent zoneAgent = new ZoneAgent();
			searchZoneAgents.add(zoneAgent);
			context.add(zoneAgent);
			geography.move(zoneAgent, geom);
		}
	}

	public List<Human> getHumansWithinDistance(double searchDistance){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Geometry searchArea =  GeometryUtils.generateBuffer(geography, 
				geography.getGeometry(this), searchDistance);
		
		Envelope searchEnvelope = searchArea.getEnvelopeInternal();
		
		Iterable<Human> nearHumans = geography.getObjectsWithin(searchEnvelope, Human.class);
		
		List<Human> nearHumanList = new ArrayList<Human>();
		
		for (Human human : nearHumans){
			nearHumanList.add(human);
		}
		
		return nearHumanList;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		double searchDistance = 5000; // meters
		List<Human> nearHumanList = getHumansWithinDistance(searchDistance);
		
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
		
	  double infectRadius = 500;  // meters	
	  
	  List<Human> humans = getHumansWithinDistance(infectRadius);
			  
		if (humans.size() > 0) {
			int index = RandomHelper.nextIntFromTo(0, humans.size() - 1);
			Human human = humans.get(index);

			context.remove(human);
			Zombie childZombie = new Zombie();
			context.add(childZombie);
			
			Point geom = fac.createPoint(currentPosisition);
			
			geography.move(childZombie, geom);
			childZombie.init();
			childZombie.randomWalk();
			
			Network net = (Network)context.getProjection("infection network");
			net.addEdge(new MyNetworkEdge(this, childZombie));
		}
	}
}
