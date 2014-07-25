/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import prefux.util.ColorLib;
import prefux.visual.VisualItem;

/**
 * Renderer for drawing simple shapes.
 * 
 * @author Martin Stockhammer
 */
public class ShapeRenderer extends AbstractShapeRenderer implements Renderer {

	private static final Logger log = LoggerFactory
	        .getLogger(ShapeRenderer.class);

	public static final double DEFAULT_SIZE = 5.0;
	public static final String DEFAULT_STYLE_CLASS = "prefux-shape";

	private double baseSize = DEFAULT_SIZE;
	private boolean useItemSize = true;
	private boolean useItemColor = true;

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
					circle.radiusProperty().bind(item.sizeProperty());
				}

				if (useItemColor) {
					final ObjectBinding<Paint> colorBinding = new ObjectBinding<Paint>() {
						{
							bind(item.fillColorProperty());
						}

						@Override
						protected Paint computeValue() {
							
							Color col = ColorLib.getColor(item.fillColorProperty()
							        .getValue());
							log.debug("COLOR: "+col+" / "+item.fillColorProperty());
							return col;
						}
					};
					circle.fillProperty().bind(colorBinding);
				}

			});
		}
		return circle;
	}

	public void setUseItemSize(boolean value) {
		this.useItemSize = value;
	}

	public boolean isUseItemSize() {
		return useItemSize;
	}

	public double getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(double baseSize) {
		this.baseSize = baseSize;
	}

	public boolean isUseItemColor() {
		return useItemColor;
	}

	public void setUseItemColor(boolean useItemColor) {
		this.useItemColor = useItemColor;
	}

} // end of class ShapeRenderer
