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

import prefux.data.Edge;
import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Table;

/**
 * Edge implementation that reads Edge data from a backing edge table.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TableEdge extends TableTuple implements Edge {

    /**
     * The backing graph.
     */
    protected Graph m_graph;   
    
    /**
     * Initialize a new Edge backed by an edge table. This method is used by
     * the appropriate TupleManager instance, and should not be called
     * directly by client code, unless by a client-supplied custom
     * TupleManager.
     * @param table the edge Table
     * @param graph the backing Graph
     * @param row the row in the edge table to which this Node instance
     *  corresponds.
     */
    protected void init(Table table, Graph graph, int row) {
        m_table = table;
        m_graph = graph;
        m_row = m_table.isValidRow(row) ? row : -1;
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

} // end of class TableEdge
