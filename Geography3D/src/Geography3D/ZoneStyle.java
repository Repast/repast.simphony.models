package Geography3D;

import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

/**
 * 
 * @author tatara
 *
 */
public class ZoneStyle implements SurfaceShapeStyle<ZoneAgent>{

	@Override
	public SurfaceShape getSurfaceShape(ZoneAgent object, SurfaceShape shape) {
	  return new SurfacePolygon();
	}

	@Override
	public Color getFillColor(ZoneAgent obj) {
		return Color.BLUE;
	}

	@Override
	public double getFillOpacity(ZoneAgent obj) {
		return 0.25;
	}

	@Override
	public Color getLineColor(ZoneAgent obj) {
		return Color.GREEN;
	}

	@Override
	public double getLineOpacity(ZoneAgent obj) {
		return 1.0;
	}

	@Override
	public double getLineWidth(ZoneAgent obj) {
		return 3;
	}
}