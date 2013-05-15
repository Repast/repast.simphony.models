package zombies.style;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.relogo.Patch;
import repast.simphony.relogo.util.ReLogoSupport;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

public class PatchStyle implements StyleOGL2D<Patch> {

	ShapeFactory2D factory;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderColor(java.lang
	 * .Object)
	 */
	public Color getBorderColor(Patch object) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderSize(java.lang
	 * .Object)
	 */
	public int getBorderSize(Patch object) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getColor(java.lang.Object)
	 */
	public Color getColor(Patch object) {
		return ReLogoSupport.lookupColor(object.getPcolor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getRotation(java.lang.Object
	 * )
	 */
	public float getRotation(Patch object) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getScale(java.lang.Object)
	 */
	public float getScale(Patch object) {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getVSpatial(java.lang.Object
	 * , saf.v3d.scene.VSpatial)
	 */
	public VSpatial getVSpatial(Patch object, VSpatial spatial) {
		if (spatial == null)
			return factory.createRectangle(15, 15);
		return spatial;
	}

	@Override
	public String getLabel(Patch object) {
		Object plabel = object.getPlabel();
		if (plabel != null && !plabel.toString().equals("")) {
			return plabel.toString();
		}
		return null;
	}

	@Override
	public Font getLabelFont(Patch object) {
		return new Font("sansserif", Font.PLAIN, 12);
	}

	@Override
	public float getLabelXOffset(Patch object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(Patch object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(Patch object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(Patch object) {
		return ReLogoSupport.lookupColor(object.getPlabelColor());
	}
  
}
