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

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prefux.data.util.Point2D;
import prefux.util.FxGraphicsLib;
import prefux.visual.VisualItem;

public class CombinedRenderer extends ArrayList<Renderer> implements Renderer {

	private static final Logger log = LogManager.getLogger(CombinedRenderer.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8423310534883356968L;

	@Override
	public void render(Parent g, VisualItem item, boolean bind) {
		if (item.getNode() == null) {
			Group grp = new Group();
			grp.setManaged(false);
			// grp.setAutoSizeChildren(false);
			item.setNode(grp);
			for (Renderer renderer : this) {
				renderer.render(grp, item, false);
			}
			if (bind) {
				Platform.runLater(() -> {
					grp.layoutXProperty().bind(item.xProperty());
					grp.layoutYProperty().bind(item.yProperty());
				});
			}
			FxGraphicsLib.addToParent(g, grp);
		}

	}

	public void render(Parent g, VisualItem item) {
		render(g, item, true);
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
		for (Renderer renderer : this) {
			renderer.addStyle(style);
		}
	}

}
