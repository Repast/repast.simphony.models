package flock.styles;

import java.awt.Color;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.style.DefaultEdgeStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * Style for Predator track edges.  The network edge style is EdgeType.LINE, so
 *   it is not neccessary to return a BranchGroup.
 * 
 * @author Eric Tatara
 *
 */
public class NetEdgeStyle extends DefaultEdgeStyle3D{

	@Override
	public TaggedBranchGroup getBranchGroup(Object o, TaggedBranchGroup taggedGroup) {
		return null;
	}

	@Override
	public EdgeType getEdgeType() {
		return EdgeType.LINE;
	}

	@Override
	public TaggedAppearance getAppearance(Object t, TaggedAppearance taggedAppearance, Object shapeID) {
		if (taggedAppearance == null || taggedAppearance.getTag() == null) {
			
			// Set the line color to pink.
			taggedAppearance = new TaggedAppearance("DEFAULT");
			AppearanceFactory.setColoredAppearance(taggedAppearance.getAppearance(), Color.PINK);
			return taggedAppearance;
		}
		return null;
	}
}