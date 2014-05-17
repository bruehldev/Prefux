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
