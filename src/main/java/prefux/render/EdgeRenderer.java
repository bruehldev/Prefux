package prefux.render;

//import java.awt.BasicStroke;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.Shape;
//import java.awt.geom.AffineTransform;
//import java.awt.geom.CubicCurve2D;
//import java.awt.geom.Line2D;
//import prefux.data.util.Point2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;

/**
 * <p>Renderer that draws edges as lines connecting nodes. Both straight and
 * curved lines are supported. Curved lines are drawn using cubic Bezier curves.
 * Subclasses can override the
 * {@link #getCurveControlPoints(EdgeItem, Point2D[], double, double, double, double)}
 * method to provide custom control point assignment for such curves.</p>
 *
 * <p>This class also supports arrows for directed edges. See the
 * {@link #setArrowType(int)} method for more.</p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class EdgeRenderer extends AbstractShapeRenderer {


	@Override
	protected Node getRawShape(VisualItem item) {
		// TODO Auto-generated method stub
		return null;
	}


} // end of class EdgeRenderer
