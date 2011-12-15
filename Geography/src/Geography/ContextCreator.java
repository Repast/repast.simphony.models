package Geography;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;


public class ContextCreator implements ContextBuilder<GisAgent> {


  public Context build(Context<GisAgent> context) {

    GeographyParameters geoParams = new GeographyParameters();
    Geography geography = GeographyFactoryFinder.createGeographyFactory(null)
            .createGeography("Geography", context, geoParams);

    GridFactoryFinder.createGridFactory(null).createGrid("Grid", context,
            GridBuilderParameters.multiOccupancy2D(new RandomGridAdder(), new StickyBorders(),
                    30, 30));

    /*
    File file = new File("/Users/nick/tmp/another.shp");
    ShapefileLoader loader = null;
    try {
      loader = new ShapefileLoader(GisAgent.class, file.toURL(), geography, context);
      loader.load();
    } catch (MalformedURLException e) {
      e.printStackTrace(); 
    }
    */


    GeometryFactory fac = new GeometryFactory();

    for (int i = 0; i < 10; i++) {
      GisAgent agent = new GisAgent();
      context.add(agent);
      agent.setWealth(i);
      Coordinate coord = new Coordinate(-103.823 + i / 100.0, 44.373);
      Point geom = fac.createPoint(coord);
      geography.move(agent, geom);
    }



    return context;
  }

}
