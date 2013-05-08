package zombies.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import repast.simphony.relogo.WayPoint;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

/**
 * 2D style for test model agents.
 */

public class WayPointStyle implements StyleOGL2D<WayPoint> {

	ShapeFactory2D factory;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public Color getBorderColor(WayPoint object) {
		return null;
	}

	@Override
	public int getBorderSize(WayPoint object) {
		return 0;
	}

	@Override
	public Color getColor(WayPoint object) {
		return Color.black;
	}

	@Override
	public float getRotation(WayPoint object) {
		return 0;
	}

	@Override
	public float getScale(WayPoint object) {
		return 0;
	}

	@Override
	public VSpatial getVSpatial(WayPoint object, VSpatial spatial) {
		if (spatial == null) {
			spatial = factory.createShape(
					new Rectangle2D.Float(-0.1f / 2f, -0.1f / 2f, 0.1f, 0.1f));
		}
		return spatial;
	}

	@Override
	public String getLabel(WayPoint object) {
		return null;
	}

	@Override
	public Font getLabelFont(WayPoint object) {
		return null;
	}

	@Override
	public float getLabelXOffset(WayPoint object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(WayPoint object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(WayPoint object) {
		return null;
	}

	@Override
	public Color getLabelColor(WayPoint object) {
		return null;
	}
}
