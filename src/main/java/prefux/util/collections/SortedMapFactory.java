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
package prefux.util.collections;

import java.util.Comparator;
import java.util.Date;

import prefux.data.DataTypeException;


/**
 * Factory class that generates the appropriate IntSortedMap implementation
 * given a key data type.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SortedMapFactory {

    public static IntSortedMap getMap(
            Class type, Comparator cmp, boolean unique)
        throws IncompatibleComparatorException
    {
        if ( !comparatorCheck(type, cmp) ) {
            throw new IncompatibleComparatorException();
        }
        
        if ( type.equals(int.class) || type.equals(byte.class) )
        {
            return new IntIntTreeMap((LiteralComparator)cmp, !unique);
        } 
        else if ( type.equals(long.class) || type.isAssignableFrom(Date.class) )
        {
            return new LongIntTreeMap((LiteralComparator)cmp, !unique);
        }
        else if ( type.equals(float.class) )
        {
            return new FloatIntTreeMap((LiteralComparator)cmp, !unique);
        }
        else if ( type.equals(double.class) )
        {
            return new DoubleIntTreeMap((LiteralComparator)cmp, !unique);
        }
        else if ( type.equals(boolean.class) )
        {
            return new BooleanIntBitSetMap();
        }
        else if ( Object.class.isAssignableFrom(type) )
        {
            return new ObjectIntTreeMap(cmp, !unique);
        }
        else {
            throw new DataTypeException(
                    "No map available for the provided type");
        }
    }
    
    public static boolean comparatorCheck(Class type, Comparator cmp) {
        if ( cmp == null )
        {
            return true;
        }
        else if ( type.equals(int.class) )
        {
            if ( !(cmp instanceof LiteralIterator) )
                return false;
            try {
                ((LiteralComparator)cmp).compare(0,0);
                return true;
            } catch ( Exception e ) {
                return false;
            }
        } 
        else if ( type.equals(long.class) )
        {
            if ( !(cmp instanceof LiteralIterator) )
                return false;
            try {
                ((LiteralComparator)cmp).compare(0L,0L);
                return true;
            } catch ( Exception e ) {
                return false;
            }
        }
        else if ( type.equals(float.class) )
        {
            if ( !(cmp instanceof LiteralIterator) )
                return false;
            try {
                ((LiteralComparator)cmp).compare(0.f,0.f);
                return true;
            } catch ( Exception e ) {
                return false;
            }
        }
        else if ( type.equals(double.class) )
        {
            if ( !(cmp instanceof LiteralIterator) )
                return false;
            try {
                ((LiteralComparator)cmp).compare(0.0,0.0);
                return true;
            } catch ( Exception e ) {
                return false;
            }
        }
        else if ( type.equals(boolean.class) )
        {
            if ( !(cmp instanceof LiteralIterator) )
                return false;
            try {
                ((LiteralComparator)cmp).compare(false,false);
                return true;
            } catch ( Exception e ) {
                return false;
            }
        }
        else if ( Object.class.isAssignableFrom(type) )
        {
            return true;
        }
        else {
            throw new DataTypeException(
                    "No comparator available for the provided type");
        }
    }
    
} // end of class SortedMapFactory
