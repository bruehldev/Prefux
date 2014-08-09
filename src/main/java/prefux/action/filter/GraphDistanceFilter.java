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
import prefux.data.Tuple;
import prefux.data.expression.Predicate;
import prefux.data.tuple.TupleSet;
import prefux.data.util.BreadthFirstIterator;
import prefux.data.util.FilterIterator;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;
import prefux.visual.expression.InGroupPredicate;

/**
 * Filter Action that sets visible all items within a specified graph distance
 * from a set of focus items; all other items will be set to invisible.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphDistanceFilter extends GroupAction {

    protected int m_distance;
    protected String m_sources;
    protected Predicate m_groupP;
    protected BreadthFirstIterator m_bfs;
   
    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses a graph distance of 1. By default, the
     * {@link prefux.Visualization#FOCUS_ITEMS} group will be used as the
     * source nodes from which to measure the distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     */
    public GraphDistanceFilter(String group) {
        this(group, 1);
    }
    
    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses the given graph distance. By default, the
     * {@link prefux.Visualization#FOCUS_ITEMS} group will be used as the
     * source nodes from which to measure the distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     * @param distance the graph distance within which items will be
     * visible.
     */
    public GraphDistanceFilter(String group, int distance) {
        this(group, Visualization.FOCUS_ITEMS, distance);
    }
    
    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses the given graph distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     * @param sources the group to use as source nodes for measuring
     * graph distance.
     * @param distance the graph distance within which items will be
     * visible.
     */
    public GraphDistanceFilter(String group, String sources, int distance)
    {
        super(group);
        m_sources = sources;
        m_distance = distance;
        m_groupP = new InGroupPredicate(
            PrefuseLib.getGroupName(group, Graph.NODES));
        m_bfs = new BreadthFirstIterator();
    }
    
    /**
     * Return the graph distance threshold used by this filter.
     * @return the graph distance threshold
     */
    public int getDistance() {
        return m_distance;
    }

    /**
     * Set the graph distance threshold used by this filter.
     * @param distance the graph distance threshold to use
     */
    public void setDistance(int distance) {
        m_distance = distance;
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
        // mark the items
        Iterator<VisualItem> items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            item.setDOI(Constants.MINIMUM_DOI);
        }
        
        // set up the graph traversal
        TupleSet src = m_vis.getGroup(m_sources);
        Iterator<Tuple> srcs = new FilterIterator(src.tuples(), m_groupP);
        m_bfs.init(srcs, m_distance, Constants.NODE_AND_EDGE_TRAVERSAL);
        
        // traverse the graph
        while ( m_bfs.hasNext() ) {
            VisualItem item = (VisualItem)m_bfs.next();
            int d = m_bfs.getDepth(item);
            PrefuseLib.updateVisible(item, true);
            item.setDOI(-d);
            item.setExpanded(d < m_distance);
        }
        
        // mark unreached items
        items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = items.next();
            if ( item.getDOI() == Constants.MINIMUM_DOI ) {
                PrefuseLib.updateVisible(item, false);
                item.setExpanded(false);
            }
        }
    }
    
    /**
     * Clears references to graph tuples.  The group and visualization are
     * retained.
     */
    public void reset() {
    	m_bfs = new BreadthFirstIterator();
    }

} // end of class GraphDistanceFilter
