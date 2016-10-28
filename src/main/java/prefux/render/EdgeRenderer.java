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


	public static List<EdgeItem> edgeList = new ArrayList<>();
	public static List<Polygon> arrowHeadList = new ArrayList<>();

	private static final Logger log = LogManager.getLogger(EdgeRenderer.class);

	public static final String DEFAULT_STYLE_CLASS = "prefux-edge";

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		log.debug("locatePoint " + p + " " + item);
		return false;
	}

	@Override
	protected Node getRawShape(VisualItem item, boolean bind) {

		EdgeItem edge = (EdgeItem) item;
		Line line = new Line();
		double[] arrowShape = new double[] {-10, -5, 10, 0, -10, 5};
		Polygon triangle = new Polygon(arrowShape);
		if(!edge.getSourceItem().get("name").toString().matches(edge.getTargetItem().get("name").toString())) {
			edgeList.add(edge);
			arrowHeadList.add(triangle);
		}

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



				//triangle.translateXProperty().bind(edge.getTargetItem().xProperty());
				//triangle.translateYProperty().bind(edge.getTargetItem().yProperty());

				if(!edge.getSourceItem().get("name").toString().matches(edge.getTargetItem().get("name").toString())) {
					//createArrowHead(edge, triangle);
				}

			});
		}
		return line;
	}


	public static void calcArrowHeadsAngle() {
		try
		{Thread.sleep(0);}
		catch (Exception e)
		{e.printStackTrace();}
		int accordingEdge = 0;
		for (Polygon arrowHead:arrowHeadList) {

			try
			{Thread.sleep(0);}
			catch (Exception e)
			{e.printStackTrace();}
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);

			javafx.geometry.Point2D targetPoint = new javafx.geometry.Point2D(edge.getTargetItem().xProperty().getValue(),edge.getTargetItem().yProperty().getValue());
			javafx.geometry.Point2D sourcePoint = new javafx.geometry.Point2D(edge.getSourceItem().xProperty().getValue(),edge.getSourceItem().yProperty().getValue());
			javafx.geometry.Point2D middlePoint = targetPoint.midpoint(sourcePoint);
			javafx.geometry.Point2D quarterPoint = targetPoint.midpoint(middlePoint);

			Double currentx = edge.getTargetItem().getX() - edge.getSourceItem().getX() - (0.5*arrowHead.getBoundsInLocal().getMaxX());
			Double currenty = edge.getTargetItem().getY() - edge.getSourceItem().getY() - (0.5*arrowHead.getBoundsInLocal().getMaxY());
			Double currentAngle = Math.toDegrees(Math.atan2(currenty,currentx));
			//System.out.println(edge.getTargetItem().getShape());
			//System.out.println(currentAngle);
			arrowHead.setTranslateX(quarterPoint.getX());
			arrowHead.setTranslateY(quarterPoint.getY());
			arrowHead.setRotate(currentAngle);
			accordingEdge++;
		}
	}

	public static void createArrowHead() {

		try
		{Thread.sleep(0);}
		catch (Exception e)
		{e.printStackTrace();}
		int accordingEdge = 0;
		// Example shape: {-10, -10, 10, -5, -10, 0}
		for (Polygon arrowHead:arrowHeadList) {
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);
			//double circleRadius = edge.getSourceItem().getNode().getBaselineOffset()/2;
			double circleRadius = 5;
			// Calc rise of edge alpha
			System.out.println(edge.getTargetItem().getY() + "==" + edge.getTargetItem().yProperty().getValue());
			System.out.println(edge.getTargetItem().getX() + "==" + edge.getTargetItem().xProperty().getValue());
			double alpha = (Math.toDegrees(Math.tan(-edge.getTargetItem().getY() + edge.getSourceItem().getY() /
					edge.getTargetItem().getX() - edge.getSourceItem().getX()))) % 360;
			double alphaForwechselwinkel = Math.tan(-edge.getTargetItem().getY() + edge.getSourceItem().getY() /
					edge.getTargetItem().getX() - edge.getSourceItem().getX());
			System.out.println("alpha" + alpha);
			// Calc triangle in circle
			double a = Math.cos(alpha) * circleRadius;
			double b = Math.sin(alpha) * circleRadius;

			// Calc cut point of circle and edge
			//double sX = edge.getTargetItem().getX()-a;
			//double sY = edge.getTargetItem().getY()+b;
			double sX = edge.getTargetItem().getX();
			double sY = edge.getTargetItem().getY();
			System.out.println("Taget node " + edge.getTargetItem().getX() + "/" + edge.getTargetItem().getY());
			System.out.println("Spitze " + sX + "/" + sY);
			// Function to calc axis of bottom arrowhead points
			double arrowMiddleLength = 10;
			// Point of arrowheadlength in line
			double a1 = Math.cos(alpha) * arrowMiddleLength;
			double b1 = Math.sin(alpha) * arrowMiddleLength;
			double mX = sX - a1;
			double mY = sY + b1;
			System.out.println("mittelpunkt" + mX + "/" + mY);
			// point to right corner
			double wechselwinkel = (Math.toDegrees(-1 / alphaForwechselwinkel)) % 360;
			System.out.println("wechselwinkel" + wechselwinkel);
			double lengtOfBottomLine = 10;
			double a2 = Math.cos(wechselwinkel) * lengtOfBottomLine / 2;
			double b2 = Math.sin(wechselwinkel) * lengtOfBottomLine / 2;
			double rX = mX + a2;
			double rY = mY + b2;
			System.out.println("Right point" + rX + "/" + rY);
			// Calc left corner point
			double lX = mX - a2;
			double lY = mY - b2;
			System.out.println("Left point" + lX + "/" + lY);

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
			arrowHead.setStroke(Color.BLACK);
			//currentTriangle.setStrokeWidth(4);
			//currentTriangle.setStrokeLineCap(StrokeLineCap.ROUND);
			arrowHead.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));
		}
		//return currentTriangle;

	}

	public static void calcArrowHeadsAngleNew() {
		try
		{Thread.sleep(0);}
		catch (Exception e)
		{e.printStackTrace();}
		int accordingEdge = 0;
		for (Polygon arrowHead:arrowHeadList) {
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);
			double deltaX = -edge.getTargetItem().getX()+edge.getSourceItem().getX();
			double deltaY = -edge.getTargetItem().getY()+edge.getSourceItem().getY();
			double length = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
			javafx.geometry.Point2D steigungsVektor = new javafx.geometry.Point2D(-edge.getTargetItem().getX()+edge.getSourceItem().getX(),
					-edge.getTargetItem().getY()+edge.getSourceItem().getY());
			javafx.geometry.Point2D middlePointOfArrowHead = new javafx.geometry.Point2D(steigungsVektor.normalize().getX()*10+edge.getTargetItem().getX(),
					steigungsVektor.normalize().getY()*10+edge.getTargetItem().getY());
			javafx.geometry.Point2D orthogonalPointOfArrowHead = new javafx.geometry.Point2D(1,-(deltaX/deltaY));

			javafx.geometry.Point2D stuetzRightPointOfArrowHead = new javafx.geometry.Point2D(orthogonalPointOfArrowHead.getX()+middlePointOfArrowHead.getX(),
					orthogonalPointOfArrowHead.getY()+middlePointOfArrowHead.getY());
			javafx.geometry.Point2D stuetzLeftPointOfArrowHead = new javafx.geometry.Point2D(orthogonalPointOfArrowHead.getX()+middlePointOfArrowHead.getX(),
					-orthogonalPointOfArrowHead.getY()+middlePointOfArrowHead.getY());

			arrowHead.getPoints().setAll(
					edge.getTargetItem().getX(), edge.getTargetItem().getY(),
					(stuetzRightPointOfArrowHead.normalize().getX()*10+middlePointOfArrowHead.getX()),
					(stuetzRightPointOfArrowHead.normalize().getY()*10+middlePointOfArrowHead.getY()),
					//orthogonalPointOfArrowHead.getX(), orthogonalPointOfArrowHead.getY()
					(stuetzLeftPointOfArrowHead.normalize().getX()*10+middlePointOfArrowHead.getX()),
					(stuetzLeftPointOfArrowHead.normalize().getY()*10+middlePointOfArrowHead.getY())
					//stuetzLeftPointOfArrowHead.getX(), stuetzLeftPointOfArrowHead.getY()
			);
			System.out.println("Distance of Nodes"+length);
			System.out.println("Distance of Target to middleP"+middlePointOfArrowHead.distance(edge.getTargetItem().getX(),edge.getTargetItem().getY()));
			System.out.println("Distance of middleP to Orth"+middlePointOfArrowHead.distance(orthogonalPointOfArrowHead.getX(),orthogonalPointOfArrowHead.getY()));
			System.out.println("Distance of middleP StuezR"+middlePointOfArrowHead.distance(stuetzRightPointOfArrowHead.getX(),stuetzRightPointOfArrowHead.getY()));
			System.out.println("Distance of middleP StuezL"+middlePointOfArrowHead.distance(stuetzLeftPointOfArrowHead.getX(),stuetzLeftPointOfArrowHead.getY()));
			System.out.println("Distance of middleP StuezRN"+middlePointOfArrowHead.distance((stuetzRightPointOfArrowHead.normalize().getX()*10+middlePointOfArrowHead.getX())
					,(stuetzRightPointOfArrowHead.normalize().getY()*10+middlePointOfArrowHead.getY())));
			System.out.println("Distance of middleP StuezLN"+middlePointOfArrowHead.distance(	(stuetzLeftPointOfArrowHead.normalize().getX()*10+middlePointOfArrowHead.getX()),
					(stuetzLeftPointOfArrowHead.normalize().getY()*10+middlePointOfArrowHead.getY())));

			accordingEdge++;
		}
	}
	/*public static void calcArrowHeadsAngleNew() {

		try
		{Thread.sleep(0);}
		catch (Exception e)
		{e.printStackTrace();}
		int accordingEdge = 0;
		for (Polygon arrowHead:arrowHeadList) {
			EdgeItem edge = (EdgeItem) edgeList.get(accordingEdge);
			try
			{Thread.sleep(0);}
			catch (Exception e)
			{e.printStackTrace();}
			// Example shape: {-10, -10, 10, -5, -10, 0}
			double circleRadius = 5;
			// Calc rise of edge alpha
			double alpha = (Math.toDegrees(Math.tan(edge.getTargetItem().getY() - edge.getSourceItem().getY()/
					edge.getTargetItem().getX() - edge.getSourceItem().getX())))%360;
			double alphaForwechselwinkel = Math.tan(edge.getTargetItem().getY() - edge.getSourceItem().getY()/
					edge.getTargetItem().getX() - edge.getSourceItem().getX());
			System.out.println("alpha"+alpha);
			// Calc triangle in circle
			double a = Math.cos(alpha)*circleRadius;
			double b = Math.sin(alpha)*circleRadius;

			// Calc cut point of circle and edge
		//	double sX = edge.getTargetItem().getX()-a;
		//	double sY = edge.getTargetItem().getY()+b;
			double sX = edge.getTargetItem().getX();
			double sY = edge.getTargetItem().getY();

			System.out.println("Taget node "+ edge.getTargetItem().getX() + "/" + edge.getTargetItem().getY());
			System.out.println("Spitze "+ sX + "/" + sY);
			// Function to calc axis of bottom arrowhead points
			double arrowMiddleLength = 10;
			// Point of arrowheadlength in line
			double a1 = Math.cos(alpha)*arrowMiddleLength;
			double b1 = Math.sin(alpha)*arrowMiddleLength;
			//double mX = sX-a1;
			//double mY = sY+b1;
			double mX = sX-10;
			double mY = sY+10;
			System.out.println("mittelpunkt" + mX + "/" + mY);
			// point to right corner
			double wechselwinkel = (Math.toDegrees(-1/alphaForwechselwinkel))%360;
			System.out.println("wechselwinkel"+wechselwinkel);
			double lengtOfBottomLine = 10;
			double a2 = Math.cos(wechselwinkel)*lengtOfBottomLine/2;
			double b2 = Math.sin(wechselwinkel)*lengtOfBottomLine/2;
			double rX = mX+a2;
			double rY = mY+b2;
			System.out.println("Right point"+rX +"/" + rY);
			// Calc left corner point
			double lX = mX-a2;
			double lY = mY-b2;
			System.out.println("Left point"+lX +"/" + lY);

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
					x2, y2,
					x3, y3,
					x1, y1,
					mX,mY

			);

			accordingEdge++;
		}
	}*/

	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

} // end of class EdgeRenderer
