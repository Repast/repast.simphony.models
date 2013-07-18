package Geography;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.visualization.gis3D.WWUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Model for testing NASA WorldWind display of geography projection
 * 
 * @author Eric Tatara
 *
 */
public class ContextCreator implements ContextBuilder {

	public Context build(Context context) {

		GeographyParameters geoParams = new GeographyParameters();
		Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
		.createGeography("Geography", context, geoParams);

		GeometryFactory fac = new GeometryFactory();

		for (int i = 0; i < 1000; i++) {
			GisAgent agent = new GisAgent("Site " + i);
			context.add(agent);
			Coordinate coord = new Coordinate(-87.75 + 0.1* Math.random(), 41.82 + 0.1 * Math.random());
			Point geom = fac.createPoint(coord);
			geography.move(agent, geom);
		}

    loadFeatures( "gisdata/Zones.shp", context, geography);
//    loadFeatures( "gisdata/Agents.shp", context, geography);
//    loadFeatures( "gisdata/Water_lines.shp", context, geography);
	
		return context;
	}
	
	private void loadFeatures (String filename, Context context, Geography geography){
		URL url = null;
		try {
			url = new File(filename).toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleFeatureIterator fiter = null;
		ShapefileDataStore store = null;
		try {
			store = new ShapefileDataStore(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		CoordinateReferenceSystem crs = null;
		try {
			crs = store.getSchema().getCoordinateReferenceSystem();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		if (crs != null)
//			geography.setCRS(crs);
		
		try {
			fiter = store.getFeatureSource().getFeatures().features();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(fiter.hasNext()){
			SimpleFeature feature = fiter.next();
      Geometry geom = (Geometry)feature.getDefaultGeometry();
			Object agent = null;
      
			if (geom instanceof MultiPolygon){
				MultiPolygon mp = (MultiPolygon)feature.getDefaultGeometry();
				geom = (Polygon)mp.getGeometryN(0);
				
				String name = (String)feature.getAttribute("Name");
				
				agent = new ZoneAgent(name);
			}
			else if (geom instanceof Point){
				geom = (Point)feature.getDefaultGeometry();				
				String name = (String)feature.getAttribute("Name");
//				double wealth = (double)feature.getAttribute("Wealth");
				
				agent = new GisAgent(name);
			}
			else if (geom instanceof MultiLineString){
				MultiLineString line = (MultiLineString)feature.getDefaultGeometry();
				geom = (LineString)line.getGeometryN(0);
				String name = (String)feature.getAttribute("Name");
				
				agent = new WaterLine(name);
			}
			
			if (agent != null){
			  context.add(agent);
			  
			  if (crs != null){
					WWUtils.projectGeometryToWGS84(geom, crs);
				}
			  
			  geography.move(agent, geom);
			}
			else{
				System.out.println("NULL agent for  " + geom);
			}
			
		}				
	}
}