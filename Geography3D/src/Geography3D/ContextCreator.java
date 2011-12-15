package Geography3D;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureIterator;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.graph.Network;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Model for testing NASA WorldWind display of geography projection
 * 
 * @author Eric Tatara
 *
 */
public class ContextCreator implements ContextBuilder<GisAgent> {

	public Context build(Context context) {

		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
		.createGeography("Geography", context, geoParams);

		Network network = NetworkFactoryFinder.createNetworkFactory(null)
		.createNetwork("Network", context, true);

		GeometryFactory fac = new GeometryFactory();

		for (int i = 0; i < 20; i++) {
			GisAgent agent = new GisAgent("Site " + i);
			context.add(agent);
			agent.setWealth(i);
			Coordinate coord = new Coordinate(-87.75 + 0.1* Math.random(), 41.82 + 0.1 * Math.random());
			Point geom = fac.createPoint(coord);
			geography.move(agent, geom);

//			if (i > 1){
//				GisAgent otherAgent = (GisAgent)context.getRandomObjects(GisAgent.class, 1).iterator().next();
//
//				network.addEdge(agent, otherAgent);
//			}
		}

//		String filename = System.getProperty("ZoneShapeFile");
    String filename = "gisdata/Zones.shp";
		
		URL url = null;
		try {
			url = new File(filename).toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FeatureIterator fiter = null;
		ShapefileDataStore store = null;
		try {
			store = new ShapefileDataStore(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			fiter = store.getFeatureSource().getFeatures().features();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(fiter.hasNext()){
			Feature feature = fiter.next();

			MultiPolygon mp = (MultiPolygon)feature.getDefaultGeometry();
			Polygon polygon = (Polygon)mp.getGeometryN(0);
			
			ZoneAgent zone = new ZoneAgent();
			context.add(zone);
			geography.move(zone, polygon);
		}				
		return context;
	}
}