package Geography3D;

import repast.simphony.visualization.gisnew.DefaultStyleGIS;
import repast.simphony.visualization.gisnew.GeoShape;
import repast.simphony.visualization.gisnew.GeoShapeFactory;
import repast.simphony.visualization.gisnew.GeoShape.FeatureType;

/**
 * 
 * @author tatara
 *
 */
public class ZoneStyle extends DefaultStyleGIS<ZoneAgent>{

	@Override
	public FeatureType getFeatureType(ZoneAgent agent) {
		return FeatureType.POLYGON;
	}
	
	public GeoShape getShape(ZoneAgent agent) {		
		return GeoShapeFactory.createPolygon();
	}
}