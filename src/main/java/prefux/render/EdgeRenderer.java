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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
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

	public static List<Arrow> arrows = new ArrayList<Arrow>();

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
		line.setStrokeWidth(2);

		// Arrow generate
		double edgeX = edge.getSourceItem().xProperty().get();
		double edgeY = edge.getSourceItem().yProperty().get();

		double[] arrowShape = new double[] {edgeX ,edgeY,10+edgeX,20+edgeY,edgeX-10,edgeY+20};
		Arrow arrow = new Arrow( line, arrowShape);
		arrows.add(arrow);
		//PolygonRenderer;


		if (bind) {
			Platform.runLater(() -> {
				line.startXProperty().bind(edge.getSourceItem().xProperty());
				line.startYProperty().bind(edge.getSourceItem().yProperty());
				line.endXProperty().bind(edge.getTargetItem().xProperty());
				line.endYProperty().bind(edge.getTargetItem().yProperty());

				// Arrow binds
				System.out.println("edgeY:" + edgeY);

				arrow.translateXProperty().bind(edge.getTargetItem().xProperty());
				arrow.translateYProperty().bind(edge.getTargetItem().yProperty());
				arrow.layoutXProperty().bind(edge.getTargetItem().xProperty());
				arrow.layoutYProperty().bind(edge.getTargetItem().yProperty());
				arrow.rotateProperty().bind(line.rotateProperty());
				//arrow.property.bind(edge.getTargetItem().xProperty());
			});
		}

		return line;
	}

	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

} // end of class EdgeRenderer
