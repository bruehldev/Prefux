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
package prefux.action.filter;

import java.util.Iterator;

import prefux.Constants;
import prefux.Visualization;
import prefux.action.GroupAction;
import prefux.data.Graph;
import prefux.data.Node;
import prefux.data.Tree;
import prefux.data.expression.Predicate;
import prefux.util.PrefuseLib;
import prefux.visual.EdgeItem;
import prefux.visual.NodeItem;
import prefux.visual.VisualItem;
import prefux.visual.expression.InGroupPredicate;

/**
 * <p>Filter Action that computes a fisheye degree-of-interest function over
 * a tree structure (or the spanning tree of a graph structure). Visibility
 * and DOI (degree-of-interest) values are set for the nodes in the
 * structure. This function includes current focus nodes, and includes 
 * neighbors only in a limited window around these foci. The size of this
 * window is determined by the distance value set for this action. All
 * ancestors of a focus up to the root of the tree are considered foci as well.
 * By convention, DOI values start at zero for focus nodes, with decreasing
 * negative numbers for each hop away from a focus.</p>
 * 
 * <p>This form of filtering was described by George Furnas as early as 1981.
 * For more information about Furnas' fisheye view calculation and DOI values,
 * take a look at G.W. Furnas, "The FISHEYE View: A New Look at Structured 
 * Files," Bell Laboratories Tech. Report, Murray Hill, New Jersey, 1981. 
 * Available online at <a href="http://citeseer.nj.nec.com/furnas81fisheye.html">
 * http://citeseer.nj.nec.com/furnas81fisheye.html</a>.</p>
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FisheyeTreeFilter extends GroupAction {

    private String m_sources;
    private Predicate m_groupP;
    
    private int m_threshold;
    
    private NodeItem m_root;
    private double m_divisor;
    
    /**
     * Create a new FisheyeTreeFilter that processes the given group.
     * @param group the data group to process. This should resolve to
     * a Graph instance, otherwise exceptions will result when this
     * Action is run.
     */
    public FisheyeTreeFilter(String group) {
        this(group, 1);
    }

    /**
     * Create a new FisheyeTreeFilter that processes the given group.
     * @param group the data group to process. This should resolve to
     * a Graph instance, otherwise exceptions will result when this
     * Action is run.
     * @param distance the graph distance threshold from high-interest
     * nodes past which nodes will not be visible nor expanded.
     */
    public FisheyeTreeFilter(String group, int distance) {
        this(group, Visualization.FOCUS_ITEMS, distance);
    }
    
    /**
     * Create a new FisheyeTreeFilter that processes the given group.
     * @param group the data group to process. This should resolve to
     * a Graph instance, otherwise exceptions will result when this
     * Action is run.
     * @param sources the group to use as source nodes, representing the
     * nodes of highest degree-of-interest.
     * @param distance the graph distance threshold from high-interest
     * nodes past which nodes will not be visible nor expanded.
     */
    public FisheyeTreeFilter(String group, String sources, int distance)
    {
        super(group);
        m_sources = sources;
        m_threshold = -distance;
        m_groupP = new InGroupPredicate(
                PrefuseLib.getGroupName(group, Graph.NODES));
    }
    
    /**
     * Get the graph distance threshold used by this filter. This
     * is the threshold for high-interest nodes, past which nodes will
     * not be visible nor expanded.
     * @return the graph distance threshold
     */
    public int getDistance() {
        return -m_threshold;
    }

    /**
     * Set the graph distance threshold used by this filter. This
     * is the threshold for high-interest nodes, past which nodes will
     * not be visible nor expanded.
     * @param distance the graph distance threshold to use
     */
    public void setDistance(int distance) {
        m_threshold = -distance;
    }
    
    /**
     * Get the name of the group to use as source nodes for measuring
     * graph distance. These form the roots from which the graph distance
     * is measured.
     * @return the source data group
     */
    public String getSources() {
        return m_sources;
    }
    
    /**
     * Set the name of the group to use as source nodes for measuring
     * graph distance. These form the roots from which the graph distance
     * is measured.
     * @param sources the source data group
     */
    public void setSources(String sources) {
        m_sources = sources;
    }
    
    /**
     * @see prefux.action.GroupAction#run(double)
     */
    public void run(double frac) {
        Tree tree = ((Graph)m_vis.getGroup(m_group)).getSpanningTree();
        m_divisor = tree.getNodeCount();
        m_root = (NodeItem)tree.getRoot();
        
        // mark the items
        Iterator<VisualItem> items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            item.setDOI(Constants.MINIMUM_DOI);
            item.setExpanded(false);
        }
        
        // compute the fisheye over nodes
        Iterator<VisualItem> iter = m_vis.items(m_sources, m_groupP);
        while ( iter.hasNext() )
            visitFocus((NodeItem)iter.next(), null);
        visitFocus(m_root, null);

        // mark unreached items
        items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( item.getDOI() == Constants.MINIMUM_DOI )
                PrefuseLib.updateVisible(item, false);
        }
    }

    /**
     * Visit a focus node.
     */
    private void visitFocus(NodeItem n, NodeItem c) {
        if ( n.getDOI() <= -1 ) {
            visit(n, c, 0, 0);
            if ( m_threshold < 0 )                 
                visitDescendants(n, c);
            visitAncestors(n);
        }
    }
    
    /**
     * Visit a specific node and update its degree-of-interest.
     */
    private void visit(NodeItem n, NodeItem c, int doi, int ldist) {
        PrefuseLib.updateVisible(n, true);
        double localDOI = -ldist / Math.min(1000.0, m_divisor);
        n.setDOI(doi+localDOI);
        
        if ( c != null ) {
            EdgeItem e = (EdgeItem)c.getParentEdge();
            e.setDOI(c.getDOI());
            PrefuseLib.updateVisible(e, true);
        }
    }
    
    /**
     * Visit tree ancestors and their other descendants.
     */
    private void visitAncestors(NodeItem n) {
        if ( n == m_root ) return;
        visitFocus((NodeItem)n.getParent(), n);
    }
    
    /**
     * Traverse tree descendents.
     */
    private void visitDescendants(NodeItem p, NodeItem skip) {
        int lidx = ( skip == null ? 0 : p.getChildIndex(skip) );
        
        Iterator<Node> children = p.children();
        
        p.setExpanded(children.hasNext());
        
        for ( int i=0; children.hasNext(); ++i ) {
            NodeItem c = (NodeItem)children.next();
            if ( c == skip ) { continue; }             
            
            int doi = (int)(p.getDOI()-1);            
            visit(c, c, doi, Math.abs(lidx-i));      
            if ( doi > m_threshold )
                visitDescendants(c, null);   
        }
    }
    
} // end of class FisheyeTreeFilter
