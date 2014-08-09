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

import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Tuple;
import prefux.data.expression.AbstractPredicate;

/**
 * Filtering predicate over a potential edge table that indicates which
 * edges are valid edges according to a backing node table. Useful for
 * creating a pool of edges for which not all node have been created, and
 * then filtering out the valid edges using the node pool.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ValidEdgePredicate extends AbstractPredicate {
    
    private Graph m_g;
    
    /**
     * Creates a new ValidEdgePredicate.
     * @param g the backing graph, the node table of this graph will be used
     * to check for valid edges.
     */
    public ValidEdgePredicate(Graph g) {
        m_g = g;
    }
    
    /**
     * Indicates if the given tuple can be used as a valid edge for
     * the nodes of the backing graph.
     * @param tpl a data tuple from a potential edge table
     * @return true if the tuple contents allow it to serve as a valid
     * edge of between nodes in the backing graph
     */
    public boolean getBoolean(Tuple tpl) {
        Node s = m_g.getNodeFromKey(tpl.getInt(m_g.getEdgeSourceField()));
        Node t = m_g.getNodeFromKey(tpl.getInt(m_g.getEdgeTargetField()));
        return ( s != null && t != null );
    }
    
} // end of class ValidEdgePredicate
