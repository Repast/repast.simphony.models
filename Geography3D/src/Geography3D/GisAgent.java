package Geography3D;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author tatara
 *
 */
public class GisAgent {

  private double wealth = 10;
  private String name;
  private int type;

	public GisAgent(String name) {
  	this.name = name;
  	type = RandomHelper.getUniform().nextIntFromTo(0, 5);
	}

  @ProbeID
  public String getName() {
    return name;
  }

  public double getWealth() {
    return wealth;
  }

  public void setWealth(double wealth) {
    this.wealth = wealth;
  }
  
  public int getType() {
		return type;
	}

  @ScheduledMethod(start = 1, interval = 1)
	public void move() {  	
    Context context = ContextUtils.getContext(this);

    Geography<GisAgent> geography = (Geography)context.getProjection("Geography");
    Geometry geom = geography.getGeometry(this);
    Coordinate coord = geom.getCoordinates()[0];
    coord.x += 0.01*(Math.random() - .5);
    coord.y += 0.01*(Math.random() - .5);
    geography.move(this, geom);

    this.wealth = RandomHelper.getUniform().nextIntFromTo(0, 8);
  }
    
  @ScheduledMethod(start = 1, interval = 1,  pick=1)
	public void reproduceAndDie() {
  	Context<GisAgent> context = ContextUtils.getContext(this);
  	Geography<GisAgent> geography = (Geography)context.getProjection("Geography");
  	Network<GisAgent> network = (Network)context.getProjection("Network");
  	Geometry geom = geography.getGeometry(this);
  	
  	// 50% chance to reproduce and die
  	if (RandomHelper.nextDouble() > 0.5){
  		GisAgent child = new GisAgent("child");
  		context.add(child);
  		
  		GeometryFactory fac = new GeometryFactory();
  		double x = geom.getCoordinates()[0].x;
  		double y = geom.getCoordinates()[0].y;

  	  Point point = fac.createPoint(new Coordinate(x,y));
  		geography.move(child, point);

  		// connect the child to two other random agents in the network
  		GisAgent otherAgent = context.getRandomObjects(GisAgent.class, 1).iterator().next();
  		network.addEdge(child, otherAgent);
  		
  		otherAgent = context.getRandomObjects(GisAgent.class, 1).iterator().next();
  		network.addEdge(child, otherAgent);

  		// kill the parent
  		context.remove(this);
  	}
  }
}