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
package prefux.action.animate;

import prefux.action.ItemAction;
import prefux.util.ColorLib;
import prefux.util.PrefuseLib;
import prefux.util.collections.CopyOnWriteArrayList;
import prefux.visual.VisualItem;


/**
 * Animator that linearly interpolates between starting and ending colors
 * for VisualItems during an animation. By default, interpolates the three
 * primary color fields: {@link VisualItem#STROKECOLOR stroke color},
 * {@link VisualItem#FILLCOLOR fill color}, and
 * {@link VisualItem#TEXTCOLOR text color}.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ColorAnimator extends ItemAction {

    private static final String[] DEFAULTS = new String[] { 
        VisualItem.STROKECOLOR, VisualItem.FILLCOLOR, 
        VisualItem.TEXTCOLOR };
    
    private CopyOnWriteArrayList m_colorFields;
    
    /**
     * Create a new ColorAnimator that processes all data groups.
     */
    public ColorAnimator() {
        super();
        setColorFields(DEFAULTS);
    }

    /**
     * Create a new ColorAnimator that processes the specified group.
     * @param group the data group to process
     */
    public ColorAnimator(String group) {
        super(group);
        setColorFields(DEFAULTS);
    }

    /**
     * Create a new ColorAnimator that processes the specified group and
     * color field.
     * @param group the data group to process
     * @param field the color field to interpolate
     */
    public ColorAnimator(String group, String field) {
        super(group);
        setColorFields(new String[] {field});
    }
    
    /**
     * Create a new ColorAnimator that processes the specified group and
     * color fields.
     * @param group the data group to process
     * @param fields the color fields to interpolate
     */
    public ColorAnimator(String group, String[] fields) {
        super(group);
        setColorFields(fields);
    }
    
    /**
     * Sets the color fields to interpolate.
     * @param fields the color fields to interpolate
     */
    public void setColorFields(String[] fields) {
        if ( fields == null ) {
            throw new IllegalArgumentException();
        }
        
        if ( m_colorFields == null )
            m_colorFields = new CopyOnWriteArrayList();
        else
            m_colorFields.clear();
        
        for ( int i=0; i<fields.length; ++i ) {
            m_colorFields.add(fields[i]);
            m_colorFields.add(PrefuseLib.getStartField(fields[i]));
            m_colorFields.add(PrefuseLib.getEndField(fields[i]));
        }
    }
    
    /**
     * @see prefux.action.ItemAction#process(prefux.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        if ( m_colorFields == null ) return;
        
        Object[] fields = m_colorFields.getArray();
        for ( int i=0; i<fields.length; i += 3 ) {
            String f  = (String)fields[i];
            String sf = (String)fields[i+1];
            String ef = (String)fields[i+2];
            
            int sc = item.getInt(sf), ec = item.getInt(ef);
            int cc = ColorLib.interp(sc, ec, frac);
            item.setInt(f, cc);
        }
    }

} // end of class ColorAnimator
