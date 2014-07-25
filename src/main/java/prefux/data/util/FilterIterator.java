package prefux.data.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import prefux.data.Tuple;
import prefux.data.expression.Predicate;

/**
 * Iterator over tuples that filters the output by a given predicate.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FilterIterator<T extends Tuple> implements Iterator<T> {
    
    private Predicate predicate;
    private Iterator<T> tuples;
    private T next;
    
    /**
     * Create a new FilterIterator.
     * @param tuples an iterator over tuples
     * @param p the filter predicate to use
     */
    public FilterIterator(Iterator<T> tuples, Predicate p) {
        this.predicate = p;
        this.tuples = tuples;
        next = advance();
    }
    
    private T advance() {
        while ( tuples.hasNext() ) {
            T t = tuples.next();
            if ( predicate.getBoolean(t) ) {
                return t;
            }
        }
        tuples = null;
        next = null;
        return null;
    }

    /**
     * @see java.util.Iterator#next()
     */
    public T next() {
        if ( !hasNext() ) {
            throw new NoSuchElementException("No more elements");
        }
        T retval = next;
        next = advance();
        return retval;
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return ( tuples != null );
    }
    
    /**
     * Not supported.
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
} // end of class FilterIterator
