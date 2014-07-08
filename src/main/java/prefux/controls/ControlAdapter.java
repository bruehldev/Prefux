package prefux.controls;



import javafx.event.Event;
import javafx.event.EventType;
import prefux.visual.VisualItem;


/**
 * Adapter class for processing prefux interface events. Subclasses can
 * override the desired methods to perform user interface event handling.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ControlAdapter implements Control {

    private boolean m_itemEnabled = true;
    private boolean m_nonItemEnabled = true;

    /**
     * 
     */
    public void setItemEventEnabled(boolean enabled) {
        m_itemEnabled = enabled;
    }
    
    public void setNonItemEventEnabled(boolean enabled) {
        m_nonItemEnabled = enabled;
    }

    @Override
    public void itemEvent(VisualItem item, Event e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void event(Event e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isItemEventEnabled() {
        return m_itemEnabled;
    }

    @Override
    public boolean isNonItemEventEnabled() {
        return m_nonItemEnabled;
    }

    @Override
    public EventType<? extends Event> getEventType() {
        return Event.ANY;
    }

    
    // ------------------------------------------------------------------------
    

} // end of class ControlAdapter
