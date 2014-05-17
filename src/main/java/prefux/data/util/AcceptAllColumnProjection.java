package prefux.data.util;

import prefux.data.column.Column;

/**
 * ColumnProjection that simply includes all columns.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class AcceptAllColumnProjection extends AbstractColumnProjection {

    /**
     * Always returns true, accepting all columns.
     * @see prefux.data.util.ColumnProjection#include(prefux.data.column.Column, java.lang.String)
     */
    public boolean include(Column col, String name) {
        return true;
    }

} // end of class AcceptAllColumnProjection
