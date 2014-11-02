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
/**
 * Copyright (c) 2014 Martin Stockhammer
 * See "LICENSE.txt" for licensing terms.
 */
package prefux.render;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class StackPaneRenderer extends ArrayList<Renderer> implements Renderer,
		List<Renderer> {

	private static final Logger log = LogManager.getLogger(StackPaneRenderer.class);

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
