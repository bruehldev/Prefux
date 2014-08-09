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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import prefux.data.Table;
import prefux.data.Tuple;
import prefux.data.event.TupleSetListener;
import prefux.data.expression.Expression;
import prefux.data.expression.Predicate;
import prefux.data.expression.parser.ExpressionParser;
import prefux.util.collections.CompositeIterator;

/**
 * <p>TupleSet implementation for treating a collection of tuple sets
 * as a single, composite tuple set. This composite does not take
 * overlap between contained TupleSets into account.</p>
 * 
 * <p>The {@link TupleSet#addTuple(Tuple)} and {@link #setTuple(Tuple)}
 * methods are not supported by this class, and calling these methods will
 * result in a UnsupportedOperationException. Instead, use the add or set
 * methods on the desired non-composite tuple set.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CompositeTupleSet extends AbstractTupleSet {

    private static final Logger s_logger
        = Logger.getLogger(CompositeTupleSet.class.getName());
    
    private Map<String, TupleSet> m_map;   // map names to tuple sets
    private Set<TupleSet> m_sets;  // support quick reverse lookup
    private int m_count; // count of total tuples
    private Listener m_lstnr;
    
    /**
     * Create a new, empty CompositeTupleSet
     */
    public CompositeTupleSet() {
        this(true);
    }
    
    protected CompositeTupleSet(boolean listen) {
        m_map = new LinkedHashMap<>();
        m_sets = new HashSet<>();
        m_count = 0;
        m_lstnr = listen ? new Listener() : null;
    }
    
    /**
     * Add a TupleSet to this composite.
     * @param name the name of the TupleSet to add
     * @param set the set to add
     */
    public void addSet(String name, TupleSet set) {
        if ( hasSet(name) ) {
            throw new IllegalArgumentException("Name already in use: "+name);
        }
        m_map.put(name, set);
        m_sets.add(set);
        m_count += set.getTupleCount();
        if ( m_lstnr != null )
            set.addTupleSetListener(m_lstnr);
    }
    
    /**
     * Indicates if this composite contains a TupleSet with the given name.
     * @param name the name to look for
     * @return true if a TupleSet with the given name is found, false otherwise
     */
    public boolean hasSet(String name) {
        return m_map.containsKey(name);
    }
    
    /**
     * Indicates if this composite contains the given TupleSet.
     * @param set the TupleSet to check for containment
     * @return true if the TupleSet is contained in this composite,
     * false otherwise
     */
    public boolean containsSet(TupleSet set) {
        return m_sets.contains(set);
    }
    
    /**
     * Get the TupleSet associated with the given name.
     * @param name the  name of the TupleSet to get
     * @return the associated TupleSet, or null if not found
     */
    public TupleSet getSet(String name) {
        return (TupleSet)m_map.get(name);
    }
    
    /**
     * Get an iterator over the names of all the TupleSets in this composite.
     * @return the iterator over contained set names.
     */
    public Iterator<String> setNames() {
        return m_map.keySet().iterator();
    }

    /**
     * Get an iterator over all the TupleSets in this composite.
     * @return the iterator contained sets.
     */
    public Iterator<TupleSet> sets() {
        return m_map.values().iterator();
    }
    
    /**
     * Remove the TupleSet with the given name from this composite.
     * @param name the name of the TupleSet to remove
     * @return the removed TupleSet, or null if not found
     */
    public TupleSet removeSet(String name) {
        TupleSet ts = (TupleSet)m_map.remove(name);
        if ( ts != null ) {
            m_sets.remove(ts);
            if ( m_lstnr != null )
                ts.removeTupleSetListener(m_lstnr);
        }
        return ts;
    }
    
    /**
     * Remove all contained TupleSets from this composite.
     */
    public void removeAllSets() {
        Iterator<Entry<String, TupleSet>> sets = m_map.entrySet().iterator();
        while ( sets.hasNext() ) {
			Entry<String, TupleSet> entry = sets.next();
            TupleSet ts = entry.getValue();
            sets.remove();
            m_sets.remove(ts);
            if ( m_lstnr != null )
                ts.removeTupleSetListener(m_lstnr);
        }
        m_count = 0;
    }
    
    /**
     * Clear this TupleSet, calling clear on all contained TupleSet
     * instances. All contained TupleSets remain members of this
     * composite, but they have their data cleared.
     * @see prefux.data.tuple.TupleSet#clear()
     */
    public void clear() {
        Iterator<Entry<String,TupleSet>> sets = m_map.entrySet().iterator();
        while ( sets.hasNext() ) {
            Entry<String, TupleSet> entry = sets.next();
            entry.getValue().clear();
        }
        m_count = 0;
    }
    
    // ------------------------------------------------------------------------
    // TupleSet Interface
    
    /**
     * Not supported.
     * @see prefux.data.tuple.TupleSet#addTuple(prefux.data.Tuple)
     */
    public Tuple addTuple(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     * @see prefux.data.tuple.TupleSet#setTuple(prefux.data.Tuple)
     */
    public Tuple setTuple(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the tuple from its source set if that source set is contained
     * within this composite.
     * @see prefux.data.tuple.TupleSet#removeTuple(prefux.data.Tuple)
     */
    public boolean removeTuple(Tuple t) {
        Table table = t.getTable();
        if ( m_sets.contains(table) ) {
            return table.removeTuple(t);
        } else {
            return false;
        }
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#containsTuple(prefux.data.Tuple)
     */
    public boolean containsTuple(Tuple t) {
        Iterator<Entry<String, TupleSet>> it = m_map.entrySet().iterator();
        while ( it.hasNext() )  {
			Entry<String, TupleSet> entry = it.next();
            TupleSet ts = entry.getValue();
            if ( ts.containsTuple(t) )
                return true;
        }
        return false;
    }

    /**
     * @see prefux.data.tuple.TupleSet#getTupleCount()
     */
    public int getTupleCount() {
        if ( m_lstnr != null ) {
            return m_count;
        } else {
            int count = 0;
            Iterator<Entry<String,TupleSet>> it = m_map.entrySet().iterator();
            while ( it.hasNext())  {
                Entry<String, TupleSet> entry = it.next();
                TupleSet ts = entry.getValue();
                count += ts.getTupleCount();
            }
            return count;
        }
    }

    /**
     * @see prefux.data.tuple.TupleSet#tuples()
     */
    public Iterator tuples() {
        CompositeIterator ci = new CompositeIterator(m_map.size());
        Iterator<Entry<String, TupleSet>> it = m_map.entrySet().iterator();
        for ( int i=0; it.hasNext(); ++i )  {
            Entry<String, TupleSet> entry = it.next();
            TupleSet ts = entry.getValue();
            ci.setIterator(i, ts.tuples());
        }
        return ci;
    }
    
    /**
     * @see prefux.data.tuple.TupleSet#tuples(prefux.data.expression.Predicate)
     */
    public Iterator tuples(Predicate filter) {
        CompositeIterator ci = new CompositeIterator(m_map.size());
        Iterator it = m_map.entrySet().iterator();
        for ( int i=0; it.hasNext(); ++i )  {
            Map.Entry entry = (Map.Entry)it.next();
            TupleSet ts = (TupleSet)entry.getValue();
            ci.setIterator(i, ts.tuples(filter));
        }
        return ci;
    }
    
    // -- Data Field Methods --------------------------------------------------
    
    /**
     * Returns true.
     * @see prefux.data.tuple.TupleSet#isAddColumnSupported()
     */
    public boolean isAddColumnSupported() {
        return true;
    }

    /**
     * Adds the value to all contained TupleSets that return a true value for
     * {@link TupleSet#isAddColumnSupported()}.
     * @see prefux.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class, java.lang.Object)
     */
    public void addColumn(String name, Class type, Object defaultValue) {
        Iterator it = m_map.entrySet().iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry)it.next();
            TupleSet ts = (TupleSet)entry.getValue();
            if ( ts.isAddColumnSupported() ) {
                try {
                    ts.addColumn(name, type, defaultValue);
                } catch ( IllegalArgumentException iae ) {
                    // already exists
                }
            } else {
                s_logger.fine("Skipped addColumn for "+entry.getKey());
            }
        }
    }

    /**
     * Adds the value to all contained TupleSets that return a true value for
     * {@link TupleSet#isAddColumnSupported()}.
     * @see prefux.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class)
     */
    public void addColumn(String name, Class type) {
        Iterator it = m_map.entrySet().iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry)it.next();
            TupleSet ts = (TupleSet)entry.getValue();
            if ( ts.isAddColumnSupported() ) {
                try {
                    ts.addColumn(name, type);
                } catch ( IllegalArgumentException iae ) {
                    // already exists
                }
            } else {
                s_logger.fine("Skipped addColumn for "+entry.getKey());
            }
        }
    }

    /**
     * Adds the value to all contained TupleSets that return a true value for
     * {@link TupleSet#isAddColumnSupported()}.
     * @see prefux.data.tuple.TupleSet#addColumn(java.lang.String, prefux.data.expression.Expression)
     */
    public void addColumn(String name, Expression expr) {
        Iterator it = m_map.entrySet().iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry)it.next();
            TupleSet ts = (TupleSet)entry.getValue();
            if ( ts.isAddColumnSupported() ) {
                try {
                    ts.addColumn(name, expr);
                } catch ( IllegalArgumentException iae ) {
                    // already exists
                }
            } else {
                s_logger.fine("Skipped addColumn for "+entry.getKey());
            }
        }
    }

    /**
     * Adds the value to all contained TupleSets that return a true value for
     * {@link TupleSet#isAddColumnSupported()}.
     * @see prefux.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.String)
     */
    public void addColumn(String name, String expr) {
        Expression ex = ExpressionParser.parse(expr);
        Throwable t = ExpressionParser.getError();
        if ( t != null ) {
            throw new RuntimeException(t);
        } else {
            addColumn(name, ex);
        }
    }    
    
    // ------------------------------------------------------------------------
    // Internal TupleSet Listener
    
    /**
     * Listener that relays tuple set change events as they occur and updates
     * the total tuple count appropriately.
     */
    private class Listener implements TupleSetListener {
        public void tupleSetChanged(TupleSet tset, Tuple[] add, Tuple[] rem) {
            m_count += add.length - rem.length;
            fireTupleEvent(add, rem);
        }
    }

} // end of class CompositeTupleSet
