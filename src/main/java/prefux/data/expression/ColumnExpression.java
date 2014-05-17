package prefux.data.expression;

import prefux.data.Schema;
import prefux.data.Tuple;

/**
 * Expression instance that returns the value stored in a Tuple data field.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ColumnExpression extends AbstractExpression implements Predicate {

    protected final String m_field;
    
    /**
     * Create a new ColumnExpression.
     * @param field the column / data field name to use
     */
    public ColumnExpression(String field) {
        m_field = field;
    }
    
    /**
     * Get the column / data field name used by this expression.
     * @return the column / data field name
     */
    public String getColumnName() {
        return m_field;
    }
    
    // ------------------------------------------------------------------------
    // Expression Interface
    
    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return s.getColumnType(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return t.get(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getInt(prefux.data.Tuple)
     */
    public int getInt(Tuple t) {
        return t.getInt(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getLong(prefux.data.Tuple)
     */
    public long getLong(Tuple t) {
        return t.getLong(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getFloat(prefux.data.Tuple)
     */
    public float getFloat(Tuple t) {
        return t.getFloat(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getDouble(prefux.data.Tuple)
     */
    public double getDouble(Tuple t) {
        return t.getDouble(m_field);
    }

    /**
     * @see prefux.data.expression.Expression#getBoolean(prefux.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        return t.getBoolean(m_field);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "["+m_field+"]";
    }
    
} // end of class ColumnExpression
