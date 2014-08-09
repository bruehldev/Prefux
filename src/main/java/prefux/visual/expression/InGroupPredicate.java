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

import prefux.data.Schema;
import prefux.data.Tuple;
import prefux.data.expression.Predicate;
import prefux.visual.VisualItem;

/**
 * Expression that indicates if an item is currently a member of a particular
 * data group. The data group name is provided by a String-valued
 * sub-expression.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class InGroupPredicate extends GroupExpression implements Predicate {
    
    /**
     * Create a new InGroupPredicate.
     */
    public InGroupPredicate() {
    }
    
    /**
     * Create a new InGroupPredicate.
     * @param group @param group the data group name to use as a parameter
     */
    public InGroupPredicate(String group) {
        super(group);
    }
    
    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return getBoolean(t) ? Boolean.TRUE : Boolean.FALSE;
    }
    
    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        if ( !(t instanceof VisualItem) )
            return false;
        
        String group = getGroup(t);
        if ( group == null ) {
            return false;
        }
        VisualItem item = (VisualItem)t;
        return item.getVisualization().isInGroup(item, group);
    }

    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "INGROUP";
    }

    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return boolean.class;
    }
    
} // end of class InGroupPredicate
