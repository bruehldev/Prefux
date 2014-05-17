package prefux.data.expression;

import prefux.data.Schema;
import prefux.data.Tuple;
import prefux.util.TypeLib;

/**
 * Expression instance representing an "if then else" clause in the prefux
 * expression language.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class IfExpression extends AbstractExpression {

    private Predicate m_test;
    private Expression m_then;
    private Expression m_else;
    
    /**
     * Create a new IfExpression.
     * @param test the predicate test for the if statement
     * @param thenExpr the expression to evaluate if the test predicate
     * evaluates to true
     * @param elseExpr the expression to evaluate if the test predicate
     * evaluates to false
     */
    public IfExpression(Predicate test,
            Expression thenExpr, Expression elseExpr)
    {
        m_test = test;
        m_then = thenExpr;
        m_else = elseExpr;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Get the test predicate.
     * @return the test predicate
     */
    public Predicate getTestPredicate() {
        return m_test;
    }

    /**
     * Get the then expression
     * @return the then expression
     */
    public Expression getThenExpression() {
        return m_then;
    }

    /**
     * Get the else expression
     * @return the else expression
     */
    public Expression getElseExpression() {
        return m_else;
    }
    
    /**
     * Set the test predicate.
     * @param p the test predicate
     */
    public void setTestPredicate(Predicate p) {
        m_test.removeExpressionListener(this);
        m_test = p;
        if ( hasListeners() ) p.addExpressionListener(this);
        fireExpressionChange();
    }

    /**
     * Set the then expression
     * @param e the then expression to set
     */
    public void setThenExpression(Expression e) {
        m_then.removeExpressionListener(this);
        m_then = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }

    /**
     * Set the else expression
     * @param e the else expression to set
     */
    public void setElseExpression(Expression e) {
        m_else.removeExpressionListener(this);
        m_else = e;
        if ( hasListeners() ) e.addExpressionListener(this);
        fireExpressionChange();
    }
    
    // ------------------------------------------------------------------------    
    
    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        Class type1 = m_then.getType(s);
        Class type2 = m_else.getType(s);
        return TypeLib.getSharedType(type1, type2);
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).get(t);
    }

    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getBoolean(t);
    }

    /**
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getDouble(t);
    }

    /**
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getFloat(t);
    }

    /**
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getInt(t);
    }

    /**
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        return (m_test.getBoolean(t) ? m_then : m_else).getLong(t);
    }

    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.data.expression.Expression#visit(prefux.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
        v.down(); m_test.visit(v); v.up();
        v.down(); m_then.visit(v); v.up();
        v.down(); m_else.visit(v); v.up();
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#addChildListeners()
     */
    protected void addChildListeners() {
        m_test.addExpressionListener(this);
        m_then.addExpressionListener(this);
        m_else.addExpressionListener(this);
    }
    
    /**
     * @see prefux.data.expression.AbstractExpression#removeChildListeners()
     */
    protected void removeChildListeners() {
        m_test.removeExpressionListener(this);
        m_then.removeExpressionListener(this);
        m_else.removeExpressionListener(this);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "IF " + m_test.toString()
            + " THEN " + m_then.toString()
            + " ELSE " + m_else.toString();
    }
    
} // end of class IfExpression
