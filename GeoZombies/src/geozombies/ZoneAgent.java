package geozombies;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

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

	protected boolean visible = false;
	protected boolean active = false;
	
	public List<Human> lookForHumans(){
		List<Human> humanList = new ArrayList<Human>();
		Context context = ContextUtils.getContext(this);

		Geography geography = (Geography)context.getProjection("Geography");
		// Find all features that intersect the zone feature
		IntersectsQuery query = new IntersectsQuery(geography, this);	

		for (Object obj : query.query()) {
			if (obj instanceof Human){
				humanList.add((Human)obj);
			}
		}
		return humanList;
	}

	public List<Human> lookForHumans(List<Human> nearHumans){
		List<Human> humanList = new ArrayList<Human>();
		Context context = ContextUtils.getContext(this);

		Geography geography = (Geography)context.getProjection("Geography");
		
		// Find all features that intersect the zone feature
			
		Geometry thisGeom = geography.getGeometry(this);
		
		for (Human human : nearHumans){
			if (thisGeom.intersects(geography.getGeometry(human))){
				humanList.add(human);
			}
		}
		
		return humanList;
	}
	
	public List<?> lookForObjects(List<?> nearObjects){
		List<Object> objectList = new ArrayList<Object>();
		Context context = ContextUtils.getContext(this);

		Geography geography = (Geography)context.getProjection("Geography");
		
		// Find all features that intersect the zone feature
			
		Geometry thisGeom = geography.getGeometry(this);
		
		for (Object o : nearObjects){
			if (thisGeom.intersects(geography.getGeometry(o))){
				objectList.add(o);
			}
		}
		
		return objectList;
	}
	
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}