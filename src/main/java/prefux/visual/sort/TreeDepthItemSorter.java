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
package prefux.visual.sort;

import prefux.Visualization;
import prefux.visual.AggregateItem;
import prefux.visual.DecoratorItem;
import prefux.visual.EdgeItem;
import prefux.visual.NodeItem;
import prefux.visual.VisualItem;

/**
 * ItemSorter that sorts items by tree depths. By default items deeper
 * in the tree are given lower scores, so that parent nodes are drawn
 * on top of child nodes. This ordering can be reversed using the
 * appropriate constructor arguments.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TreeDepthItemSorter extends ItemSorter {

    protected static final int AGGREGATE = 0;
    protected static final int EDGE      = 1;
    protected static final int ITEM      = 2;
    protected static final int NODE      = 3;
    protected static final int DECORATOR = 4;
    
    private int m_childrenAbove;
    private int m_hover;
    private int m_highlight;
    private int m_depth;
    
    /**
     * Create a new TreeDepthItemSorter that orders nodes such that parents
     * are placed above their children.
     */
    public TreeDepthItemSorter() {
        this(false);
    }
    
    /**
     * Create a new TreeDepthItemSorter with the given sort ordering by depth.
     * @param childrenAbove true if children should be ordered above their
     * parents, false if parents should be ordered above their children.
     */
    public TreeDepthItemSorter(boolean childrenAbove) {
        if ( childrenAbove ) {
            m_childrenAbove = 1;
            m_hover = 13;
            m_highlight = 12;
            m_depth = 14;
        } else {
            m_childrenAbove = -1;
            m_hover = 24;
            m_highlight = 23;
            m_depth = 12;
        }
    }
    
    /**
     * Score items similarly to {@link ItemSorter}, but additionally
     * ranks items based on their tree depth.
     * @see prefux.visual.sort.ItemSorter#score(prefux.visual.VisualItem)
     */
    public int score(VisualItem item) {
        int type = ITEM;
        if ( item instanceof EdgeItem ) {
            type = EDGE;
        } else if ( item instanceof AggregateItem ) {
            type = AGGREGATE;
        } else if ( item instanceof DecoratorItem ) {
            type = DECORATOR;
        }
        
        int score = (1<<(25+type));
        if ( item instanceof NodeItem ) {
            int depth = ((NodeItem)item).getDepth();
            score += m_childrenAbove*(depth<<m_depth);
        }
        if ( item.isHover() ) {
            score += (1<<m_hover);
        }
        if ( item.isHighlighted() ) {
            score += (1<<m_highlight);
        }
        if ( item.isInGroup(Visualization.FOCUS_ITEMS) ) {
            score += (1<<11);
        }
        if ( item.isInGroup(Visualization.SEARCH_ITEMS) ) {
            score += (1<<10);
        }

        return score;
//        int score = 0;
//        if ( item.isHover() ) {
//            score += (1<<m_hover);
//        }
//        if ( item.isHighlighted() ) {
//            score += (1<<m_highlight);
//        }
//        if ( item instanceof NodeItem ) {
//            score += (1<<27); // nodes before edges
//            score += m_childrenAbove*(((NodeItem)item).getDepth()<<m_depth);
//        }
//        if ( item.isInGroup(Visualization.FOCUS_ITEMS) ) {
//            score += (1<<11);
//        }
//        if ( item.isInGroup(Visualization.SEARCH_ITEMS) ) {
//            score += (1<<10);
//        }
//        if ( item instanceof DecoratorItem ) {
//            score += (1<<9);
//        }
//        return score;
    }

} // end of class TreeDepthItemSorter
