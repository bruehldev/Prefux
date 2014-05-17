package prefux.controls;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import prefux.visual.VisualItem;


/**
 * Adapter class for processing prefux interface events. Subclasses can
 * override the desired methods to perform user interface event handling.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ControlAdapter implements Control {

    private boolean m_enabled = true;
    
    /**
     * @see prefux.controls.Control#isEnabled()
     */
    public boolean isEnabled() {
        return m_enabled;
    }
    
    /**
     * @see prefux.controls.Control#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        m_enabled = enabled;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.controls.Control#itemDragged(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemMoved(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemMoved(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemWheelMoved(prefux.visual.VisualItem, java.awt.event.MouseWheelEvent)
     */
    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemClicked(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemClicked(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemPressed(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemReleased(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemEntered(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemExited(prefux.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemKeyPressed(prefux.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyPressed(VisualItem item, KeyEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemKeyReleased(prefux.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyReleased(VisualItem item, KeyEvent e) {
    } 

    /**
     * @see prefux.controls.Control#itemKeyTyped(prefux.visual.VisualItem, java.awt.event.KeyEvent)
     */
    public void itemKeyTyped(VisualItem item, KeyEvent e) {
    } 

    /**
     * @see prefux.controls.Control#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {    
    } 

    /**
     * @see prefux.controls.Control#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {     
    } 

    /**
     * @see prefux.controls.Control#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {    
    } 

    /**
     * @see prefux.controls.Control#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
    } 

    /**
     * @see prefux.controls.Control#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
    } 

    /**
     * @see prefux.controls.Control#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
    } 

    /**
     * @see prefux.controls.Control#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
    } 

    /**
     * @see prefux.controls.Control#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    } 

} // end of class ControlAdapter
