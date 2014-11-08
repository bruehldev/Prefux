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
package prefux.data.tuple;

import java.util.Iterator;
import java.util.LinkedHashSet;

import prefux.data.Tuple;
import prefux.data.event.EventConstants;

/**
 * <p>TupleSet implementation that maintains a set of heterogeneous Tuples
 * -- tuples that can come from any backing data source. This class supports
 * {@link #addTuple(Tuple)} and {@link #removeTuple(Tuple)} but does not
 * support adding new columns to contained tuples.</p>
 * 
 * <p>This TupleSet uses a {@link java.util.LinkedHashSet} to support fast
 * lookup of contained tuples while mainting Tuples in the order in which
 * they are added to the set.</p> 
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DefaultTupleSet extends AbstractTupleSet implements EventConstants
{
    protected LinkedHashSet<Tuple> m_tuples;

    /**
     * Create a new, empty DefaultTupleSet.
     */
    public DefaultTupleSet() {
        m_tuples = new LinkedHashSet<Tuple>();
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#getTupleCount()
     */
    public int getTupleCount() {
        return m_tuples.size();
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#addTuple(prefux.data.Tuple)
     */
    public Tuple addTuple(Tuple t) {
        t = addInternal(t);
        if ( t != null )
            fireTupleEvent(t, INSERT);
        return t;
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#setTuple(prefux.data.Tuple)
     */
    public Tuple setTuple(Tuple t) {
        Tuple[] rem = clearInternal();
        t = addInternal(t);
        Tuple[] add = t==null ? null : new Tuple[] {t};
        fireTupleEvent(add, rem);
        return t;
    }
    
    /**
     * Adds a tuple without firing a notification.
     * @param t the Tuple to add
     * @return the added Tuple
     */
    protected final Tuple addInternal(Tuple t) {
        if ( m_tuples.add(t) ) {
            return t;
        } else {
            return null;
        }
    }

    /**
     * @see prefux.data.tuple.TupleSet#containsTuple(prefux.data.Tuple)
     */
    public boolean containsTuple(Tuple t) {
        return m_tuples.contains(t);
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#removeTuple(prefux.data.Tuple)
     */
    public boolean removeTuple(Tuple t) {
        boolean b = removeInternal(t);
        if ( b )
            fireTupleEvent(t, DELETE);
        return b;
    }
    
    /**
     * Removes a tuple without firing a notification.
     * @param t the tuple to remove
     * @return true if the tuple is removed successfully, false otherwise
     */
    protected final boolean removeInternal(Tuple t) {
        return ( m_tuples.remove(t) );
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#clear()
     */
    public void clear() {
        if ( getTupleCount() > 0 ) {
            Tuple[] t = clearInternal();
            fireTupleEvent(null, t);
        }
    }
    
    /**
     * Clear the internal state without firing a notification.
     * @return an array of removed tuples
     */
    public Tuple[] clearInternal() {
        Tuple[] t = new Tuple[getTupleCount()];
        Iterator<Tuple> iter = tuples();
        for ( int i=0; iter.hasNext(); ++i ) {
            t[i] = (Tuple)iter.next();
        }
        m_tuples.clear();
        return t;
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#tuples()
     */
    public Iterator<Tuple> tuples() {
        return m_tuples.iterator();
    }
    
    /**
     * Get the contents of this TupleSet as an array.
     * @return the contents of this TupleSet as an array
     */
    public Tuple[] toArray() {
        Tuple[] t = new Tuple[getTupleCount()];
        m_tuples.toArray(t);
        return t;
    }

	@Override
    public boolean hasColumn(String name) {
	    return true;
    }
    
} // end of class DefaultTupleSet
