package geography;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.Category;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.shape.random.RandomPointsBuilder;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.GeometryUtils;
import repast.simphony.space.gis.RepastCoverageFactory;
import repast.simphony.space.gis.WritableGridCoverage2D;
import repast.simphony.space.graph.Network;

/**
 * ContextBuilder for the GIS demo.  In this model, mobile GisAgents move around
 * the Geography with a random motion and are represented by point locations.  
 * ZoneAgents are polygons that represent certain geographic areas.  WaterLine
 * agents represent water supply lines from Lake Michigan that supply the 
 * Chicago area.  When a ZoneAgent intersects a WaterLine, the ZoneAgent will 
 * have access to fresh drinking water.  GisAgents that are within a certain 
 * distance from the ZoneAgent boundary will also have access to water.  Agents
 * that are not in proximity to a Zone with a water supply will not have access
 * to water (they will be thirsty).  BufferZoneAgents are for visualization 
 * to illustrate the extend of the boundary around a ZoneAgent.  
 * 
 * GisAgents may be generated programmatically depending on the value for 
 * number of agents.  GisAgents, ZoneAgents, and WaterLine agents are also
 * loaded from ESRI shapefiles.
 * 
 * @author Eric Tatara
 *
 */
public class ContextCreator implements ContextBuilder {

	int numAgents;
	double zoneDistance;
		
	public Context build(Context context) {
		
		System.out.println("Geography ContextCreator.build()");
					
		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		numAgents = (Integer)parm.getValue("numAgents");
		zoneDistance = (Double)parm.getValue("zoneDistance");

		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
				.createGeography("Geography", context, geoParams);

		GeometryFactory fac = new GeometryFactory();

		NetworkBuilder<?> netBuilder = new NetworkBuilder<Object>("Network", context, true);
		Network net = netBuilder.buildNetwork();
		
//		Geometry polygon = null;
//		int numPoints = 100;
//		RandomPointsBuilder shapeBuilder = new RandomPointsBuilder();
//    shapeBuilder.setExtent(polygon);
//    shapeBuilder.setNumPoints(numPoints);
//           
//     MultiPoint mtPoint = (MultiPoint)shapeBuilder.getGeometry();
//    
//  
//    for (Coordinate coord : mtPoint.getCoordinates()){
//    
//    
//    }
    	
		
		// Generate some points
		for (int i = 0; i < numAgents; i++) {
			GisAgent agent = new GisAgent("Site " + i);
			context.add(agent);

			Coordinate coord = new Coordinate(-88 + 0.5* Math.random(), 41.5 + 0.5 * Math.random());
			Point geom = fac.createPoint(coord);
			geography.move(agent, geom);
			
			Object o = context.getRandomObject();
		
			if (o != null && o instanceof GisAgent) {
				net.addEdge(agent, o, 1.0);
			}
		}

		// TODO GIS: use an example of ShapefileLoader
		
		// TODO GIS Coverage layer
		// envelope around Midway airport
		ReferencedEnvelope env = new ReferencedEnvelope(-87.761278, -87.742395, 
				41.778022, 41.791462, DefaultGeographicCRS.WGS84);
		
		WritableGridCoverage2D coverage = RepastCoverageFactory.createWritableCoverageFloat(
				"Eric's data", 10, 10, env, null, -1, 0, 10, -1);
		
		geography.addCoverage("My coverage", coverage);
		
		// envelope south west Chicago
		env = new ReferencedEnvelope(-87.9220, -87.7236, 41.50, 41.7313, DefaultGeographicCRS.WGS84);
		
		// Simple 3-category coverage with no-data
		 Category[] categories	= new Category[] {	
	        new Category("No data", Color.BLACK, 0),
	        new Category("Level 1", Color.GREEN, 1),
	        new Category("Level 2", Color.BLUE, 2),
	        new Category("Level 3", Color.RED, 3)
	    };
		WritableGridCoverage2D coverage2 = RepastCoverageFactory.createWritableByteIndexedCoverage(
				"Eric's data indexed", 100, 200, env, categories, null, 0);
		
		geography.addCoverage("My indexed coverage", coverage2);
		
		// envelope south east Chicago
		env = new ReferencedEnvelope(-87.7236, -87.5252, 41.50, 41.7313, DefaultGeographicCRS.WGS84);

		int maxColorIndex = 10; //RepastCoverageFactory.MAX_BYTE_COLOR_INDEX;
		Color[] whiteRedColorScale = new Color[maxColorIndex];
		
		// white to red color scale
		for (int i=0; i<whiteRedColorScale.length; i++) {
			int blueGreen = (255/maxColorIndex*(maxColorIndex-i));			
			whiteRedColorScale[i] = new Color(255, blueGreen, blueGreen); 
		}
			
		// Color scale coverage with no-data
		 categories	= new Category[] {	
	        new Category("No data", Color.WHITE, 0),
	        new Category("Level", whiteRedColorScale, NumberRange.create(1, 10))
	    };
		WritableGridCoverage2D coverage3 = RepastCoverageFactory.createWritableByteIndexedCoverage(
				"Eric's data indexed", 100, 200, env, categories, null, 0);
		
		geography.addCoverage("My indexed coverage 2", coverage3);
	
		
		// Load Features from shapefiles
//		loadFeatures( "data/TestShapefile2latlon.shp", context, geography);
		loadFeatures( "data/Zones2.shp", context, geography);
		loadFeatures( "data/Agents2.shp", context, geography);
		loadFeatures( "data/WaterLines.shp", context, geography);
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		ScheduleParameters params = ScheduleParameters.createAtEnd(ScheduleParameters.LAST_PRIORITY);

//		schedule.schedule(params, new IAction(){
//			File shpFile = new File("output/testShapeFile.shp");
//			
//			@Override
//			public void execute() {
//				ShapefileWriter shpWriter = new ShapefileWriter(geography);
//				
//				try {
//					shpWriter.write("geography.GisAgent.FeatureType", shpFile.toURI().toURL());
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		
		return context;
	}

	/**
	 * Loads features from the specified shapefile.  The appropriate type of agents
	 * will be created depending on the geometry type in the shapefile (point, 
	 * line, polygon).
	 * 
	 * @param filename the name of the shapefile from which to load agents
	 * @param context the context
	 * @param geography the geography
	 */
	private void loadFeatures (String filename, Context context, Geography geography){
		URL url = null;
		try {
			url = new File(filename).toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		
		// Try to load the shapefile
		SimpleFeatureIterator fiter = null;
		ShapefileDataStore store = null;
		store = new ShapefileDataStore(url);

		try {
			fiter = store.getFeatureSource().getFeatures().features();

			while(fiter.hasNext()){
				features.add(fiter.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			fiter.close();
			store.dispose();
		}
		
		// For each feature in the file
		for (SimpleFeature feature : features){
			Geometry geom = (Geometry)feature.getDefaultGeometry();
			Object agent = null;

			if (!geom.isValid()){
				System.out.println("Invalid geometry: " + feature.getID());
			}
			
			// For Polygons, create ZoneAgents
			if (geom instanceof MultiPolygon){
				MultiPolygon mp = (MultiPolygon)feature.getDefaultGeometry();
				geom = (Polygon)mp.getGeometryN(0);

				// Read the feature attributes and assign to the ZoneAgent
				String name = (String)feature.getAttribute("name");
				double taxRate = (double)feature.getAttribute("Tax_Rate");
//				String name = String.valueOf((Long)feature.getAttribute("OBJECTID"));
//				double taxRate = (double)feature.getAttribute("ProbEggs");

				agent = new ZoneAgent(name,taxRate);

				// Create a BufferZoneAgent around the zone, just for visualization
				Geometry buffer = GeometryUtils.generateBuffer(geography, geom, zoneDistance);
				BufferZoneAgent bufferZone = new BufferZoneAgent("Buffer: " + name, 
						zoneDistance, (ZoneAgent)agent);
				context.add(bufferZone);
				geography.move(bufferZone, buffer);
			}

			// For Points, create RadioTower agents
			else if (geom instanceof Point){
				geom = (Point)feature.getDefaultGeometry();				

				// Read the feature attributes and assign to the ZoneAgent
				String name = (String)feature.getAttribute("Name");
				agent = new RadioTower(name);
			}

			// For Lines, create WaterLines
			else if (geom instanceof MultiLineString){
				MultiLineString line = (MultiLineString)feature.getDefaultGeometry();
				geom = (LineString)line.getGeometryN(0);

				// Read the feature attributes and assign to the ZoneAgent
				String name = (String)feature.getAttribute("Name");
				double flowRate = (Long)feature.getAttribute("Flow_Rate");
				agent = new WaterLine(name, flowRate);
			}

			if (agent != null){
				context.add(agent);
				geography.move(agent, geom);
			}
			else{
				System.out.println("Error creating agent for  " + geom);
			}
		}				
	}
}