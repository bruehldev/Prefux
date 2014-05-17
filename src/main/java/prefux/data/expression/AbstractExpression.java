package prefux.data.expression;

import prefux.data.Tuple;
import prefux.data.event.ExpressionListener;
import prefux.util.collections.CopyOnWriteArrayList;

/**
 * Abstract base class for Expression implementations. Provides support for
 * listeners and defaults every Expression evaluation method to an
 * unsupported operation.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractExpression
    implements Expression, ExpressionListener
{
    private CopyOnWriteArrayList m_listeners = new CopyOnWriteArrayList();
    
    /**
     * @see prefux.data.expression.Expression#visit(prefux.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
    }

    /**
     * @see prefux.data.expression.Expression#addExpressionListener(prefux.data.event.ExpressionListener)
     */
    public final void addExpressionListener(ExpressionListener lstnr) {
        if ( !m_listeners.contains(lstnr) ) {
            m_listeners.add(lstnr);
            addChildListeners();
        }
    }
    
    /**
     * @see prefux.data.expression.Expression#removeExpressionListener(prefux.data.event.ExpressionListener)
     */
    public final void removeExpressionListener(ExpressionListener lstnr) {
        m_listeners.remove(lstnr);
        if ( m_listeners.size() == 0 )
            removeChildListeners();
    }

    /**
     * Indicates if any listeners are registered with this Expression.
     * @return true if listeners are registered, false otherwise
     */
    protected final boolean hasListeners() {
        return m_listeners != null && m_listeners.size() > 0;
    }
    
    /**
     * Fire an expression change.
     */
    protected final void fireExpressionChange() {
        Object[] lstnrs = m_listeners.getArray();
        for ( int i=0; i<lstnrs.length; ++i ) {
            ((ExpressionListener)lstnrs[i]).expressionChanged(this);
        }
    }
    
    /**
     * Add child listeners to catch and propagate sub-expression updates.
     */
    protected void addChildListeners() {
        // nothing to do
    }

    /**
     * Remove child listeners for sub-expression updates.
     */
    protected void removeChildListeners() {
        // nothing to do
    }

    /**
     * Relay an expression change event.
     * @see prefux.data.event.ExpressionListener#expressionChanged(prefux.data.expression.Expression)
     */
    public void expressionChanged(Expression expr) {
        fireExpressionChange();
    }
    
    // ------------------------------------------------------------------------
    // Default Implementation
    
    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        throw new UnsupportedOperationException();
    }
    
} // end of abstract class AbstractExpression
