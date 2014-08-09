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
package prefux.action.assignment;

import java.util.logging.Logger;

import prefux.Constants;
import prefux.action.EncoderAction;
import prefux.data.expression.Predicate;
import prefux.data.expression.parser.ExpressionParser;
import prefux.visual.VisualItem;


/**
 * <p>Assignment Action that assigns shape values to VisualItems.
 * Shape values are simple integer codes that indicate to
 * appropriate renderer instances what shape should be drawn. The default
 * list of shape values is included in the {@link prefux.Constants} class,
 * all beginning with the prefix <code>SHAPE</code>. Of course, clients can
 * always create their own shape codes that are handled by a custom Renderer. 
 * </p>
 * 
 * <p>
 * By default, a ShapeAction simply sets each VisualItem to be a
 * rectangle. Clients can change this default value to achieve uniform shape
 * assignment, or can add any number of additional rules for shape assignment.
 * Rules are specified by a Predicate instance which, if returning true, will
 * trigger that rule, causing either the provided shape value or the result of
 * a delegate ShapeAction to be applied. Rules are evaluated in the order in
 * which they are added to the ShapeAction, so earlier rules will have
 * precedence over rules added later.
 * </p>
 * 
 * <p>In addition, subclasses can simply override {@link #getShape(VisualItem)}
 * to achieve custom shape assignment. In some cases, this may be the simplest
 * or most flexible approach.</p>
 * 
 * <p>This Action only sets the shape field of the VisualItem. For this value
 * to have an effect, a renderer instance that takes this shape value
 * into account must be used (e.g., {@link prefux.render.ShapeRenderer}).
 * </p>
 * 
 * <p>To automatically assign shape values based on varying values of a
 * particular data field, consider using the {@link DataShapeAction}.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ShapeAction extends EncoderAction {

    protected int m_defaultShape = Constants.SHAPE_RECTANGLE;
    
    /**
     * Constructor. A default rectangle shape will be used.
     */
    public ShapeAction() {
        super();
    }
    
    /**
     * Constructor. A default rectangle shape will be used.
     * @param group the data group processed by this Action.
     */
    public ShapeAction(String group) {
        super(group);
    }

    /**
     * Constructor with a specified a default shape value.
     * @param group the data group processed by this Action.
     * @param shape the default shape value to use
     */
    public ShapeAction(String group, int shape) {
        super(group);
        m_defaultShape = shape;
    }
    
    /**
     * Returns the default shape value assigned to items.
     * @return the default shape value
     */
    public int getDefaultSize() {
        return m_defaultShape;
    }
    
    /**
     * Sets the default shape value assigned to items. Items will be assigned
     * the default shape if they do not match any registered rules.
     * @param defaultShape the new default shape value
     */
    public void setDefaultShape(int defaultShape) {
        m_defaultShape = defaultShape;
    }
    
    /**
     * Add a shape mapping rule to this ShapeAction. VisualItems that match
     * the provided predicate will be assigned the given shape value (assuming
     * they do not match an earlier rule).
     * @param p the rule Predicate 
     * @param shape the shape value
     */
    public void add(Predicate p, int shape) {
        super.add(p, new Integer(shape));
    }

    /**
     * Add a shape mapping rule to this ShapeAction. VisualItems that match
     * the provided expression will be assigned the given shape value (assuming
     * they do not match an earlier rule). The provided expression String will
     * be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param shape the shape value
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, int shape) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        add(p, shape);       
    }
    
    /**
     * Add a size mapping rule to this ShapeAction. VisualItems that match
     * the provided predicate will be assigned the shape value returned by
     * the given ShapeAction's getSize() method.
     * @param p the rule Predicate 
     * @param f the delegate ShapeAction to use
     */
    public void add(Predicate p, ShapeAction f) {
        super.add(p, f);
    }

    /**
     * Add a shape mapping rule to this ShapeAction. VisualItems that match
     * the provided expression will be assigned the given shape value (assuming
     * they do not match an earlier rule). The provided expression String will
     * be parsed to generate the needed rule Predicate.
     * @param expr the expression String, should parse to a Predicate. 
     * @param f the delegate ShapeAction to use
     * @throws RuntimeException if the expression does not parse correctly or
     * does not result in a Predicate instance.
     */
    public void add(String expr, ShapeAction f) {
        Predicate p = (Predicate)ExpressionParser.parse(expr);
        super.add(p, f);
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * @see prefux.action.ItemAction#process(prefux.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        item.setShape(getShape(item));
    }
    
    /**
     * Returns a shape value for the given item.
     * @param item the item for which to get the shape value
     * @return the shape value for the item
     */
    public int getShape(VisualItem item) {
        Object o = lookup(item);
        if ( o != null ) {
            if ( o instanceof ShapeAction ) {
                return ((ShapeAction)o).getShape(item);
            } else if ( o instanceof Number ) {
                return ((Number)o).intValue();
            } else {
                Logger.getLogger(this.getClass().getName())
                    .warning("Unrecognized Object from predicate chain.");
            }
        }
        return m_defaultShape;   
    }

} // end of class ShapeAction
