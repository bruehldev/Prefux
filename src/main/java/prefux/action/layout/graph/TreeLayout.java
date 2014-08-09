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
package prefux.action.layout.graph;

import prefux.action.layout.Layout;
import prefux.data.Graph;
import prefux.data.Tree;
import prefux.data.tuple.TupleSet;
import prefux.visual.NodeItem;

/**
 * Abstract base class providing convenience methods for tree layout algorithms.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class TreeLayout extends Layout {

    protected NodeItem m_root;

    /**
     * Create a new TreeLayout.
     */
    public TreeLayout() {
        super();
    }

    /**
     * Create a new TreeLayout.
     * @param group the data group to layout. This must resolve to a graph
     * instance, otherwise an exception will result when subclasses attempt
     * to retrieve the layout root.
     */
    public TreeLayout(String group) {
        super(group);
    }
    
    // ------------------------------------------------------------------------

    /**
     * Explicitly set the node to use as the layout root.
     * @param root the node to use as the root.  A null value is legal, and
     * indicates that the root of the spanning tree of the backing graph will
     * be used as the layout root. If the node is not a member of this layout's
     * data group, an exception will be thrown.
     * @throws IllegalArgumentException if the provided root is not a member of
     * this layout's data group.
     */
    public void setLayoutRoot(NodeItem root) {
        if ( !root.isInGroup(m_group) )
            throw new IllegalArgumentException("Input node is not a member "
                    + "of this layout's data group");
        m_root = root;
    }
    
    /**
     * Return the NodeItem to use as the root for this tree layout.  If the
     * layout root is not set, this method has the side effect of setting it
     * to the root of the graph's spanning tree.
     * @return the root node to use for this tree layout.
     * @throws IllegalStateException if the action's data group does not
     * resolve to a {@link prefux.data.Graph} instance.
     */
    public NodeItem getLayoutRoot() {
        if ( m_root != null )
            return m_root;
        
        TupleSet ts = m_vis.getGroup(m_group);
        if ( ts instanceof Graph ) {
            Tree tree = ((Graph)ts).getSpanningTree();
            return (NodeItem)tree.getRoot();
        } else {
            throw new IllegalStateException("This action's data group does" +
                    "not resolve to a Graph instance.");
        }
    }
    
    /**
     * Clears references to graph tuples.  The group and visualization are
     * retained.
     */
    public void reset() {
    	m_root = null;
    }

} // end of abstract class TreeLayout
