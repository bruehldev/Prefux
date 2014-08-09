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
package prefux.data.util;

import prefux.data.event.ProjectionListener;
import prefux.util.collections.CopyOnWriteArrayList;

/**
 * Abstract base class for column projection instances. Implements the
 * listener functionality.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractColumnProjection implements ColumnProjection {

    // ------------------------------------------------------------------------
    // Listener Methods
    
    private CopyOnWriteArrayList m_listeners;
    
    /**
     * @see prefux.data.util.ColumnProjection#addProjectionListener(prefux.data.event.ProjectionListener)
     */
    public void addProjectionListener(ProjectionListener lstnr) {
        if ( m_listeners == null )
            m_listeners = new CopyOnWriteArrayList();
        if ( !m_listeners.contains(lstnr) )
            m_listeners.add(lstnr);
    }

    /**
     * @see prefux.data.util.ColumnProjection#removeProjectionListener(prefux.data.event.ProjectionListener)
     */
    public void removeProjectionListener(ProjectionListener lstnr) {
        if ( m_listeners != null )
            m_listeners.remove(lstnr);
        if ( m_listeners.size() == 0 )
            m_listeners = null;
    }
    
    public void fireUpdate() {
        if ( m_listeners == null )
            return;
        Object[] lstnrs = m_listeners.getArray();
        for ( int i=0; i<lstnrs.length; ++i ) {
            ((ProjectionListener)lstnrs[i]).projectionChanged(this);
        }
    }
    
} // end of abstract class AbstractColumnProjection
