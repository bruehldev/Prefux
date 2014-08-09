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

import java.util.logging.Logger;

import prefux.data.Tuple;
import prefux.data.expression.AbstractExpression;
import prefux.data.expression.Expression;
import prefux.data.expression.Function;
import prefux.data.expression.ObjectLiteral;

/**
 * Abstract base class for Expression instances dealing with data groups
 * within a Visualization. Maintains an Expression that serves as the
 * paremter to this Function; this Expression should return a valid
 * group name when evaluated on a given Tuple.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class GroupExpression extends AbstractExpression
    implements Function
{
    private static final Logger s_logger
        = Logger.getLogger(GroupExpression.class.getName());

    protected Expression m_group;
    
    /**
     * Create a new GroupExpression.
     */
    protected GroupExpression() {
        m_group = null;
    }
    
    /**
     * Create a new GroupExpression over the given group name.
     * @param group the data group name
     */
    protected GroupExpression(String group) {
        m_group = new ObjectLiteral(group);
    }
    
    /**
     * Evaluate the group name expression for the given Tuple
     * @param t the input Tuple to the group name expression
     * @return the String result of the expression
     */
    protected String getGroup(Tuple t) {
        String group = (String)m_group.get(t);
        if ( group == null ) {
            s_logger.warning("Null group lookup");
        }
        return group;
    }
    
    
    /**
     * Attempts to add the given expression as the group expression.
     * @see prefux.data.expression.Function#addParameter(prefux.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        if ( m_group == null )
            m_group = e;
        else
            throw new IllegalStateException(
               "This function takes only 1 parameter.");
    }

    /**
     * @see prefux.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName()+"("+m_group+")";
    }
    
} // end of class GroupExpression
