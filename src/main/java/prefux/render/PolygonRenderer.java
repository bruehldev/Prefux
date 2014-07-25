package prefux.render;

import javafx.scene.Node;
import prefux.visual.VisualItem;


/**
 * <p>Renderer for drawing a polygon, either as a closed shape, or as a
 * series of potentially unclosed curves. VisualItems must have a data field
 * containing an array of doubles that tores the polyon. A {@link double#NaN}
 * value can be used to mark the end point of the polygon for double arrays
 * larger than their contained points. By default, this renderer will
 * create closed paths, joining the first and last points in the point
 * array if necessary. The {@link #setClosePath(boolean)} method can be
 * used to render open paths, such as poly-lines or poly-curves.</p>
 * 
 * <p>A polygon edge type parameter (one of 
 * {@link prefux.Constants#POLY_TYPE_LINE},
 * {@link prefux.Constants#POLY_TYPE_CURVE}, or
 * {@link prefux.Constants#POLY_TYPE_STACK}) determines how the
 * edges of the polygon are drawn. The LINE type result in a standard polygon,
 * with straight lines drawn between each sequential point. The CURVE type
 * causes the edges of the polygon to be interpolated as a cardinal spline,
 * giving a smooth blob-like appearance to the shape. The STACK type is similar
 * to the curve type except that straight line segments (not curves) are used
 * when the slope of the line between two adjacent points is zero or infinity.
 * This is useful for drawing stacks of data with otherwise curved edges.</p>
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class PolygonRenderer extends AbstractShapeRenderer {

	public static final String POLYGON = "POLYGON";


	@Override
	protected Node getRawShape(VisualItem item, boolean bind) {
		// TODO Auto-generated method stub
		return null;
	}

   
} // end of class PolygonRenderer
