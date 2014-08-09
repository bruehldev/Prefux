package prefux.render;

import javafx.scene.paint.Paint;

@FunctionalInterface
public interface FillPainter {
	
	Paint fill(int color);

}
