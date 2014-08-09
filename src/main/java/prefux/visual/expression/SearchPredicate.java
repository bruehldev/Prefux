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
package prefux.visual.expression;

import prefux.Visualization;
import prefux.data.Tuple;
import prefux.data.expression.BooleanLiteral;
import prefux.data.expression.Expression;
import prefux.data.search.SearchTupleSet;
import prefux.visual.VisualItem;

/**
 * Expression that indicates if an item is currently a member of a data group of the type
 * {@link prefux.data.search.SearchTupleSet}, but including a possible special case in
 * which all items should be pass through the predicate if no search query is specified.
 * The data group name is provided by a String-valued sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SearchPredicate extends InGroupPredicate {

    private Expression m_incEmpty;
    private int paramCount = 0;
    
    /**
     * Create a new SearchPredicate. By default, looks into the
     * {@link prefux.Visualization#ALL_ITEMS} data group and assumes all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate() {
        this(Visualization.SEARCH_ITEMS, true);
        paramCount = 0;
    }
    
    /**
     * Create a new SearchPredicate. By default, looks into the
     * {@link prefux.Visualization#ALL_ITEMS} data group.
     * @param includeAllByDefault indicates if all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate(boolean includeAllByDefault) {
        this(Visualization.SEARCH_ITEMS, includeAllByDefault);
    }
    
    /**
     * Create a new SearchPredicate.
     * @param group the data group to look up, should resolve to a
     * {@link prefux.data.search.SearchTupleSet} instance.
     * @param includeAllByDefault indicates if all items
     * should pass the predicate if no search query has been specified.
     */
    public SearchPredicate(String group, boolean includeAllByDefault) {
        super(group);
        m_incEmpty = new BooleanLiteral(includeAllByDefault);
        paramCount = 2;
    }
    
    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        String group = getGroup(t);
        if ( group == null ) return false;
        boolean incEmpty = m_incEmpty.getBoolean(t);
        
        VisualItem item = (VisualItem)t;
        Visualization vis = item.getVisualization();
        SearchTupleSet search = (SearchTupleSet)vis.getGroup(group);
        if ( search == null && incEmpty )
            return true;
        
        String query = search.getQuery();
        return (incEmpty && (query==null || query.length()==0)) 
                || vis.isInGroup(item, group);
    }

    /**
     * @see prefux.data.expression.Function#addParameter(prefux.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        if ( paramCount == 0 )
            super.addParameter(e);
        else if ( paramCount == 1 )
            m_incEmpty = e;
        else
            throw new IllegalStateException(
              "This function takes only 2 parameters.");
    }

    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "MATCH";
    }

    /**
     * @see prefux.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 2;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"("+m_group+", "+m_incEmpty+")";
    }
    
} // end of class SearchPredicate
