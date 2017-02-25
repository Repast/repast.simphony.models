/**
 * 
 */
package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
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
	protected List<Geometry> generateSearchZones(Coordinate center){
		List<Geometry> geomList = new ArrayList<Geometry>();
		
		// Radius of the search circle area
		double radius = 0.1;  // in degree lat/lon
		
		// Number of search circle area divisions (pie slices)
		double numDivisions = 8;
		
		// The radian interval used to divide the search circle area
		double interval = 2 * Math.PI / numDivisions;
		
		GeometryFactory fac = new GeometryFactory();
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
		List<Geometry> searchZones = generateSearchZones(center);
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
	
		ZoneAgent zoneWithMostHumans = null;
		int maxCount = -1;
		
		if (activeSearchZone != null){
			maxCount = activeSearchZone.lookForHumans().size();
			zoneWithMostHumans = activeSearchZone;
		}
		
		for (ZoneAgent zone : searchZoneAgents){	
			zone.setActive(false);
			
			List<Human> foundHumans = zone.lookForHumans();
			
			if(foundHumans.size() > maxCount){
				zoneWithMostHumans = zone;
				maxCount = foundHumans.size();
			}
		}
		
		if (maxCount > 0 && zoneWithMostHumans != activeSearchZone){
			activeSearchZone = zoneWithMostHumans;
			zoneWithMostHumans.setActive(true);
			moveTowards(zoneWithMostHumans);
		}
		else{
//		randomWalk();			
		}
		
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
		
		double moveSlowDown = 5;
		double x = (zoneCenter.getX() - currentPosisition.x) / moveSlowDown;
		double y = (zoneCenter.getY() - currentPosisition.y) / moveSlowDown;
		
		geography.moveByDisplacement(this, x, y);
		
		moved = true;
		
		updateSearchZone(currentPosisition);
	}

	public void infect() {
		
		// TODO infection radius
		
//		GridPoint pt = grid.getLocation(this);
//		List<Object> humans = new ArrayList<Object>();
//		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
//			if (obj instanceof Human) {
//				humans.add(obj);
//			}
//		}
//
//		if (humans.size() > 0) {
//			int index = RandomHelper.nextIntFromTo(0, humans.size() - 1);
//			Object obj = humans.get(index);
//			NdPoint spacePt = space.getLocation(obj);
//			Context<Object> context = ContextUtils.getContext(obj);
//			context.remove(obj);
//			Zombie zombie = new Zombie(space, grid);
//			context.add(zombie);
//			space.moveTo(zombie, spacePt.getX(), spacePt.getY());
//			grid.moveTo(zombie, pt.getX(), pt.getY());
//			
//			Network<Object> net = (Network<Object>)context.getProjection("infection network");
//			net.addEdge(this, zombie);
//		}
	}
}
