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
package prefux.util;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import prefux.data.Tuple;
import prefux.data.event.ExpressionListener;
import prefux.data.event.TupleSetListener;
import prefux.data.expression.Expression;
import prefux.data.tuple.TupleSet;

/**
 * Convenience listener class that implements ExpressionListener,
 * TupleSetListener, and ComponentListener and routes all the
 * callbacks into a generic {@link #update(Object)} method. For the
 * case of ComponentListener, only the resize event is funneled into
 * the update method.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class UpdateListener 
    implements ExpressionListener, TupleSetListener, ComponentListener
{
    /**
     * Generic update routine triggerred by any of the other callbacks.
     * @param source a source object, either the Expression, TupleSet,
     * or Component that triggered this update event.
     */
    public abstract void update(Object source);
    
    /**
     * @see prefux.data.event.ExpressionListener#expressionChanged(prefux.data.expression.Expression)
     */
    public void expressionChanged(Expression expr) {
        update(expr);
    }
    
    /**
     * @see prefux.data.event.TupleSetListener#tupleSetChanged(prefux.data.tuple.TupleSet, prefux.data.Tuple[], prefux.data.Tuple[])
     */
    public void tupleSetChanged(TupleSet tset, Tuple[] added, Tuple[] removed) {
        update(tset);
    }
    
    /**
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent e) {
        update(e.getSource());
    }

    /**
     * Does nothing.
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e) {
        // do nothing
    }
    /**
     * Does nothing.
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent e) {
        // do nothing
    }
    /**
     * Does nothing.
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e) {
        // do nothing
    }

} // end of abstract class UpdateListener
