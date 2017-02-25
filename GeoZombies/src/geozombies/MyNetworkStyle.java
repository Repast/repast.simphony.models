package geozombies;

import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.render.SurfaceShape;

import java.awt.Color;

import repast.simphony.visualization.gis3D.style.SurfaceShapeStyle;

/**
 * Style for MyNetworkEdges.
 * 
 * @author Eric Tatara
 *
 */
public class MyNetworkStyle implements SurfaceShapeStyle<MyNetworkEdge>{

	@Override
	public SurfaceShape getSurfaceShape(MyNetworkEdge object, SurfaceShape shape) {
	  return new SurfacePolyline();
	}

	@Override
	public Color getFillColor(MyNetworkEdge obj) {
		return null;
	}

	@Override
	public double getFillOpacity(MyNetworkEdge obj) {
		return 0;
	}

	@Override
	public Color getLineColor(MyNetworkEdge obj) {
		return Color.BLUE;
	}

	@Override
	public double getLineOpacity(MyNetworkEdge obj) {
		return 1.0;
	}

	@Override
	public double getLineWidth(MyNetworkEdge obj) {
		return 2;
	}
}