package zombies.style;

import java.awt.Color;

import repast.simphony.relogo.BaseLink;
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
public class LinkStyle implements EdgeStyleOGL2D {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getColor(repast.simphony
	 * .space.graph.RepastEdge)
	 */
	public Color getColor(RepastEdge<?> edge) {
		BaseLink<?> link = (BaseLink<?>) edge;
		return ReLogoSupport.lookupColor(link.getColor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.EdgeStyleOGL2D#getLineWidth(repast
	 * .simphony.space.graph.RepastEdge)
	 */
	public int getLineWidth(RepastEdge<?> edge) {
		return (int) (Math.abs(edge.getWeight()));
	}

}
