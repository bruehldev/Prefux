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
package prefux.data.query;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultBoundedRangeModel;

import prefux.util.ui.ValuedRangeModel;

/**
 * Supports an ordered range of arbitrary objects. Designed to support
 * range-based dynamic queries over ordered, but not necessarily numerical,
 * data.
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ObjectRangeModel extends DefaultBoundedRangeModel
    implements ValuedRangeModel
{
    private Object[] m_objects;
    private Map m_ordinal;
    
    /**
     * Create a new ObjectRangeModel with the given objects. The objects are
     * assumed to sorted in ascending order.
     * @param objects the members of this ObjectRangeModel, sorted in ascending
     * order.
     */
    public ObjectRangeModel(Object[] objects) {
        setValueRange(objects);
    }
    
    /**
     * Sets the range model to the given objects. The objects are
     * assumed to sorted in ascending order.
     * @param objects the members of this ObjectRangeModel, sorted in ascending
     * order.
     */
    public void setValueRange(Object[] objects) {
        if ( m_objects != null && objects.length == m_objects.length ) {
            boolean equal = true;
            for ( int i=0; i<objects.length; ++i ) {
                if ( objects[i] != m_objects[i] ) {
                    equal = false; break;
                }
            }
            if ( equal ) return; // early exit, model hasn't changed
        }
        // build a new object array
        m_objects = new Object[objects.length];
        System.arraycopy(objects, 0, m_objects, 0, objects.length);
        
        // build the object->index map
        if ( m_ordinal == null ) {
            m_ordinal = new HashMap();
        } else {
            m_ordinal.clear();
        }
        for ( int i=0; i<objects.length; ++i ) {
            m_ordinal.put(objects[i], new Integer(i));
        }
        setRangeProperties(0, objects.length-1, 0, objects.length-1, false);
    }
    
    /**
     * Return the Object at the given index.
     * @param i the index of the Object
     * @return return the requested Object.
     */
    public Object getObject(int i) {
        return m_objects[i];
    }
    
    
    /**
     * Return the index for a given Object, indicating its order in the range.
     * @param o the Object to lookup.
     * @return the index of the Object in the range model, -1 if the Object is
     * not found in the model.
     */
    public int getIndex(Object o) {
        Integer idx = (Integer)m_ordinal.get(o);
        return (idx==null ? -1 : idx.intValue());
    }
    
    /**
     * @see prefux.util.ui.ValuedRangeModel#getMinValue()
     */
    public Object getMinValue() {
        return m_objects[getMinimum()];
    }
    
    /**
     * @see prefux.util.ui.ValuedRangeModel#getMaxValue()
     */
    public Object getMaxValue() {
        return m_objects[getMaximum()];
    }
    
    /**
     * @see prefux.util.ui.ValuedRangeModel#getLowValue()
     */
    public Object getLowValue() {
        return m_objects[getValue()];
    }
    
    /**
     * @see prefux.util.ui.ValuedRangeModel#getHighValue()
     */
    public Object getHighValue() {
        return m_objects[getValue()+getExtent()];
    }
    
} // end of class ObjectRangeModel
