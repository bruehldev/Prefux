/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */
package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prefux.data.util.Point2D;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;

/**
 * <p>
 * Renderer that draws edges as lines connecting nodes. Both straight and curved
 * lines are supported. Curved lines are drawn using cubic Bezier curves.
 * Subclasses can override the
 * method to provide custom control point assignment for such curves.
 * </p>
 *
 * <p>
 * This class also supports arrows for directed edges. See the
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class EdgeRenderer extends AbstractShapeRenderer implements Renderer {

	public static List<Polygon> arrowHeads = new ArrayList<>();
	public static List<EdgeItem> edgeList = new ArrayList<>();
	public static List<Polygon> arrowHeadList = new ArrayList<>();

	public static class Arrow extends Polygon {

		public double rotate;
		public float t;
		Line line;
		Rotate rz;

		public Arrow( Line line) {
			super();
			this.line = line;
			this.t = 1;
			init();
		}

		public Arrow( Line line, double... arg0) {
			super(arg0);
			this.line = line;
			this.t = 1;
			//init();
		}

		private void init() {
			update(0,0);
		}

		public void update(final double x,final double y) {
			setTranslateX(x);
			setTranslateY(y);
		}

	}

	private static final Logger log = LogManager.getLogger(EdgeRenderer.class);

	public static final String DEFAULT_STYLE_CLASS = "prefux-edge";

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		log.debug("locatePoint " + p + " " + item);
		return false;
	}

	@Override
	protected Node getRawShape(VisualItem item, boolean bind) {

		//System.out.println(item);
		EdgeItem edge = (EdgeItem) item;
		Line line = new Line();
		//ArrowHead head = new ArrowHead();
		//line.setStrokeWidth(2);

		// Arrow generate
		double edgeX = edge.getSourceItem().xProperty().get();
		double edgeY = edge.getSourceItem().yProperty().get();

		//Arrow arrow = new Arrow( line, arrowShape);
		//arrows.add(arrow);
		//PolygonRenderer;
		//double[] arrowShape = new double[] {edgeX ,edgeY,10+edgeX,20+edgeY,edgeX-10,edgeY+20};
		// 1. Left bottom / 2. top right middle / 3. left top
		double[] arrowShape = new double[] {-10, -10, 10, -5, -10, 0};
		//double circleRadius = 0;
	//	double[] arrowShape = new double[] {edge.getTargetItem().getX()-10, edge.getTargetItem().getY()-10, edge.getTargetItem().getX(),
	//			edge.getTargetItem().getY(),edge.getTargetItem().getX()-10,edge.getTargetItem().getY()};
		Polygon arrowHead = new Polygon(arrowShape);
		Polygon triangle = new Polygon();
		System.out.println(edge.getSourceItem().get("name").toString() + " and " + edge.getTargetItem().get("name").toString());
		if(!edge.getSourceItem().get("name").toString().matches(edge.getTargetItem().get("name").toString())) {
			//arrowHeads.add(arrowHead);
			edgeList.add(edge);
			arrowHeadList.add(triangle);

		}
		Rotate rotationTransform = new Rotate();

		if (bind) {
			Platform.runLater(() -> {

				try
				{Thread.sleep(0);}
				catch (Exception e)
				{e.printStackTrace();}


					line.startXProperty().bind(edge.getSourceItem().xProperty());
					line.startYProperty().bind(edge.getSourceItem().yProperty());
					line.endXProperty().bind(edge.getTargetItem().xProperty());
					line.endYProperty().bind(edge.getTargetItem().yProperty());

				if(!edge.getSourceItem().get("name").toString().matches(edge.getTargetItem().get("name").toString())) {
					createArrowHead(edge, triangle);



					// Start calculate angle
					//double pitch = Math.sqrt(Math.pow((edge.getTargetItem().getX() - edge.getSourceItem().getX())
					//		/ (edge.getTargetItem().getY() - edge.getSourceItem().getY()), 2));
					//double angle =Math.atan(pitch);

					//System.out.println("Angle for " + ((EdgeItem) item).getTargetItem().get("name").toString() + " is " + angle);

					//Double currentx = edge.getTargetItem().getX() - edge.getSourceItem().getX() - (0.5*arrowHead.getBoundsInLocal().getMinX());
					//Double currenty = edge.getTargetItem().getY() - edge.getSourceItem().getY() - (0.5*arrowHead.getBoundsInLocal().getMinY());
					//Double currentAngle = Math.toDegrees(Math.atan2(currenty,currentx));

					//Rotate arrowHeadRotation = new Rotate(currentAngle, edge.getTargetItem().getX(),edge.getTargetItem().getY());
					//arrowHeadRotation.pivotXProperty().bind(edge.getSourceItem().xProperty());
					//arrowHeadRotation.pivotYProperty().bind(edge.getSourceItem().yProperty());
					//arrowHeadRotation.setAxis(Rotate.Z_AXIS);
					//edge.getAdjacentItem((NodeItem) arrowHead);

					//((NodeItem) arrowHead).setNode(edge.getNode());
					//arrowHead.getTransforms().add(arrowHeadRotation);
				}


					//arrowHead.translateXProperty().bind(edge.getTargetItem().xProperty());
					//arrowHead.translateYProperty().bind(edge.getTargetItem().yProperty());
				//arrowHead.rotationAxisProperty().bind(edge.getNode().rotationAxisProperty());
				//arrowHead.rotateProperty().bind(line.rotateProperty());
/*
					double pitch = Math.sqrt(Math.pow(edge.getTargetItem().getX() - edge.getSourceItem().getX(), 2)
							/ Math.pow(edge.getTargetItem().getY() - edge.getSourceItem().getY(), 2));
					double angle = Math.tan(pitch);
					arrowHead.getTransforms().add(new Rotate(angle, edge.getTargetItem().getX(), edge.getTargetItem().getY()));
					*/

			});
		}


		return line;
	}

	public static void calcArrowHeadsAngle() {


		int accordingEdge = 0;
		for (Polygon arrowHead:arrowHeads) {
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);
			Double currentx = edge.getTargetItem().getX() - edge.getSourceItem().getX() - (0.5*arrowHead.getBoundsInLocal().getMaxX());
			Double currenty = edge.getTargetItem().getY() - edge.getSourceItem().getY() - (0.5*arrowHead.getBoundsInLocal().getMaxY());
			Double currentAngle = Math.toDegrees(Math.atan2(currenty,currentx));
			//System.out.println(edge.getTargetItem().getShape());
			//System.out.println(currentAngle);

			arrowHead.setRotate(currentAngle);
			accordingEdge++;
		}
	}

	public static Polygon createArrowHead(EdgeItem edge, Polygon currentTriangle) {
		// Example shape: {-10, -10, 10, -5, -10, 0}

		double circleRadius = edge.getSourceItem().getNode().getBaselineOffset()/2;
		// Calc rise of edge alpha
		double alpha = Math.toDegrees(Math.atan2(edge.getTargetItem().getY() - edge.getSourceItem().getY(),
				edge.getTargetItem().getX() - edge.getSourceItem().getX()));
		// Calc triangle in circle
		double a = Math.cos(alpha)*circleRadius;
		double b = Math.sin(alpha)*circleRadius;

		// Calc cut point of circle and edge
		double sX = edge.getTargetItem().getX()-a;
		double sY = edge.getTargetItem().getY()+b;
		//System.out.println("Taget node "+ edge.getTargetItem().getX() + "/" + edge.getTargetItem().getY());
		//System.out.println("Spitze "+ sX + "/" + sY);
		// Function to calc axis of bottom arrowhead points
		double arrowMiddleLength = 10;
		// Point of arrowheadlength in line
		double a1 = Math.cos(alpha)*arrowMiddleLength;
		double b1 = Math.sin(alpha)*arrowMiddleLength;
		double mX = sX-a1;
		double mY = sY+b1;
		// point to right corner
		double wechselwinkel = 90-alpha;
		double lengtOfBottomLine = 10;
		double a2 = Math.cos(wechselwinkel)*lengtOfBottomLine/2;
		double b2 = Math.sin(wechselwinkel)*lengtOfBottomLine/2;
		double rX = mX+a2;
		double rY = mY+b2;

		// Calc left corner point
		double lX = mX-a2;
		double lY = mY-b2;

		// Point of bottem left corner
		double x1 = lX;
		double y1 = lY;
		// Point of the head
		double x2 = sX;
		double y2 = sY;
		// Point of bottem right corner
		double x3 = rX;
		double y3 = rY;
		currentTriangle.getPoints().setAll(
				x1, y1,
				x2, y2,
				x3, y3
		);
		currentTriangle.setStroke(Color.FORESTGREEN);
		currentTriangle.setStrokeWidth(4);
		currentTriangle.setStrokeLineCap(StrokeLineCap.ROUND);
		currentTriangle.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));
		return currentTriangle;

	}

	public static void syncArrowHead() {

	}

	public static void calcArrowHeadsAngleNew() {
		int accordingEdge = 0;
		for (Polygon arrowHead:arrowHeadList) {
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);

			double circleRadius = edge.getSourceItem().getNode().getBaselineOffset()/2;
			// Calc rise of edge alpha
			double alpha = Math.toDegrees(Math.atan2(edge.getTargetItem().getY() - edge.getSourceItem().getY(),
					edge.getTargetItem().getX() - edge.getSourceItem().getX()));
			// Calc triangle in circle
			double a = Math.cos(alpha)*circleRadius;
			double b = Math.sin(alpha)*circleRadius;

			// Calc cut point of circle and edge
			double sX = edge.getTargetItem().getX()-a;
			double sY = edge.getTargetItem().getY()+b;
			// Function to calc axis of bottom arrowhead points
			double arrowMiddleLength = 10;
			// Point of arrowheadlength in line
			double a1 = Math.cos(alpha)*arrowMiddleLength;
			double b1 = Math.sin(alpha)*arrowMiddleLength;
			double mX = sX-a1;
			double mY = sY+b1;
			// point to right corner
			double wechselwinkel = 90-alpha;
			double lengtOfBottomLine = 10;
			double a2 = Math.cos(wechselwinkel)*lengtOfBottomLine/2;
			double b2 = Math.sin(wechselwinkel)*lengtOfBottomLine/2;
			double rX = mX+a2;
			double rY = mY+b2;

			// Calc left corner point
			double lX = mX-a2;
			double lY = mY-b2;

			// Point of bottem left corner
			double x1 = lX;
			double y1 = lY;
			// Point of the head
			double x2 = sX;
			double y2 = sY;
			// Point of bottem right corner
			double x3 = rX;
			double y3 = rY;
			arrowHead.getPoints().setAll(
					x1, y1,
					x2, y2,
					x3, y3
			);
			accordingEdge++;
		}
	}

	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

} // end of class EdgeRenderer
