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
package prefux.data;


/**
 * Tuple sub-interface that represents an edge in a graph structure. Each edge
 * has both a source node and a target node. For directed edges, this
 * distinction indicates the directionality of the edge. For undirected edges
 * this distinction merely reflects the underlying storage of the nodes.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface Edge extends Tuple {

    /**
     * Returns the graph of which this Edge is a member.
     * @return the Graph containing this Edge
     */
    public Graph getGraph();
    
    /**
     * Indicates if this edge is directed or undirected.
     * @return true if directed, false if undirected.
     */
    public boolean isDirected();
    
    /**
     * Returns the first, or source, node upon which this Edge
     * is incident.
     * @return the source Node
     */
    public Node getSourceNode();
    
    /**
     * Returns the second, or target, node upon which this Edge
     * is incident.
     * @return the source Node
     */
    public Node getTargetNode();
    
    /**
     * Given a Node upon which this Edge is incident, the opposite incident
     * Node is returned. Throws an exception if the input node is not incident
     * on this Edge.
     * @param n a Node upon which this Edge is incident
     * @return the other Node touched by this Edge
     */
    public Node getAdjacentNode(Node n);
    
} // end of interface Edge
