package Geography;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.util.ContextUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class GisAgent {

  private double wealth;

  private String name;

  public GisAgent(String name) {
  	this.name = name;
  	wealth = 0;  
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString(){
  	return name;
  }

  public double getWealth() {
    return wealth;
  }

  public void setWealth(double wealth) {
    this.wealth = wealth;
  }

  @ScheduledMethod(start = 1, pick = 1, interval = 1)
	public void step() {  	
    Context context = ContextUtils.getContext(this);

    Geography<GisAgent> geography = (Geography)context.getProjection("Geography");
    Geometry geom = geography.getGeometry(this);
    Coordinate coord = geom.getCoordinates()[0];
    coord.x += RandomHelper.nextDoubleFromTo(-0.005, 0.005);
    coord.y += RandomHelper.nextDoubleFromTo(-0.005, 0.005);
    geography.move(this, geom);

    wealth += RandomHelper.getUniform().nextDoubleFromTo(1,5);
  }
  
  @ScheduledMethod(start = 1, pick = 1, interval = 1)
 	public void reproduce() {

     Context context = ContextUtils.getContext(this);
     Geography<GisAgent> geography = (Geography)context.getProjection("Geography");
     Geometry geom = geography.getGeometry(this);
     Coordinate coord = geom.getCoordinates()[0];

     GisAgent child = new GisAgent("Child Site-" + RandomHelper.nextIntFromTo(1, 1000000));
     context.add(child);
     double xo = RandomHelper.nextDoubleFromTo(-0.05, 0.05);
     double yo = RandomHelper.nextDoubleFromTo(-0.05, 0.05);
     
     Coordinate c2 = new Coordinate(coord.x + xo, coord.y + yo);
     geography.move(child, new GeometryFactory().createPoint(c2));
   }
  
  @ScheduledMethod(start = 5, pick = 1, interval = 1)
 	public void die() {  	 
    Context context = ContextUtils.getContext(this);
    context.remove(this);
   }
}
