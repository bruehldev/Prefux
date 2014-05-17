/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefux.txt" for licensing terms.
 */
package prefux.util.display;

import prefux.Display;

/**
 * Listener interface for monitoring changes to the space occupied by
 * VisualItems within the space of a Display.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface ItemBoundsListener {

    /**
     * Signals a change in the total bounds occupied by VisualItems in
     * a particular Display.
     * @param d the Display whose item bounds has changed
     */
    public void itemBoundsChanged(Display d);
    
} // end of interface ItemBoundsListener
