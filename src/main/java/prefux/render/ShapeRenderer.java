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
/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prefux.util.ColorLib;
import prefux.visual.VisualItem;

/**
 * 
 * Renderer for drawing simple shapes.
 * 
 * The shape is a circle and can either be styled by a CSS style class, or
 * colored by a 
 * 
 * @author Martin Stockhammer
 */
public class ShapeRenderer extends AbstractShapeRenderer implements Renderer {

	private static final Logger log = LogManager.getLogger(AbstractShapeRenderer.class);

	public static final double DEFAULT_SIZE = 5.0;
	
	/**
	 * The default CSS style class used for the node.
	 */
	public static final String DEFAULT_STYLE_CLASS = "prefux-shape";

	private double baseSize = DEFAULT_SIZE;
	
	private boolean useItemSize = true;
	private int fillMode = SOLID;
	
	/**
	 * No fill from the item value. You can further use style classes.
	 */
	public static final int NONE = 0;
	/**
	 * Use a custom fill function.
	 */
	public static final int CUSTOM = 1;
	/**
	 * Fill the node with static color
	 */
	public static final int SOLID = 2;
	/**
	 * Fill the node with a gradient
	 */
	public static final int GRADIENT = 3;
	/**
	 * Fill the node with a gradient
	 */
	public static final int GRADIENT_SPHERE = 4;
	
	/*
	 * Standard gradient fill function
	 */
	private FillPainter standardGradientPainter = (int color) -> {
		Color col0 = ColorLib.getColor(color);
		Color col1 = new Color(col0.getRed(), col0
		        .getGreen(), col0.getBlue(), 0.5);
		Color col2 = new Color(col0.getRed(), col0
		        .getGreen(), col0.getBlue(), 0.0);
		RadialGradient grad = new RadialGradient(0,
		        0.0, 0.5, 0.5, 0.5, true,
		        CycleMethod.NO_CYCLE, new Stop(0, col0),
		        new Stop(0.5, col1), new Stop(1.0, col2));
		return grad;
	};
	/*
	 * Gradient fill function with sphere effect
	 */
	private FillPainter sphereGradientPainter = (int color) -> {
		Color col0 = ColorLib.getColor(color);
		Color col1 = Color.WHITE;
		RadialGradient grad = new RadialGradient(215,
		        0.4, 0.45, 0.45, 0.5, true,
		        CycleMethod.NO_CYCLE, new Stop(0, col1),
		        new Stop(1.0, col0));
		return grad;
	};
	/*
	 * Standard block fill function
	 */
	private FillPainter standardColorPainter = (int color) -> {
		return ColorLib.getColor(color);
	};
	
	private FillPainter[] fillPainter = new FillPainter[] {
			null,
			null,
			standardColorPainter,
			standardGradientPainter,
			sphereGradientPainter
	};

	
	@Override
	public String getDefaultStyle() {
		return DEFAULT_STYLE_CLASS;
	}

	@Override
	protected Node getRawShape(VisualItem item, boolean bind) {
		double radius = useItemSize ? item.getSize() * getBaseSize()
		        : getBaseSize();
		final Circle circle = new Circle(radius);
		if (bind) {

			Platform.runLater(() -> {
				circle.centerXProperty().bind(item.xProperty());
				circle.centerYProperty().bind(item.yProperty());
				if (useItemSize) {

					circle.radiusProperty().bind(
					        Bindings.multiply(getBaseSize(),
					                item.sizeProperty()));
				}

				if (!(fillMode==NONE)) {
					final ObjectBinding<Paint> colorBinding = new ObjectBinding<Paint>() {

						{
							bind(item.fillColorProperty());
						}

						@Override
						protected Paint computeValue() {
							return fillPainter[fillMode].fill(item.fillColorProperty().getValue());
						}
					};
					circle.fillProperty().bind(colorBinding);
				}

			});
		}
		return circle;
	}

	/**
	 * If set to true, the size of the node is bound to the size attribute of the visual item.
	 * @param value
	 */
	public void setUseItemSize(boolean value) {
		this.useItemSize = value;
	}

	/**
	 * Returns true, if the size of the node is bound to the size property
	 * of the visual item.
	 * @return
	 */
	public boolean isUseItemSize() {
		return useItemSize;
	}

	/**
	 * Returns the base size of the node.
	 * @return
	 */
	public double getBaseSize() {
		return baseSize;
	}

	/**
	 * Sets the base size of the node.
	 * @param baseSize
	 */
	public void setBaseSize(double baseSize) {
		this.baseSize = baseSize;
	}

	/**
	 * Set the fill mode to:
	 * <dl>
	 * <dt><code>NONE</code></dt>
	 * <dd>Do not use the color attribute of the visual item to fill the node.
	 * <dt><code>COLOR</code></dt>
	 * <dd>Fill the node with the color of the visual item.
	 * <dt><code>GRADIENT</code></dt>
	 * <dd>Use a gradient based on the color of the visual item to fill the node. 
	 * </dl>  
	 * 
	 * @param mode The mode to use.
	 * 
	 * @see #SOLID
	 * @see #GRADIENT
	 * @see #NONE
	 */
	public void setFillMode(int mode) {
		if (mode>fillPainter.length) {
			throw new IllegalArgumentException("Mode must be NONE, COLOR, GRADIENT OR CUSTOM");
		}
		if (mode==CUSTOM) {
			if (fillPainter[CUSTOM]==null) {
				throw new IllegalArgumentException("Custom mode is only set by setFillPainter()");
			}
		} else {
			fillPainter[CUSTOM]=null;
		}
		this.fillMode = mode;
	}
	
	public void setFillPainter(FillPainter painter) {
		this.fillPainter[CUSTOM] = painter;
		setFillMode(CUSTOM);
	}
	

} // end of class ShapeRenderer
