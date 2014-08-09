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
