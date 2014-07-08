package prefux.controls;

import java.util.EventListener;

import javafx.event.Event;
import javafx.event.EventType;
import prefux.visual.VisualItem;


/**
 * Listener interface for processing user interface events on a Display.
 * 
 * @author alan newberger
 * @author <a href="http://jheer.org">jeffrey heer</a>
 * @author Martin Stockhammer
 */
public interface Control extends EventListener
{
   
    
    /**
     * Indicates if this Control is currently enabled for item events.
     * 
     * @return true if the control is enabled, false if disabled
     */
    public boolean isItemEventEnabled();
    
    /**
     * Indicates if this Control is currently enabled for non item events.
     * 
     * @return
     */
    public boolean isNonItemEventEnabled();
    
    /**
     * Sets the enabled status for item events of this control.
     * @param enabled true to enable the control, false to disable it
     */
    public void setItemEventEnabled(boolean enabled);

    /**
     * Sets the enabled status for non item events of this control.
     * @param enabled true to enable the control, false to disable it
     */
    public void setNonItemEventEnabled(boolean enabled);

    // -- Actions performed on VisualItems ------------------------------------

    /**
     * Invoked when a event occurs on the visual item.
     */
    public void itemEvent(VisualItem item, Event e);
    
    
    /**
     * Invoked when a event occurs on the display but not on any item.
     * @param e
     */
    public void event(Event e);
  
    /**
     * Returns the event type this control listens to
     * @return
     */
    public EventType<? extends Event> getEventType();
  

} // end of interface ControlListener
