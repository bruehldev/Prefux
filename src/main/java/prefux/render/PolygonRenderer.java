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
