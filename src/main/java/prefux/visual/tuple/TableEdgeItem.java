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
package prefux.visual.tuple;

import prefux.data.Graph;
import prefux.data.Node;
import prefux.visual.EdgeItem;
import prefux.visual.NodeItem;
import prefux.visual.VisualTable;

/**
 * EdgeItem implementation that used data values from a backing
 * VisualTable of edges.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableEdgeItem extends TableVisualItem implements EdgeItem {

    protected Graph m_graph;
    
    /**
     * Initialize a new TableEdgeItem for the given graph, table, and row.
     * This method is used by the appropriate TupleManager instance, and
     * should not be called directly by client code, unless by a
     * client-supplied custom TupleManager.
     * @param table the backing VisualTable
     * @param graph the backing VisualGraph
     * @param row the row in the node table to which this Edge instance
     *  corresponds.
     */
    protected void init(VisualTable table, Graph graph, int row) {
        super.init(table, graph, row);
        m_graph = graph;
    }
    
    /**
     * @see prefux.data.Edge#getGraph()
     */
    public Graph getGraph() {
        return m_graph;
    }
    
    /**
     * @see prefux.data.Edge#isDirected()
     */
    public boolean isDirected() {
        return m_graph.isDirected();
    }

    /**
     * @see prefux.data.Edge#getSourceNode()
     */
    public Node getSourceNode() {
        return m_graph.getSourceNode(this);
    }

    /**
     * @see prefux.data.Edge#getTargetNode()
     */
    public Node getTargetNode() {
        return m_graph.getTargetNode(this);
    }

    /**
     * @see prefux.data.Edge#getAdjacentNode(prefux.data.Node)
     */
    public Node getAdjacentNode(Node n) {
        return m_graph.getAdjacentNode(this, n);
    }
    
    /**
     * @see prefux.visual.EdgeItem#getSourceItem()
     */
    public NodeItem getSourceItem() {
        return (NodeItem)getSourceNode();
    }

    /**
     * @see prefux.visual.EdgeItem#getTargetItem()
     */
    public NodeItem getTargetItem() {
        return (NodeItem)getTargetNode();
    }

    /**
     * @see prefux.visual.EdgeItem#getAdjacentItem(prefux.visual.NodeItem)
     */
    public NodeItem getAdjacentItem(NodeItem n) {
        return (NodeItem)getAdjacentNode(n);
    }

} // end of class TableEdgeItem
