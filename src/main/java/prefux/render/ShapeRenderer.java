/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefux.visual.VisualItem;

/**
 * Renderer for drawing simple shapes.
 * 
 * @author Martin Stockhammer
 */
public class ShapeRenderer extends AbstractShapeRenderer implements Renderer {

    private static final Logger log = LoggerFactory
            .getLogger(ShapeRenderer.class);

    public double DEFAULT_RADIUS = 5.0;
    public static final String DEFAULT_STYLE_CLASS = "prefux-shape";

    @Override
    public void setBounds(VisualItem item) {
        log.debug("setBounds " + item);
        log.debug("setBounds " + item.xProperty().get()+"/"+item.yProperty().get());
        log.debug("setBounds " + item.getX()+"/"+item.getY());
        
        // Direct binding

    }

    @Override
    public String getDefaultStyle() {
        return DEFAULT_STYLE_CLASS;
    }

    @Override
    protected Node getRawShape(VisualItem item) {
        Circle circle = new Circle(DEFAULT_RADIUS);
        Platform.runLater(() -> {
            circle.centerXProperty().bind(item.xProperty());
            circle.centerYProperty().bind(item.yProperty());
        });
        return circle;
    }

} // end of class ShapeRenderer
