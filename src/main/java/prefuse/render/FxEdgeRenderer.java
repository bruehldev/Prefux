package prefuse.render;

//import java.awt.BasicStroke;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.Shape;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.CubicCurve2D;
//import java.awt.geom.Line2D;
//import prefuse.data.util.Point2D;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.shape.Line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefuse.util.FxGraphicsLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

/**
 * <p>
 * Renderer that draws edges as lines connecting nodes. Both straight and curved
 * lines are supported. Curved lines are drawn using cubic Bezier curves.
 * Subclasses can override the
 * {@link #getCurveControlPoints(EdgeItem, Point2D[], double, double, double, double)}
 * method to provide custom control point assignment for such curves.
 * </p>
 *
 * <p>
 * This class also supports arrows for directed edges. See the
 * {@link #setArrowType(int)} method for more.
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FxEdgeRenderer implements Renderer {

	private static final Logger log = LoggerFactory
			.getLogger(FxEdgeRenderer.class);

	/**
	 * @see prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem)
	 */
	public void render(Parent p, VisualItem item) {
		log.debug("render");
		EdgeItem edge = (EdgeItem) item;
		if (item.getNode() == null) {
			VisualItem item1 = edge.getSourceItem();
			if (item1.getNode() == null) {
				item1.render(p);
			}
			VisualItem item2 = edge.getTargetItem();
			if (item2.getNode() == null) {
				item2.render(p);
			}
			Line li = new Line();
			li.startXProperty().bind(item1.getNode().layoutXProperty());
			li.startYProperty().bind(item1.getNode().layoutYProperty());
			li.endXProperty().bind(item2.getNode().layoutXProperty());
			li.endYProperty().bind(item2.getNode().layoutYProperty());
			edge.setNode(li);
			FxGraphicsLib.addToParent(p, li);
		}
	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		log.debug("locatePoint " + p + " " + item);
		return false;
	}

	@Override
	public void setBounds(VisualItem item) {
		log.debug("setBounds " + item);

	}

} // end of class EdgeRenderer
