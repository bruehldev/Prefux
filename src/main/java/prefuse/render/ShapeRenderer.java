package prefuse.render;

//import java.awt.Shape;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.GeneralPath;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jfxmediaimpl.platform.Platform;

import prefuse.util.FxGraphicsLib;
import prefuse.visual.VisualItem;

/**
 * Renderer for drawing simple shapes. This class provides a number of built-in
 * shapes, selected by an integer value retrieved from a VisualItem.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ShapeRenderer implements Renderer {

	Logger log = LoggerFactory.getLogger(ShapeRenderer.class);

	public double DEFAULT_RADIUS = 5.0;
	public static final String DEFAULT_STYLE_CLASS = "prefx-shape";

	@Override
	public void render(Parent g, VisualItem item) {
		log.debug("Rendering " + item);
		Node shape = item.getNode();
		if (shape == null) {
			shape = new Circle(DEFAULT_RADIUS);
			item.setNode(shape);
			FxGraphicsLib.addToParent(g, shape);
		}
		item.setStyle(DEFAULT_STYLE_CLASS);

	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBounds(VisualItem item) {
		log.debug("setBounds " + item);
		Node node = item.getNode();
		javafx.application.Platform.runLater(() -> {
			log.debug("Setting new coords: "+item.getX()+" / "+item.getY());
			node.setLayoutX(item.getX());
			node.setLayoutY(item.getY());
		});

	}

} // end of class ShapeRenderer
