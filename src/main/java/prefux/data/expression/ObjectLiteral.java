package prefux.data.expression;

import prefux.data.Schema;
import prefux.data.Tuple;

/**
 * Literal expression of an Object value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ObjectLiteral extends Literal {

    private final Object m_value;
    
    /**
     * Create a new ObjectLiteral.
     * @param value the literal value
     */
    public ObjectLiteral(Object value) {
        m_value = value;
    }
    
    // ------------------------------------------------------------------------
    // Expression Interface
    
    /**
     * @see prefux.data.expression.Expression#getType(prefux.data.Schema)
     */
    public Class getType(Schema s) {
        return m_value==null ? Object.class : m_value.getClass();
    }

    /**
     * @see prefux.data.expression.Expression#get(prefux.data.Tuple)
     */
    public Object get(Tuple t) {
        return m_value;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if ( m_value == null ) {
            return "NULL";
        } else {
            return "'"+m_value.toString()+"'";
        }
    }

} // end of class ObjectLiteral
