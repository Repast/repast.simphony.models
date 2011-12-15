package Geography3D;

import java.awt.Color;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualization.gis3D.Material;
import repast.simphony.visualization.gis3D.MaterialFactory;
import repast.simphony.visualization.gis3D.style.EdgeStyleGIS3D;

/**
 * 
 * @author Eric Tatara
 *
 */
public class GisNetStyle implements EdgeStyleGIS3D<RepastEdge>{

	private Color edgeColor;
	
	public GisNetStyle(){
		int d = RandomHelper.nextIntFromTo(0, 5);
		
		switch(d){
		  case 0 : edgeColor = Color.BLUE; break;
		  case 1 : edgeColor = Color.RED; break;
		  case 2 : edgeColor = Color.GREEN; break;
		  case 3 : edgeColor = Color.ORANGE; break;
		  case 4 : edgeColor = Color.YELLOW; break;
		  case 5 : edgeColor = Color.PINK; break;
		}
		
	}
	
	public Material getMaterial(RepastEdge obj, Material material) {
		return MaterialFactory.setMaterialAppearance(material, edgeColor);
	}

	public float edgeRadius(RepastEdge obj) {
		return 20;
	}

	public boolean isScaled(RepastEdge object) {
		return false;
	}

}