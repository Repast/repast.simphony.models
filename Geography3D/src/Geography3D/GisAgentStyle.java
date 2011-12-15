package Geography3D;

import java.awt.Color;

import repast.simphony.visualization.gisnew.DefaultStyleGIS;
import repast.simphony.visualization.gisnew.GeoShape.FeatureType;

/**
 * 
 * @author Eric Tatara
 *
 */
public class GisAgentStyle extends DefaultStyleGIS<GisAgent>{

	@Override
	public FeatureType getFeatureType(GisAgent agent) {
		return FeatureType.POINT;
	}
	
  @Override
  public Color getFillColor(GisAgent agent) {
		return Color.GREEN;
	}

}