package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeometryUtils;
import repast.simphony.util.ContextUtils;

public class ZombieUtils {

	public static GeometryFactory fac = new GeometryFactory();

	/**
	 * Generates a list of polygon geometries that defines the search area around
	 * a Zombie.  This implementation creates a set of "pie slices" within a 
	 * circle around the Zombie. 
	 * 
	 * @param center the current Zombie location coordinate
	 * @return a list of polygon geometries
	 */
	public static List<Geometry> generateSearchZones(Geography geography, 
			Coordinate center, int numZones){
		List<Geometry> geomList = new ArrayList<Geometry>();

		// Radius of the search circle area
		double radius = 0.1;  // in degree lat/lon

		// The radian interval used to divide the search circle area
		double interval = 2 * Math.PI / numZones;

		GeometricShapeFactory gsf = new GeometricShapeFactory();

		int numArcPoints = 10; // Num points in the arc (precision)

		gsf.setCentre(center);
		gsf.setSize(radius); 
		gsf.setNumPoints(numArcPoints); 

		// Generate a polygon geometry for each circle pie slice
		for (int i=0; i < numZones; i++){
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

	public static List<?> getObjectsWithinDistance(Object source, Class clazz,
			double searchDistance){
		Context context = ContextUtils.getContext(source);
		Geography geography = (Geography)context.getProjection("Geography");
		
		Geometry searchArea =  GeometryUtils.generateBuffer(geography, 
				geography.getGeometry(source), searchDistance);
		
		Envelope searchEnvelope = searchArea.getEnvelopeInternal();
		Iterable<?> nearObjects = geography.getObjectsWithin(searchEnvelope, clazz);	
		List nearObjectList = new ArrayList();
		
		for (Object o : nearObjects){
			nearObjectList.add(o);
		}	
		return nearObjectList;
	}
	
}
