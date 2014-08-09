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

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefux.data.util.Point2D;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;

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
public class EdgeRenderer extends AbstractShapeRenderer implements Renderer {

	private static final Logger log = LoggerFactory
	        .getLogger(EdgeRenderer.class);

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
		if (bind) {
			Platform.runLater(() -> {
				line.startXProperty().bind(edge.getSourceItem().xProperty());
				line.startYProperty().bind(edge.getSourceItem().yProperty());
				line.endXProperty().bind(edge.getTargetItem().xProperty());
				line.endYProperty().bind(edge.getTargetItem().yProperty());
			});
		}
		return line;
	}

	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

} // end of class EdgeRenderer
