package zombies.style;
/**
 * 
 */

import java.awt.Color;

import repast.simphony.relogo.TrackingEdge;
import repast.simphony.relogo.util.ReLogoSupport;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;

/**
 * ReLogo implementation of OGL 2D edge style.
 * 
 * 
 * @author Nick Collier
 * @author jozik
 */
public class TrackingLinkStyle implements EdgeStyleOGL2D {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getColor(repast.simphony
	 * .space.graph.RepastEdge)
	 */
	public Color getColor(RepastEdge<?> edge) {
		return ReLogoSupport.lookupColor(((TrackingEdge<?>)edge).getColor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getLineWidth(repast
	 * .simphony.space.graph.RepastEdge)
	 */
	public int getLineWidth(RepastEdge<?> edge) {
		return ((TrackingEdge<?>)edge).getSize();
	}

}
