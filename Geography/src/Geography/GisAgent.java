package Geography;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.jscience.physics.amount.Amount;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;
import repast.simphony.random.RandomHelper;
import repast.simphony.annotate.AgentAnnot;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

import javax.measure.unit.SI;

@AgentAnnot(displayName = "Geo Agent")
public class GisAgent {

  private double wealth = 10;
  private Amount amount = Amount.valueOf(10, SI.METER);

  public GisAgent() {
	}

  @ProbeID
  public String getName() {
    return "Site 3";
  }


  public double getWealth() {
    return wealth;
  }

  public void setWealth(double wealth) {
    this.wealth = wealth;
  }


  /*
  public void setAmount(Amount amt) {
    amount = amt;
  }

  public Amount getAmount() {
    return amount;
  }
  */

  @ScheduledMethod(start = 1, pick = 1, interval = 1)
	public void step() {

    Context context = ContextUtils.getContext(this);

    Geography<GisAgent> geography = (Geography)context.getProjection("Geography");
    Geometry geom = geography.getGeometry(this);
    Coordinate coord = geom.getCoordinates()[0];
    coord.x += .005;
    coord.y += .005;
    geography.move(this, geom);

    //GisAgent agent = new GisAgent();
    //context.add(agent);
    //geography.move(agent, new GeometryFactory().createPoint(coord));



    this.wealth = RandomHelper.getUniform().nextIntFromTo(0, 8);
  }
}
