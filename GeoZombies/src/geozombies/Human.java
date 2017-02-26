/**
 * 
 */
package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author nick
 *
 */
public class Human {
	
	private int energy, startingEnergy;

	// Search zone agent list that defines the search radius area for this Human
	protected List<ZoneAgent> searchZoneAgents;
	
	protected ZoneAgent activeSearchZone;
	
	public Human(int energy) {
		this.energy = startingEnergy = energy;
	}
	
	@ScheduledMethod(start = 0)
	public void init(){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		// Generate a list of search zone agents
		Coordinate center = geography.getGeometry(this).getCoordinate();
		List<Geometry> searchZones = ZombieUtils.generateSearchZones(geography, center, 4);
		searchZoneAgents = new ArrayList<ZoneAgent>();
		
		for (Geometry geom : searchZones){
			ZoneAgent zoneAgent = new ZoneAgent();
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
		List nearZombieList = ZombieUtils.getObjectsWithinDistance(this, 
				Zombie.class, searchDistance);
		
		ZoneAgent zoneWithLeastZombies = null;
		int minCount = Integer.MAX_VALUE;
		
		if (activeSearchZone != null){
			minCount = activeSearchZone.lookForObjects(nearZombieList).size();
			zoneWithLeastZombies = activeSearchZone;
		}
		
		boolean run = false;
		for (ZoneAgent zone : searchZoneAgents){	
			zone.setActive(false);
			
			List foundZombies = zone.lookForObjects(nearZombieList);
			
			if (foundZombies.size() > 0) 
				run = true;
			
			if(foundZombies.size() < minCount){
				zoneWithLeastZombies = zone;
				minCount = foundZombies.size();
				activeSearchZone = zoneWithLeastZombies;
			}
		}
		
		if (run)
			moveTowards(activeSearchZone);
		
	
		activeSearchZone.setActive(true);
		
	}
		

	public void moveTowards(ZoneAgent zone) {
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Point zoneCenter = geography.getGeometry(zone).getCentroid();
		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		double moveSlowDown = 20;
		double x = (zoneCenter.getX() - currentPosisition.x) / moveSlowDown;
		double y = (zoneCenter.getY() - currentPosisition.y) / moveSlowDown;
		
		geography.moveByDisplacement(this, x, y);
		
		updateSearchZone(currentPosisition);
		
		energy--;
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

}
