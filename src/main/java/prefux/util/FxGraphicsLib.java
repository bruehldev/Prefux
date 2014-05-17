package prefux.util;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import prefux.render.AbstractShapeRenderer;
import prefux.visual.VisualItem;

public class FxGraphicsLib {
	
	public static final String BORDER_CLASS = "itemBorder";

	public static void paint(Parent g, VisualItem item, Node shape,
			String style, int renderType) {
		HBox myShape = new HBox();
		myShape.getChildren().add(shape);
		if (renderType == AbstractShapeRenderer.RENDER_TYPE_DRAW ||
				renderType == AbstractShapeRenderer.RENDER_TYPE_DRAW_AND_FILL) {
			myShape.getStyleClass().add(BORDER_CLASS);
		}
		
		addToParent(g,shape);
		shape.getStyleClass().add(style);
		if (renderType == AbstractShapeRenderer.RENDER_TYPE_DRAW_AND_FILL ||
				renderType == AbstractShapeRenderer.RENDER_TYPE_FILL)
			shape.setVisible(true);
	}
	
	public static void addToParent(Parent g, Node child) {
		if (g instanceof Pane) {
			Pane p = (Pane) g;
			if (!find(p,child)) {
				p.getChildren().add(child);
			}
		} else if (g instanceof Group) {
			Group gr = (Group) g;
			if (!find(gr,child)) {
				gr.getChildren().add(child);
			}
		}
		
	}

	
	public static boolean find(Parent p, Node node) {
		return p.lookupAll("#"+node.getId()).stream().anyMatch(n -> 
			n.equals(node)
		);
	}

	public static int intersectLineRectangle(Point2D a1, Point2D a2,
			Rectangle2D r, Point2D[] pts) {
		double a1x = a1.getX(), a1y = a1.getY();
		double a2x = a2.getX(), a2y = a2.getY();
		double mxx = r.getMaxX(), mxy = r.getMaxY();
		double mnx = r.getMinX(), mny = r.getMinY();

		if (pts[0] == null)
			pts[0] = Point2D.ZERO;
		if (pts[1] == null)
			pts[1] = Point2D.ZERO;

		int i = 0;
		if (intersectLineLine(mnx, mny, mxx, mny, a1x, a1y, a2x, a2y, pts[i]) > 0)
			i++;
		if (intersectLineLine(mxx, mny, mxx, mxy, a1x, a1y, a2x, a2y, pts[i]) > 0)
			i++;
		if (i == 2)
			return i;
		if (intersectLineLine(mxx, mxy, mnx, mxy, a1x, a1y, a2x, a2y, pts[i]) > 0)
			i++;
		if (i == 2)
			return i;
		if (intersectLineLine(mnx, mxy, mnx, mny, a1x, a1y, a2x, a2y, pts[i]) > 0)
			i++;
		return i;
	}

	public static int intersectLineLine(double a1x, double a1y, double a2x,
			double a2y, double b1x, double b1y, double b2x, double b2y,
			Point2D intersect) {
		double ua_t = (b2x - b1x) * (a1y - b1y) - (b2y - b1y) * (a1x - b1x);
		double ub_t = (a2x - a1x) * (a1y - b1y) - (a2y - a1y) * (a1x - b1x);
		double u_b = (b2y - b1y) * (a2x - a1x) - (b2x - b1x) * (a2y - a1y);

		if (u_b != 0) {
			double ua = ua_t / u_b;
			double ub = ub_t / u_b;

			if (0 <= ua && ua <= 1 && 0 <= ub && ub <= 1) {
				intersect = new Point2D(a1x + ua * (a2x - a1x), a1y + ua
						* (a2y - a1y));
				return 1;
			} else {
				return GraphicsLib.NO_INTERSECTION;
			}
		} else {
			return (ua_t == 0 || ub_t == 0 ? GraphicsLib.COINCIDENT
					: GraphicsLib.PARALLEL);
		}
	}
	
	public static final double getCenterX(Rectangle2D rect) {
		return rect.getMinX()+rect.getWidth()/2;
	}

	public static final double getCenterY(Rectangle2D rect) {
		return rect.getMinY()+rect.getHeight()/2;
	}
	
	public static final void setBounds(VisualItem item, Shape shape) {
		Bounds bounds = shape.getBoundsInLocal();
		item.setBounds(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}

}
