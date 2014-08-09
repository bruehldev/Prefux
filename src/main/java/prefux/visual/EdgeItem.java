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

import prefux.data.Edge;

/**
 * VisualItem that represents an edge in a graph. This interface combines
 * the {@link VisualItem} interface with the {@link prefux.data.Edge}
 * interface.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface EdgeItem extends VisualItem, Edge {

    /**
     * Get the first, or source, NodeItem upon which this edge is incident.
     * @return the source NodeItem
     */
    public NodeItem getSourceItem();
    
    /**
     * Get the second, or target, NodeItem upon which this edge is incident.
     * @return the target NodeItem
     */
    public NodeItem getTargetItem();
    
    /**
     * Get the NodeItem connected to the given NodeItem by this edge.
     * @param n a NodeItem upon which this edge is incident. If this item
     * is not connected to this edge, a runtime exception will be thrown.
     * @return the other NodeItem connected to this edge
     */
    public NodeItem getAdjacentItem(NodeItem n);
    
} // end of interface EdgeItem
