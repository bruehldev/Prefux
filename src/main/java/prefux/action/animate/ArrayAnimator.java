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

import java.util.logging.Logger;

import prefux.action.ItemAction;
import prefux.util.PrefuseLib;
import prefux.visual.VisualItem;


/**
 * Animator that inerpolates an array of numerical values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ArrayAnimator extends ItemAction {

    private static final Logger s_logger
        = Logger.getLogger(ArrayAnimator.class.getName());
    
    private String m_field; // the field
    private String m_start; // the start field
    private String m_end;   // the end field
    
    /**
     * Create a new ArrayAnimator that processes the given data group
     * and interpolates arrays in the given data field.
     * @param group the data group to process
     * @param field the data field to interpolate. This should be an
     * interpolated field (have start and end instances as well as
     * the field name itself).
     */
    public ArrayAnimator(String group, String field) {
        super(group);
        m_field = field;
        m_start = PrefuseLib.getStartField(field);
        m_end = PrefuseLib.getEndField(field);
    }
    
    /**
     * @see prefux.action.ItemAction#process(prefux.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        Object o = item.get(m_field);
        if ( o instanceof float[] ) {
            float[] a = (float[])o;
            float[] s = (float[])item.get(m_start);
            float[] e = (float[])item.get(m_end);
            
            float f = (float)frac;
            for ( int i=0; i<a.length; ++i ) {
                if ( Float.isNaN(a[i]) ) break;
                a[i] = s[i] + f*(e[i]-s[i]);
            }
            item.setValidated(false);
        } else if ( o instanceof double[] ) {
            double[] a = (double[])o;
            double[] s = (double[])item.get(m_start);
            double[] e = (double[])item.get(m_end);
            
            for ( int i=0; i<a.length; ++i ) {
                if ( Double.isNaN(a[i]) ) break;
                a[i] = s[i] + frac*(e[i]-s[i]);
            }
            item.setValidated(false);
        } else {
            s_logger.warning("Encountered non-double/non-float array type: "
                    + (o==null ? "null" : o.getClass().getName()));
        }
    }

} // end of class ArrayAnimator
