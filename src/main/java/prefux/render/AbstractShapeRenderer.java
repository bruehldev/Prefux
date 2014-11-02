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

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.transform.Transform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

/**
 * <p>
 * Abstract base class implementation of the Renderer interface for supporting
 * the drawing of basic shapes. Subclasses should override the
 * {@link #getRawShape(VisualItem) getRawShape} method, which returns the shape
 * to draw. Optionally, subclasses can also override the
 * {@link #getTransform(VisualItem) getTransform} method to apply a desired
 * <code>AffineTransform</code> to the shape.
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> The intention of drawShape ist that it is called only once. It
 * registers the JavaFX nodes on the parent nodes. The same for getRawShape().
 * </p>
 * 
 * @version 2.0
 * @author Martin Stockhammer
 * @author alan newberger
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * 
 */
public abstract class AbstractShapeRenderer implements Renderer {

	private static final Logger log = LogManager.getLogger(AbstractShapeRenderer.class);

	public static final int RENDER_TYPE_NONE = 0;
	public static final int RENDER_TYPE_DRAW = 1;
	public static final int RENDER_TYPE_FILL = 2;
	public static final int RENDER_TYPE_DRAW_AND_FILL = 3;

	private int m_renderType = RENDER_TYPE_DRAW_AND_FILL;
	protected Transform m_transform;
	protected boolean m_manageBounds = true;

	private List<String> rendererStyles = new ArrayList<>();
	private List<Transform> transforms = null;

	public AbstractShapeRenderer() {
		if (getDefaultStyle() != null) {
			rendererStyles.add(getDefaultStyle());
		}
	}

	public void setManageBounds(boolean b) {
		m_manageBounds = b;
	}

	/**
	 * @see prefux.render.Renderer#render(java.awt.Graphics2D,
	 *      prefux.visual.VisualItem)
	 */
	public void render(Parent g, VisualItem item, boolean bind) {
		log.debug("Rendering item " + item);
		Node shape = getShape(item, bind);
		if (shape != null)
			drawShape(g, item, shape);
		if (getNode(item) == null) {
			item.setNode(shape);
		}
	}

	public void render(Parent g, VisualItem item) {
		render(g,item,true);
	}

	/**
	 * Draws the specified shape into the provided Graphics context, using
	 * stroke and fill color values from the specified VisualItem. This method
	 * can be called by subclasses in custom rendering routines.
	 */
	protected void drawShape(Parent g, VisualItem item, Node shape) {
		ObservableList<String> styleClazzes = shape.getStyleClass();
		for (String style : rendererStyles) {
			if (style != null && !styleClazzes.contains(style)) {
				styleClazzes.add(style);
			}
		}
		String style = getStyle(item);
		if (getStyle(item) != null && !styleClazzes.contains(style)) {
			styleClazzes.add(style);
		}
		FxGraphicsLib.addToParent(g, shape);
	}

	/**
	 * Returns the style class for the given item.
	 * 
	 * @param item
	 * @return
	 */
	public String getStyle(VisualItem item) {
		return item.getStyle();
	}

	/**
	 * Returns the default style class for the current rendering.
	 * 
	 * @return
	 */
	public String getDefaultStyle() {
		return null;
	}

	/**
	 * Returns the shape describing the boundary of an item. The shape's
	 * coordinates should be in abolute (item-space) coordinates.
	 * 
	 * @param item
	 *            the item for which to get the Shape
	 */
	public Node getShape(VisualItem item, boolean bind) {
		List<Transform> at = getTransform(item);
		Node rawShape = getRawShape(item, bind);
		if (at != null && at.size() > 0) {
			ObservableList<Transform> ts = rawShape.getTransforms();
			for (Transform transform : at) {
				ts.add(transform);
			}
		}
		return rawShape;
	}

	/**
	 * Return a non-transformed shape for the visual representation of the item.
	 * Subclasses must implement this method.
	 * 
	 * @param item
	 *            the VisualItem being drawn
	 * @return the "raw", untransformed shape.
	 */
	protected abstract Node getRawShape(VisualItem item, boolean bind);
	
	

	/**
	 * Return the graphics space transform applied to this item's shape, if any.
	 * Subclasses can implement this method, otherwise it will return null to
	 * indicate no transformation is needed.
	 * 
	 * @param item
	 *            the VisualItem
	 * @return the graphics space transform, or null if none
	 */
	protected List<Transform> getTransform(VisualItem item) {
		return transforms;
	}

	protected void addTransform(Transform transform) {
		if (this.transforms == null) {
			this.transforms = new ArrayList<>(2);
		}
		this.transforms.add(transform);
	}

	/**
	 * Returns a value indicating if a shape is drawn by its outline, by a fill,
	 * or both. The default is to draw both.
	 * 
	 * @return the rendering type
	 */
	public int getRenderType(VisualItem item) {
		return m_renderType;
	}

	/**
	 * Sets a value indicating if a shape is drawn by its outline, by a fill, or
	 * both. The default is to draw both.
	 * 
	 * @param type
	 *            the new rendering type. Should be one of
	 *            {@link #RENDER_TYPE_NONE}, {@link #RENDER_TYPE_DRAW},
	 *            {@link #RENDER_TYPE_FILL}, or
	 *            {@link #RENDER_TYPE_DRAW_AND_FILL}.
	 */
	public void setRenderType(int type) {
		if (type < RENDER_TYPE_NONE || type > RENDER_TYPE_DRAW_AND_FILL) {
			throw new IllegalArgumentException("Unrecognized render type.");
		}
		m_renderType = type;
	}

	/**
	 * @see prefux.render.Renderer#locatePoint(prefux.data.util.Point2D,
	 *      prefux.visual.VisualItem)
	 */
	public boolean locatePoint(Point2D p, VisualItem item) {
		if (item.getBounds().contains(p)) {
			// if within bounds, check within shape outline
			Node s = getNode(item);
			return (s != null ? s.contains(p) : false);
		} else {
			return false;
		}
	}

	protected Node getNode(VisualItem item) {
		return item.getNode();
	}



	public void addStyle(String style) {
		if (!rendererStyles.contains(style))
			rendererStyles.add(style);
	}

} // end of abstract class AbstractShapeRenderer
