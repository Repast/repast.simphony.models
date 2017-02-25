package geozombies;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.query.space.gis.IntersectsQuery;
import repast.simphony.space.gis.Geography;
import repast.simphony.util.ContextUtils;

/**
 * 
 * @author Eric Tatara
 *
 */
public class ZoneAgent {

	protected boolean active;
	
	public List<Human> lookForHumans(){
		List<Human> humanList = new ArrayList<Human>();
		Context context = ContextUtils.getContext(this);

		Geography geography = (Geography)context.getProjection("Geography");
		// Find all features that intersect the zone feature
		IntersectsQuery query = new IntersectsQuery(geography, this);	

		for (Object obj : query.query()) {
			// If the zone finds a water line, then set the zone water rate from the line
			if (obj instanceof Human){
				humanList.add((Human)obj);
			}
		}
		return humanList;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}