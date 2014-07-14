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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prefux.controls.Control;
import prefux.data.expression.AndPredicate;
import prefux.data.expression.BooleanLiteral;
import prefux.data.expression.Predicate;
import prefux.data.util.Point2D;
import prefux.data.util.Rectangle2D;
import prefux.visual.EdgeItem;
import prefux.visual.VisualItem;
import prefux.visual.expression.VisiblePredicate;

import com.sun.javafx.css.StyleManager;

public class FxDisplay extends Group implements Display, EventHandler<Event> {

    private static final Logger log = LoggerFactory.getLogger(FxDisplay.class);

    public static final String DEFAULT_STYLESHEET = "prefux/prefux.css";

    protected AndPredicate m_predicate = new AndPredicate();

    private int m_itemCount = 0;

    private Visualization vis;

    private List<Control> m_controls = new ArrayList<>();

    private Map<Node, VisualItem> m_registeredNodes = new HashMap<>();

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
    public double getFrameRate() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getVisibleItemCount() {
        return m_itemCount;
    }

    @Override
    public void setHighQuality(boolean quality) {
        // TODO Auto-generated method stub

    }

    @Override
    public Visualization getVisualization() {
        return vis;
    }

    public void setVisualization(Visualization vis) {
        log.debug("setVisualization");
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
    public void getAbsoluteCoordinate(Point2D m_anchor, Point2D m_anchor2) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTranformInProgress() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void zoomAbs(Point2D p, double zoom) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zoom(Point2D p, double zoom) {
        // TODO Auto-generated method stub

    }

    @Override
    public void animatePanAndZoomToAbs(Point2D center, double scale,
            long duration) {
        // TODO Auto-generated method stub

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

    @Override
    public void handle(Event event) {
        VisualItem item = findItem(event.getSource());
        if (item != null) {
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

    private VisualItem findItem(Object source) {
        if (source instanceof Node) {
            return m_registeredNodes.get((Node) source);
        } else {
            return null;
        }
    }

}
