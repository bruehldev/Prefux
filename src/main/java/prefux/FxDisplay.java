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
package prefux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Scale;

import com.sun.javafx.css.StyleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prefux.controls.Control;
import prefux.data.expression.AndPredicate;
import prefux.data.expression.BooleanLiteral;
import prefux.data.expression.Predicate;
import prefux.data.util.Point2D;
import prefux.data.util.Rectangle2D;
import prefux.render.EdgeRenderer;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;
import prefux.visual.expression.VisiblePredicate;

public class FxDisplay extends Group implements Display, EventHandler<Event> {

	private static final Logger log = LogManager.getLogger(FxDisplay.class);

    public static final String DEFAULT_STYLESHEET = "prefux/prefux.css";

    protected AndPredicate m_predicate = new AndPredicate();

    private int m_itemCount = 0;

    private Visualization vis;

    private List<Control> m_controls = new ArrayList<>();

    private Map<Node, VisualItem> m_registeredNodes = new HashMap<>();

    private Scale zoomScale = new Scale();
    private DoubleProperty zoomFactor = new SimpleDoubleProperty(1.0);
    private DoubleProperty zoomPivotX = new SimpleDoubleProperty(0.0);
    private DoubleProperty zoomPivotY = new SimpleDoubleProperty(0.0);

    public FxDisplay(Visualization vis) {
        setVisualization(vis);
        setPredicate(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see prefux.Display#getPredicate()
     */
    @Override
    public Predicate getPredicate() {
        if (m_predicate.size() == 1) {
            return BooleanLiteral.TRUE;
        } else {
            return m_predicate.get(0);
        }
    }

    /**
     * Sets the filtering Predicate used to control what items are drawn by this
     * Display.
     * 
     * @param p
     *            the filtering {@link prefux.data.expression.Predicate} to use
     */
    public synchronized void setPredicate(Predicate p) {
        if (p == null) {
            m_predicate.set(VisiblePredicate.TRUE);
        } else {
            m_predicate.set(new Predicate[] { p, VisiblePredicate.TRUE });
        }
    }

    @Override
    public void damageReport(Rectangle2D region) {

    }

    @Override
    public void damageReport() {
        log.debug("damageReport");
    }

    @Override
    public double getDisplayX() {
        return getLayoutX();
    }

    @Override
    public double getDisplayY() {
        return getLayoutY();
    }

    @Override
    public double getScale() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getVisibleItemCount() {
        return m_itemCount;
    }

    @Override
    public Visualization getVisualization() {
        return vis;
    }

    public void setVisualization(Visualization vis) {
        log.debug("setVisualization");
        initializeZoom();
        StyleManager.getInstance().addUserAgentStylesheet(DEFAULT_STYLESHEET);
        vis.addDisplay(this);
        this.vis = vis;
        LinkedList<VisualItem> nodes = new LinkedList<>();
        Iterator<VisualItem> it = vis.items();
        // We render nodes after edges for better stacking
        while (it.hasNext()) {
            VisualItem item = it.next();
            if (item instanceof EdgeItem) {
                item.getRenderer().render(this, item);
                item.getNode().addEventHandler(Event.ANY, this);
                m_registeredNodes.put(item.getNode(), item);
                m_itemCount++;
            } else {
                nodes.offer(item);
            }
        }
        // Rendering edges after the nodes
        for (VisualItem item : nodes) {
            item.getRenderer().render(this, item);
            item.getNode().addEventHandler(Event.ANY, this);
            m_registeredNodes.put(item.getNode(), item);
            m_itemCount++;
        }
    }
    
    private void initializeZoom() {
        this.getTransforms().add(zoomScale);
        zoomScale.xProperty().bind(zoomFactor);
        zoomScale.yProperty().bind(zoomFactor);
        zoomPivotX.set(this.getBoundsInLocal().getWidth()/2.0);
        zoomPivotX.set(this.getBoundsInLocal().getHeight()/2.0);
        zoomScale.pivotXProperty().bind(zoomPivotX);
        zoomScale.pivotYProperty().bind(zoomPivotY);
    }

    @Override
    public void repaint() {
        log.debug("repaint");

    }

    @Override
    public double getWidth() {
        return super.getLayoutBounds().getWidth();
    }

    @Override
    public double getHeight() {
        return super.getLayoutBounds().getHeight();
    }

    @Override
    public Point2D getAbsoluteCoordinate(Point2D m_anchor) {
        // TODO Transformations?
    	return null;
    }

    @Override
    public void zoomAbs(Point2D p, double zoom) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zoom(Point2D p, double zoom) {
    	zoomFactor.set(zoom);
    	zoomPivotX.set(p.getX());
    	zoomPivotY.set(p.getY());
    }
    
    public DoubleProperty zoomFactorProperty() {
    	return zoomFactor;
    }

    
    public DoubleProperty zoomPivotXProperty() {
    	return zoomPivotX;
    }

    public DoubleProperty zoomPivotYProperty() {
    	return zoomPivotY;
    }



    @Override
    public void panToAbs(Point2D center) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addControlListener(Control cl) {
        if (!m_controls.contains(cl))
            m_controls.add(cl);

    }

    @Override
    public void removeControlListener(Control cl) {
        if (m_controls.contains(cl)) {
            m_controls.remove(cl);
        }

    }

    /*
     * This handler is registered for each node that is available.
     * 
     * @see javafx.event.EventHandler#handle(javafx.event.Event)
     */
    @Override
    public void handle(Event event) {

        VisualItem item = findItem(event.getSource());
        if (item != null) {
        	if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
        		item.setHover(true);
        	} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
        		item.setHover(false);
        	}
            for (Control cl : m_controls) {
                if (cl.isItemEventEnabled()
                        && cl.getEventType()
                                .getClass()
                                .isAssignableFrom(
                                        event.getEventType().getClass())) {
                    cl.itemEvent(item, event);
                }
            }
        }
    }

    public VisualItem findItem(Object source) {
        if (source instanceof Node) {
            return m_registeredNodes.get((Node) source);
        } else {
            return null;
        }
    }

}
