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
package prefux.visual;

import prefux.Visualization;
import prefux.data.Graph;
import prefux.data.Table;
import prefux.data.event.EventConstants;
import prefux.util.collections.IntIterator;

/**
 * A visual abstraction of a graph data structure. NodeItem and EdgeItem tuples
 * provide the visual representations for the nodes and edges of the graph.
 * VisualGraphs should not be created directly, they are created automatically
 * by adding data to a Visualization, for example by using the
 * {@link Visualization#addGraph(String, Graph)} method.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class VisualGraph extends Graph implements VisualTupleSet {

    private Visualization m_vis;
    private String m_group;
   
    /**
     * Create a new VisualGraph
     * @param nodes the visual node table
     * @param edges the visual edge table
     * @param directed indicates if graph edges are directed or undirected
     * @param nodeKey the node table field by which to index the nodes.
     * This value can be null, indicating that just the row indices should be
     * used.
     * @param sourceKey the edge table field storing source node keys
     * @param targetKey the edge table field storing target node keys
     */
    public VisualGraph(VisualTable nodes, VisualTable edges, boolean directed,
            String nodeKey, String sourceKey, String targetKey)
    {
        super(nodes, edges, directed, nodeKey, sourceKey, targetKey);
    }
    
    /**
     * Fire a graph event. Makes sure to invalidate all edges connected
     * to a node that has been updated.
     * @see prefux.data.Graph#fireGraphEvent(prefux.data.Table, int, int, int, int)
     */
    protected void fireGraphEvent(Table t, 
            int first, int last, int col, int type)
    {
        // if a node is invalidated, invalidate the edges, too
        if ( type==EventConstants.UPDATE && 
             col==VisualItem.IDX_VALIDATED && t==getNodeTable() )
        {
            VisualTable nodes = (VisualTable)t;
            VisualTable edges = (VisualTable)getEdgeTable();
            
            for ( int i=first; i<=last; ++i ) {
                if ( nodes.isValidated(i) )
                    continue; // look only for invalidations
                
                if ( i < 0 ) {
                    System.err.println("catch me - VisualGraph fireGraphEvent");
                }
//                try {
                IntIterator erows = edgeRows(i);
                while ( erows.hasNext() ) {
                    int erow = erows.nextInt();
                    edges.setValidated(erow, false);
                }
//                } catch ( Exception ex ) {
//                  ex.printStackTrace();
//                }
            }
        }
        // fire the event off to listeners
        super.fireGraphEvent(t, first, last, col, type);
    }
    
    /**
     * Get the node row index value for the given key.
     * TODO: test this more thoroughly?
     */
    public int getNodeIndex(int key) {
        if ( m_nkey == null ) {
            return ((VisualTable)getNodeTable()).getChildRow(key);
        } else {
            return super.getNodeIndex(key);
        }
    }
    
    // ------------------------------------------------------------------------
    // VisualGraph Methods
    
    /**
     * @see prefux.visual.VisualTupleSet#getVisualization()
     */
    public Visualization getVisualization() {
        return m_vis;
    }
    
    /**
     * Set the visualization associated with this VisualGraph
     * @param vis the visualization to set
     */
    public void setVisualization(Visualization vis) {
        m_vis = vis;
    }
    
    /**
     * Get the visualization data group name for this graph
     * @return the data group name
     */
    public String getGroup() {
        return m_group;
    }
    
    /**
     * Set the visualization data group name for this graph
     * @return the data group name to use
     */
    public void setGroup(String group) {
        m_group = group;
    }
    
} // end of class VisualGraph
