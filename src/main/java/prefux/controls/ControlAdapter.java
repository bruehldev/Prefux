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
