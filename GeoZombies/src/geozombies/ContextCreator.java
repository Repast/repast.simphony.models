package geozombies;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
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
 * TODO Change project dependencies to standard Repast model 
 *
 */
public class ContextCreator implements ContextBuilder {

	int numAgents;
	double zoneDistance;
	
	public Context build(Context context) {		
		Parameters parm = RunEnvironment.getInstance().getParameters();
		
		System.setProperty("gov.nasa.worldwind.config.document", "worldwind.xml");
		
//		 System.setProperty("gov.nasa.worldwind.config.file", "geoZombiesMap.properties");
		
		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
				.createGeography("Geography", context, geoParams);

		GeometryFactory fac = new GeometryFactory();

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
				"infection network", context, true);
		
		Network net = netBuilder.buildNetwork();
		
		// Bind the geography and network events to create edge geometries for visualization
		new GISNetworkListener(context, geography, net);
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int zombieCount = (Integer) params.getValue("zombie_count");
		for (int i = 0; i < zombieCount; i++) {
			Zombie zombie = new Zombie();
			context.add(zombie);
			
			// TODO set the boundaries in which to create agents
			Coordinate coord = new Coordinate(-88 + 0.5* Math.random(), 41.5 + 0.5 * Math.random());
			Point geom = fac.createPoint(coord);
			
			geography.move(zombie, geom);
		}

		int humanCount = (Integer) params.getValue("human_count");
		for (int i = 0; i < humanCount; i++) {
			int energy = RandomHelper.nextIntFromTo(4, 10);
			
			Human human = new Human(energy);
			
			context.add(human);
			
			// TODO set the boundaries in which to create agents
			Coordinate coord = new Coordinate(-88 + 0.5* Math.random(), 41.5 + 0.5 * Math.random());
			Point geom = fac.createPoint(coord);
			
			geography.move(human, geom);
		}
		
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}
		
		return context;
	}
}