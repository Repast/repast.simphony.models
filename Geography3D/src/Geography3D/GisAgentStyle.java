package Geography3D;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.render.BasicWWTexture;

import java.net.URL;

import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;

/**
 * 
 * @author Eric Tatara
 *
 */
public class GisAgentStyle extends DefaultMarkStyle<GisAgent>{
	
	@Override
	public BasicWWTexture getTexture(GisAgent object, BasicWWTexture texture) {
	
		if (texture != null)
			return texture;

		URL localUrl = WorldWind.getDataFileStore().requestFile("icons/bug.png");
		if (localUrl != null)	{
			return new BasicWWTexture(localUrl, false);
		}
		
//		Color color = Color.red;
//		
//		BufferedImage image = PatternFactory.createPattern(PatternFactory.PATTERN_SQUARE, 
//				new Dimension(10, 10), 0.7f,  color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
//		
//		return new BasicWWTexture(image);	
		
		return null;
	}
}