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
package prefux.controls;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prefux.data.Table;
import prefux.data.event.EventConstants;
import prefux.data.event.TableListener;
import prefux.data.util.Point2D;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;

/**
 * Changes a node's location when dragged on screen. Other effects include
 * fixing a node's position when the mouse if over it, and changing the mouse
 * cursor to a hand when the mouse passes over an item.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DragControl extends ControlAdapter implements TableListener {

    private VisualItem activeItem;
    protected String action;
    protected Point2D down = new Point2D();
    protected Point2D temp = new Point2D();
    protected boolean dragged, wasFixed, resetItem;
    private boolean fixOnMouseOver = true;
    private Tooltip mousePositionToolTip = new Tooltip("");

    private static final Logger log = LogManager.getLogger(DragControl.class);
    private Delta delta = new Delta();

    /**
     * Creates a new drag control that issues repaint requests as an item is
     * dragged.
     */
    public DragControl() {
    }

    /**
     * Creates a new drag control that invokes an action upon drag events.
     *
     * @param action the action to run when drag events occur.
     */
    public DragControl(String action) {
        this.action = action;
    }

    /**
     * Creates a new drag control that invokes an action upon drag events.
     *
     * @param action         the action to run when drag events occur
     * @param fixOnMouseOver indicates if object positions should become fixed (made
     *                       stationary) when the mouse pointer is over an item.
     */
    public DragControl(String action, boolean fixOnMouseOver) {
        this.fixOnMouseOver = fixOnMouseOver;
        this.action = action;
    }

    /**
     * Determines whether or not an item should have it's position fixed when
     * the mouse moves over it.
     *
     * @param s whether or not item position should become fixed upon mouse
     *          over.
     */
    public void setFixPositionOnMouseOver(boolean s) {
        fixOnMouseOver = s;
    }

    @Override
    public void itemEvent(VisualItem item, Event e) {

        MouseEvent ev = (MouseEvent) e;
        activeItem = item;
        //  Start Copy
        if (item.get("name") != null) {
            activeItem.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String msg = item.get("name").toString();
                    mousePositionToolTip.setText(msg);
                    Node node = (Node) event.getSource();
                    mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
                }
            });
            activeItem.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mousePositionToolTip.hide();
                    mousePositionToolTip.setText("");
                }
            });
        }
        // Copy end

        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            delta.x = item.getX() - ev.getSceneX();
            delta.y = item.getY() - ev.getSceneY();
            item.getNode().setCursor(Cursor.MOVE);
        } else if (e.getEventType() == MouseEvent.DRAG_DETECTED) {
            log.info("Drag Event detected");
            wasFixed = item.isFixed();
            resetItem = true;
            activeItem = item;
            item.setFixed(true);
            item.getTable().addTableListener(this);
            PrefuseLib.setX(activeItem, null, ev.getSceneX() + delta.x);
            PrefuseLib.setY(activeItem, null, ev.getSceneY() + delta.y);
            //for( EdgeRenderer.Arrow arrow: EdgeRenderer.arrows) {
            //arrow.update(ev.getSceneX() , ev.getSceneY() );
            //}
        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (activeItem != null) {
                PrefuseLib.setX(activeItem, null, ev.getSceneX() + delta.x);
                PrefuseLib.setY(activeItem, null, ev.getSceneY() + delta.y);
            }
        } else if (e.getEventType() == MouseDragEvent.MOUSE_DRAG_RELEASED ||
                e.getEventType() == MouseEvent.MOUSE_RELEASED) {
            if (activeItem != null) {
                activeItem.setFixed(false);
                activeItem = null;
            }
        }
    }

    @Override
    public EventType<? extends Event> getEventType() {
        return MouseEvent.ANY;
    }

    // /**
    // * @see prefux.controls.Control#itemEntered(prefux.visual.VisualItem,
    // java.awt.event.MouseEvent)
    // */
    // public void itemEntered(VisualItem item, MouseEvent e) {
    // Display d = (Display)e.getSource();
    // d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    // activeItem = item;
    // if ( fixOnMouseOver ) {
    // wasFixed = item.isFixed();
    // resetItem = true;
    // item.setFixed(true);
    // item.getTable().addTableListener(this);
    // }
    // }
    //
    // /**
    // * @see prefux.controls.Control#itemExited(prefux.visual.VisualItem,
    // java.awt.event.MouseEvent)
    // */
    // public void itemExited(VisualItem item, MouseEvent e) {
    // if ( activeItem == item ) {
    // activeItem = null;
    // item.getTable().removeTableListener(this);
    // if ( resetItem ) item.setFixed(wasFixed);
    // }
    // Display d = (Display)e.getSource();
    // d.setCursor(Cursor.getDefaultCursor());
    // } //
    //
    // /**
    // * @see prefux.controls.Control#itemPressed(prefux.visual.VisualItem,
    // java.awt.event.MouseEvent)
    // */
    // public void itemPressed(VisualItem item, MouseEvent e) {
    // if (!SwingUtilities.isLeftMouseButton(e)) return;
    // if ( !fixOnMouseOver ) {
    // wasFixed = item.isFixed();
    // resetItem = true;
    // item.setFixed(true);
    // item.getTable().addTableListener(this);
    // }
    // dragged = false;
    // Display d = (Display)e.getComponent();
    // d.getAbsoluteCoordinate(e.getPoint(), down);
    // }
    //
    // /**
    // * @see prefux.controls.Control#itemReleased(prefux.visual.VisualItem,
    // java.awt.event.MouseEvent)
    // */
    // public void itemReleased(VisualItem item, MouseEvent e) {
    // if (!SwingUtilities.isLeftMouseButton(e)) return;
    // if ( dragged ) {
    // activeItem = null;
    // item.getTable().removeTableListener(this);
    // if ( resetItem ) item.setFixed(wasFixed);
    // dragged = false;
    // }
    // }
    //
    // /**
    // * @see prefux.controls.Control#itemDragged(prefux.visual.VisualItem,
    // java.awt.event.MouseEvent)
    // */
    // public void itemDragged(VisualItem item, MouseEvent e) {
    // if (!SwingUtilities.isLeftMouseButton(e)) return;
    // dragged = true;
    // Display d = (Display)e.getComponent();
    // d.getAbsoluteCoordinate(e.getPoint(), temp);
    // double dx = temp.getX()-down.getX();
    // double dy = temp.getY()-down.getY();
    // double x = item.getX();
    // double y = item.getY();
    //
    // item.setStartX(x); item.setStartY(y);
    // item.setX(x+dx); item.setY(y+dy);
    // item.setEndX(x+dx); item.setEndY(y+dy);
    //
    // if ( repaint )
    // item.getVisualization().repaint();
    //
    // down.setLocation(temp);
    // if ( action != null )
    // d.getVisualization().run(action);
    // }

    /**
     * @see prefux.data.event.TableListener#tableChanged(prefux.data.Table, int,
     * int, int, int)
     */
    public void tableChanged(Table t, int start, int end, int col, int type) {
        if (activeItem == null || type != EventConstants.UPDATE
                || col != t.getColumnNumber(VisualItem.FIXED))
            return;
        int row = activeItem.getRow();
        if (row >= start && row <= end)
            resetItem = false;
    }

} // end of class DragControl
