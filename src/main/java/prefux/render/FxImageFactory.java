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
package prefux.render;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class FxImageFactory {
	
    protected int m_imageCacheSize = 3000;

	protected Map<String, Image> imageCache =
	        new LinkedHashMap<String, Image>((int) (m_imageCacheSize + 1 / .75F), .75F, true) {
	            /**
				 * 
				 */
				private static final long serialVersionUID = -1965214321238752135L;

				public boolean removeEldestEntry(Map.Entry<String,Image> eldest) {
	                return size() > m_imageCacheSize;
	            }
	        };
	
	public Image getImage(String path) {
		Image result;
		if (!imageCache.containsKey(path)) {
			result = new Image(path);
			imageCache.put(path, result);
		} else {
			result = imageCache.get(path);
		}
		return result;
	}

	public void setMaxImageDimensions(int width, int height) {
		// TODO: Implement it?
		// Ignoring
		
	}

}
