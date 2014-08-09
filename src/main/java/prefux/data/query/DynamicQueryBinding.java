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
package prefux.data.query;

import javax.swing.JComponent;

import prefux.data.expression.Predicate;
import prefux.data.tuple.TupleSet;

/**
 * <p>Abstract base class for dynamic query bindings, which support
 * data queries that can be dynamically edited with direct manipulation
 * user interface components. DynamicQueryBinding instances
 * take a particular field of a table, create a
 * {@link prefux.data.expression.Predicate} instance for filtering Tuples
 * based on the values of that data field, and bind that Predicate to any
 * number of user interface components that can be used to manipulate the
 * parameters of the predicate.</p>
 * 
 * <p>Examples include dynamically filtering over a particular range of
 * values ({@link RangeQueryBinding}), isolating specific categories of
 * data ({@link ListQueryBinding}), and performing text search over
 * data ({@link SearchQueryBinding}).</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class DynamicQueryBinding {
    
    /** The actual query over Table data. */
    protected Predicate m_query;
    /** The TupleSet processed by the query. */
    protected TupleSet m_tuples;
    /** The data field processed by the query. */
    protected String m_field;
    
    /**
     * Create a new DynamicQueryBinding. Called by subclasses.
     * @param tupleSet the TupleSet to query
     * @param field the data field (Table column) to query
     */
    protected DynamicQueryBinding(TupleSet tupleSet, String field) {
        m_tuples = tupleSet;
        m_field = field;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Returns the query predicate bound to this dynamic query. The predicate's
     * behavior can vary dynamically based on interaction with user interface
     * components created by this binding. To automatically monitor changes to
     * this predicate, clients should register an 
     * {@link prefux.data.event.ExpressionListener} with the
     * {@link prefux.data.expression.Predicate} returned by this method.
     * @return the dynamic query {@link prefux.data.expression.Predicate}
     */
    public Predicate getPredicate() {
        return m_query;
    }
    
    /**
     * Sets the dynamic query predicate. For class-internal use only.
     * @param p the predicate to set
     */
    protected void setPredicate(Predicate p) {
        m_query = p;
    }
    
    /**
     * Generates a new user interface component for dynamically adjusting
     * the query values. The type of the component depends on the subclass
     * of DynamicQueryBinding being used. Some subclasses can generate
     * multiple types of user interface components. Such classes will include
     * additional methods for generating the specific kinds of components
     * supported.
     * @return a user interface component for adjusting the query.
     */
    public abstract JComponent createComponent();
    
} // end of class DynamicQueryBinding
