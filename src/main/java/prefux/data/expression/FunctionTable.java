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
package prefux.data.expression;

import java.util.HashMap;

import prefux.visual.expression.GroupSizeFunction;
import prefux.visual.expression.HoverPredicate;
import prefux.visual.expression.InGroupPredicate;
import prefux.visual.expression.QueryExpression;
import prefux.visual.expression.SearchPredicate;
import prefux.visual.expression.ValidatedPredicate;
import prefux.visual.expression.VisiblePredicate;

/**
 * Function table that allows lookup of registered FunctionExpressions
 * by their function name.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FunctionTable {
    
    private FunctionTable() {
        // prevent instantiation
    }
    
    private static HashMap s_functionTable;
    static {
        s_functionTable = new HashMap();
        // tuple functions
        addFunction("ROW", RowFunction.class);
        addFunction("ISNODE", IsNodeFunction.class);
        addFunction("ISEDGE", IsEdgeFunction.class);
        addFunction("DEGREE", DegreeFunction.class);
        addFunction("INDEGREE", InDegreeFunction.class);
        addFunction("OUTDEGREE", OutDegreeFunction.class);
        addFunction("CHILDCOUNT", ChildCountFunction.class);
        addFunction("TREEDEPTH", TreeDepthFunction.class);
        
        // numeric functions
        addFunction("ABS", AbsFunction.class);
        addFunction("ACOS", AcosFunction.class);
        addFunction("ASIN", AsinFunction.class);
        addFunction("ATAN", AtanFunction.class);
        addFunction("ATAN2", Atan2Function.class);
        addFunction("CEIL", CeilFunction.class);
        addFunction("CEILING", CeilFunction.class);
        addFunction("COS", CosFunction.class);
        addFunction("COT", CotFunction.class);
        addFunction("DEGREES", DegreesFunction.class);
        addFunction("E", EFunction.class);
        addFunction("EXP", ExpFunction.class);
        addFunction("FLOOR", FloorFunction.class);
        addFunction("LOG", LogFunction.class);
        addFunction("LOG2", Log2Function.class);
        addFunction("LOG10", Log10Function.class);
        addFunction("MAX", MaxFunction.class);
        addFunction("MIN", MaxFunction.class);
        addFunction("MOD", MaxFunction.class);
        addFunction("PI", PiFunction.class);
        addFunction("POW", PowFunction.class);
        addFunction("POWER", PowFunction.class);
        addFunction("RADIANS", RadiansFunction.class);
        addFunction("RAND", RandFunction.class);
        addFunction("ROUND", RoundFunction.class);
        addFunction("SIGN", SignFunction.class);
        addFunction("SIN", SinFunction.class);
        addFunction("SQRT", SqrtFunction.class);
        addFunction("SUM", SumFunction.class);
        addFunction("TAN", TanFunction.class);
        
        addFunction("SAFELOG10", SafeLog10Function.class);
        addFunction("SAFESQRT", SafeSqrtFunction.class);
        
        // string functions
        addFunction("CAP", CapFunction.class);
        addFunction("CONCAT", ConcatFunction.class);
        addFunction("CONCAT_WS", ConcatWsFunction.class);
        addFunction("FORMAT", FormatFunction.class);
        addFunction("INSERT", RPadFunction.class);
        addFunction("LENGTH", LengthFunction.class);
        addFunction("LOWER", LowerFunction.class);
        addFunction("LCASE", LowerFunction.class);
        addFunction("LEFT", LeftFunction.class);
        addFunction("LPAD", LPadFunction.class);
        addFunction("MID", SubstringFunction.class);
        addFunction("POSITION", PositionFunction.class);
        addFunction("REVERSE", ReverseFunction.class);
        addFunction("REPEAT", RepeatFunction.class);
        addFunction("REPLACE", ReplaceFunction.class);
        addFunction("RIGHT", RightFunction.class);
        addFunction("RPAD", RPadFunction.class);
        addFunction("SPACE", SpaceFunction.class);
        addFunction("SUBSTRING", SubstringFunction.class);
        addFunction("UPPER", UpperFunction.class);
        addFunction("UCASE", UpperFunction.class);
        
        // color functions
        addFunction("RGB", RGBFunction.class);
        addFunction("RGBA", RGBAFunction.class);
        addFunction("GRAY", GrayFunction.class);
        addFunction("HEX", HexFunction.class);
        addFunction("HSB", HSBFunction.class);
        addFunction("HSBA", HSBAFunction.class);
        addFunction("COLORINTERP", ColorInterpFunction.class);
        
        // visualization functions
        addFunction("GROUPSIZE", GroupSizeFunction.class);
        addFunction("HOVER", HoverPredicate.class);
        addFunction("INGROUP", InGroupPredicate.class);
        addFunction("MATCH", SearchPredicate.class);
        addFunction("QUERY", QueryExpression.class);
        addFunction("VISIBLE", VisiblePredicate.class);
        addFunction("VALIDATED", ValidatedPredicate.class);
    }
    
    /**
     * Indicates if a function of the given name is included in the function
     * table.
     * @param name the function name
     * @return true if the function is in the table, false otherwise
     */
    public static boolean hasFunction(String name) {
        return s_functionTable.containsKey(name);
    }
    
    /**
     * Add a function to the function table. It will then become available
     * for use with compiled statements of the prefux expression language.
     * @param name the name of the function. This name must not already
     * be registered in the table, i.e. there is no function overloading.
     * @param type the Class instance of the function itself
     */
    public static void addFunction(String name, Class type) {
        if ( !Function.class.isAssignableFrom(type) ) {
            throw new IllegalArgumentException(
                "Type argument must be a subclass of FunctionExpression.");
        }
        if ( hasFunction(name) ) {
            throw new IllegalArgumentException(
                "Function with that name already exists");
        }
        String lo = name.toLowerCase();
        String hi = name.toUpperCase();
        if ( !name.equals(lo) && !name.equals(hi) )
            throw new IllegalArgumentException(
                "Name can't have mixed case, try \""+hi+"\" instead.");
        s_functionTable.put(lo, type);
        s_functionTable.put(hi, type);
    }
    
    /**
     * Get a new Function instance for the function with the given name.
     * @param name the name of the function to create
     * @return the instantiated Function
     */
    public static Function createFunction(String name) {
        Class type = (Class)s_functionTable.get(name);
        if ( type == null ) {
            throw new IllegalArgumentException(
                    "Unrecognized function name");
        }
        try {
            return (Function)type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

} // end of class FunctionTable
