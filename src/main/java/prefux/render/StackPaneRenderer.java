/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class StackPaneRenderer extends ArrayList<Renderer> implements Renderer,
		List<Renderer> {

	private static final Logger log = LoggerFactory
			.getLogger(StackPaneRenderer.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -333317478953441168L;

	public StackPaneRenderer() {
	}

	@Override
	public void render(Parent g, VisualItem item, boolean bind) {
		if (log.isTraceEnabled())
			log.trace("Rendering " + item + " on " + g);
		StackPane pane = new StackPane();
		if (item.getNode() == null)
			item.setNode(pane);
		for (Renderer renderer : this) {
			renderer.render(pane, item, false);
		}
		if (bind) {
			Platform.runLater(()-> {
				pane.layoutXProperty().bind(item.xProperty());
				pane.layoutYProperty().bind(item.yProperty());
			});
		}
		FxGraphicsLib.addToParent(g, pane);
	}

	@Override
	public void render(Parent g, VisualItem item) { 
		render(g,item,true);
	}

	@Override
	public boolean locatePoint(Point2D p, VisualItem item) {
		boolean locate = false;
		for (Renderer renderer : this) {
			locate |= renderer.locatePoint(p, item);
		}
		return locate;
	}

	@Override
	public void addStyle(String style) {
		// TODO Auto-generated method stub

	}

}
