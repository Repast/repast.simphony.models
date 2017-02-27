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
public class InfectionNetworkStyle implements SurfaceShapeStyle<InfectionNetworkEdge>{

	@Override
	public SurfaceShape getSurfaceShape(InfectionNetworkEdge object, SurfaceShape shape) {
	  return new SurfacePolyline();
	}

	@Override
	public Color getFillColor(InfectionNetworkEdge obj) {
		return null;
	}

	@Override
	public double getFillOpacity(InfectionNetworkEdge obj) {
		return 0;
	}

	@Override
	public Color getLineColor(InfectionNetworkEdge obj) {
		return Color.BLUE;
	}

	@Override
	public double getLineOpacity(InfectionNetworkEdge obj) {
		return 1.0;
	}

	@Override
	public double getLineWidth(InfectionNetworkEdge obj) {
		return 2;
	}
}