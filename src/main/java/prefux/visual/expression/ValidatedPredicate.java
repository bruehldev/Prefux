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
package prefux.visual.expression;

import prefux.data.expression.ColumnExpression;
import prefux.data.expression.Expression;
import prefux.data.expression.Function;
import prefux.data.expression.NotPredicate;
import prefux.data.expression.Predicate;
import prefux.visual.VisualItem;

/**
 * Expression that indicates if an item's validated flag is set.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ValidatedPredicate extends ColumnExpression
    implements Predicate, Function
{
    /** Convenience instance for the validated == true case. */
    public static final Predicate TRUE = new ValidatedPredicate();
    /** Convenience instance for the validated == false case. */
    public static final Predicate FALSE = new NotPredicate(TRUE);
    
    /**
     * Create a new ValidatedPredicate.
     */
    public ValidatedPredicate() {
        super(VisualItem.VALIDATED);
    }
    
    /**
     * @see prefux.data.expression.Function#getName()
     */
    public String getName() {
        return "VALIDATED";
    }

    /**
     * @see prefux.data.expression.Function#addParameter(prefux.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        throw new IllegalStateException("This function takes 0 parameters");
    }

    /**
     * @see prefux.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 0;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"()";
    }

} // end of class ValidatedPredicate
