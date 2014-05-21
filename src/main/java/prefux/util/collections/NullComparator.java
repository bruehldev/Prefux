package prefux.util.collections;

import java.util.Comparator;

/**
 * A do-nothing comparator that simply treats all objects as equal.
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class NullComparator<T> implements Comparator<T> {

	public int compare(T o1, T o2) {
		return 0;
	}

} // end of class NullComparator
