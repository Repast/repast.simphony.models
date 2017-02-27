package geozombies;

import java.net.URL;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.BasicWWTexture;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.WWTexture;
import repast.simphony.random.RandomHelper;
import repast.simphony.visualization.gis3D.PlaceMark;
import repast.simphony.visualization.gis3D.style.DefaultMarkStyle;

/**
 * Style for Human Agents.
 * 
 * @author Eric Tatara
 *
 */
public class HumanStyle extends DefaultMarkStyle<Human>{

	/**
	 * The gov.nasa.worldwind.render.Offset is used to position the icon from 
	 *   the mark point location.  If no offset is provided, the lower left corner
	 *   of the icon is located at the point (lat lon) position.  Using values of
	 *   (0.5,0.5) will position the icon center over the lat lon location.
	 *   The first two arguments in the Offset constructor are the x and y 
	 *   offset values.  The third and fourth arguments are the x and y units 
	 *   for the offset. AVKey.FRACTION represents units of the image texture 
	 *   size, with 1.0 being one image width/height.  AVKey.PIXELS can be used 
	 *   to specify the offset in pixels. 
	 */
	Offset iconOffset = new Offset(0.5d, 0.5d, AVKey.FRACTION, AVKey.FRACTION);

	
	// TODO Remove mark field and getPlaceMark() call for Repast 2.5 release.  
	//      It is used here because getIconOffset is not called by the display.
	PlaceMark mark;

	@Override
	public PlaceMark getPlaceMark(Human agent, PlaceMark mark) {

		if (mark == null)
			mark = new PlaceMark();

		mark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
		mark.setLineEnabled(false);

		mark.getAttributes().setImageOffset(getIconOffset(agent));

		this.mark = mark;

		return mark;
	}
	
	
	/**
	 * Here we set the appearance of the TowerAgent using a non-changing icon.
	 */
	@Override
	public WWTexture getTexture(Human agent, WWTexture texture) {
			
		// If the texture is already defined, then just return the same texture since
		//  we don't want to update the tower agent appearance.  The only time the 
		//  below code will actually be used is on the initialization of the display
		//  when the icons are created.
		if (texture != null)
			return texture;
		
		String fileName = "icons/female_avatar2.png";
		if (RandomHelper.nextIntFromTo(0, 1) == 0){
			fileName = "icons/male_avatar2.png";
		}
		
		// BasicWWTexture is useful when the texture is a non-changing image.
		URL localUrl = WorldWind.getDataFileStore().requestFile(fileName);
		if (localUrl != null)	{
			return new BasicWWTexture(localUrl, false);
		}
		
		return null;
	}
	
	@Override
	public Offset getIconOffset(Human agent){
		return iconOffset;
	}
	
	// TODO Remove offset call for Repast 2.5 release.  It is used here because
	//      getIconOffset is not called by the display.
	@Override	
	public double getScale(Human agent) {
			
		mark.getAttributes().setImageOffset(getIconOffset(agent));
		return 1;
	} 

}