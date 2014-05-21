package prefux.util.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator implementation that combines the results of multiple iterators.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class CompositeIterator<T> implements Iterator<T> {

	private List<Iterator<T>> m_iters;
	private int m_cur;

	public CompositeIterator(int size) {
		m_iters = new ArrayList<>();
	}

	public CompositeIterator(Iterator<T> iter1, Iterator<T> iter2) {
		ArrayList<Iterator<T>> myl = new ArrayList<>();
		myl.add(iter1);
		myl.add(iter2);
		init(myl);
	}

	public CompositeIterator(List<Iterator<T>> iters) {
		init(iters);
	}

	private final void init(List<Iterator<T>> iters) {
		m_iters = iters;
		m_cur = 0;
	}

	public void setIterator(int idx, Iterator<T> iter) {
		if (m_iters.size() <= idx) {
			m_iters.add(idx, iter);
		} else {
			m_iters.set(idx, iter);
		}
	}

	/**
	 * Not supported.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		if (hasNext()) {
			return m_iters.get(m_cur).next();
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (m_iters == null)
			return false;

		while (true) {
			if (m_cur >= m_iters.size()) {
				m_iters = null;
				return false;
			}
			if (m_iters.get(m_cur) == null) {
				++m_cur;
			} else if (m_iters.get(m_cur).hasNext()) {
				return true;
			} else {
				++m_cur;
			}
		}
	}

} // end of class CompositeIterator
