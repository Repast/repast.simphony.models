package flock.styles;

import repast.simphony.visualization.visualization3D.style.DefaultStyle3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

/**
 * Style for Waypoints.  The waypoints are the nodes between network edges, but
 *   we'd like the nodes to be invisible, so the returned BranchGroup does not
 *   assign any shape.
 *  
 * @author Eric Tatara
 *
 */
public class WaypointStyle extends DefaultStyle3D {

  public TaggedBranchGroup getBranchGroup(Object o, TaggedBranchGroup taggedGroup) {
  	 if (taggedGroup == null || taggedGroup.getTag() == null) {
       taggedGroup = new TaggedBranchGroup("DEFAULT");
       return taggedGroup;
     }
  	 
  	 return null;
  }

  public TaggedAppearance getAppearance(Object t, TaggedAppearance taggedAppearance, Object shapeID) {
  	return null;
  }

}
