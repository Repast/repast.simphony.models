package zombies.style;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.relogo.Turtle;
import repast.simphony.relogo.image.NLImage;
import repast.simphony.relogo.styles.NLImageSpatialSource;
import repast.simphony.relogo.styles.ReLogoImageSpatialSource;
import repast.simphony.relogo.styles.ReLogoSVGSpatialSource;
import repast.simphony.relogo.styles.ReLogoSpatialSource;
import repast.simphony.relogo.styles.StyleUtility;
import repast.simphony.relogo.util.ReLogoSupport;
import repast.simphony.scenario.ScenarioUtils;
import repast.simphony.visualizationOGL2D.SVGSpatialSource;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

import com.thoughtworks.xstream.XStream;

/**
 * 2D style for turtles.
 */

public class TurtleStyle implements StyleOGL2D<Turtle> {

	ShapeFactory2D shapeFactory;
	Map<String, ReLogoSpatialSource> reLogoSpatialSources = new HashMap<String, ReLogoSpatialSource>();

	@SuppressWarnings("unchecked")
	public void init(ShapeFactory2D shapeFactory) {
		this.shapeFactory = shapeFactory;
		Map<String, String> props = new HashMap<String, String>();
		props.put(SVGSpatialSource.KEY_BSQUARE_SIZE, "15");
		XStream xstream = new XStream();
		xstream.setClassLoader(this.getClass().getClassLoader());
		File scenarioDir = ScenarioUtils.getScenarioDir();
		if (scenarioDir != null) {
			File projectDir = scenarioDir.getParentFile();
			if (projectDir != null) {
				File shapesDir = new File(projectDir, "shapes");
				if (shapesDir != null) {
					String xml = "turtleShapes.xml";
					try {
						File shapesXml = new File(shapesDir, xml); 
						if (shapesXml.exists()) {
							for (NLImage image : (List<NLImage>) xstream
									.fromXML(new FileReader(shapesXml))) {
								NLImageSpatialSource source = new NLImageSpatialSource(
										image);
								reLogoSpatialSources
										.put(source.getID(), source);
								try {
									source.registerSource(shapeFactory, props);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						Map<String, String> svgAndImageProps = new HashMap<String, String>();
			svgAndImageProps.put(SVGSpatialSource.KEY_BSQUARE_SIZE, "90");
						try {
							Map<String, String> svgFileNamesAndPaths = StyleUtility
									.getSVGFileNamesAndPaths(shapesDir);
							for (String svgFileName : svgFileNamesAndPaths
									.keySet()) {
								ReLogoSVGSpatialSource source = new ReLogoSVGSpatialSource(
										svgFileName,
										svgFileNamesAndPaths.get(svgFileName));
								reLogoSpatialSources
										.put(source.getID(), source);
								if (source.isSimple()) {
									// use props
									source.registerSource(shapeFactory, props);
								} else {
									// use svgAndImageProps
									source.registerSource(shapeFactory,
											svgAndImageProps);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						try {
							Map<String, String> imageTurtleShapesMap = StyleUtility
									.getImageFileNamesAndPaths(shapesDir);
							for (String imageFileName : imageTurtleShapesMap
									.keySet()) {
								ReLogoImageSpatialSource source = new ReLogoImageSpatialSource(
										imageFileName,
										imageTurtleShapesMap.get(imageFileName));
								reLogoSpatialSources
										.put(source.getID(), source);
								source.registerSource(shapeFactory,
										svgAndImageProps);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public VSpatial getVSpatial(Turtle agent, VSpatial spatial) {
		String agentShape = agent.getShape();
		if (spatial != null) {
			if (agent.isShapeChanged()) {
				if (!shapeFactory.isNameRegistered(agentShape)) {
					spatial = shapeFactory.getNamedSpatial("default");
				} else {
					spatial = shapeFactory.getNamedSpatial(agentShape);
				}
				agent.setShapeChanged(false);
			} else if (agent.isVisibilityChanged()) {
				spatial.setVisible(!agent.isHiddenQ());
				agent.setVisibilityChanged(false);
			}
		} else {
			if (!shapeFactory.isNameRegistered(agentShape)) {
				spatial = shapeFactory.getNamedSpatial("default");
			} else {
				spatial = shapeFactory.getNamedSpatial(agentShape);
			}
		}
		return spatial;
	}

	public Color getColor(Turtle agent) {
		return ReLogoSupport.lookupColor(agent.getColor());
	}

	public float getRotation(Turtle agent) {
		String agentShape = agent.getShape();

		if (!shapeFactory.isNameRegistered(agentShape)) {
			// Default shape rotates
			return (float) agent.getHeading();
		}
		float offset = reLogoSpatialSources.get(agentShape).getOffset();
		if (reLogoSpatialSources.get(agentShape).doRotate()) {
			return offset + (float) agent.getHeading();
		} else {
			return offset;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderColor(java.lang.
	 * Object)
	 */
	public Color getBorderColor(Turtle object) {
		return Color.BLACK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getBorderSize(java.lang
	 * .Object )
	 */
	public int getBorderSize(Turtle object) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.visualizationOGL2D.StyleOGL2D#getScale(java.lang.Object)
	 */
	public float getScale(Turtle object) {
		ReLogoSpatialSource rss = reLogoSpatialSources.get(object.getShape());
		if (rss != null && !rss.isSimple()) {
			return (float) object.getSize() / 6f;
		}
		return (float) object.getSize();
	}

	@Override
	public String getLabel(Turtle object) {
		Object label = object.getLabel();
		if (label != null && !label.toString().equals("")) {
			return label.toString();
		}
		return null;
	}

	@Override
	public Font getLabelFont(Turtle object) {
		return new Font("sansserif", Font.PLAIN, 12);
	}

	@Override
	public float getLabelXOffset(Turtle object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(Turtle object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(Turtle object) {
		return Position.NORTH;
	}

	@Override
	public Color getLabelColor(Turtle object) {
		return ReLogoSupport.lookupColor(object.getLabelColor());
	}
}
