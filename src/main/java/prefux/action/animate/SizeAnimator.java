package prefux.action.animate;

import prefux.action.ItemAction;
import prefux.visual.VisualItem;

/**
 * Animator that linearly interpolates the size of a VisualItems.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class SizeAnimator extends ItemAction {

    /**
     * Create a new SizeAnimator that processes all data groups.
     */
    public SizeAnimator() {
        super();
    }
    
    /**
     * Create a new SizeAnimator that processes the specified group.
     * @param group the data group to process.
     */
    public SizeAnimator(String group) {
        super(group);
    }

    /**
     * @see prefux.action.ItemAction#process(prefux.visual.VisualItem, double)
     */
    public void process(VisualItem item, double frac) {
        double ss = item.getStartSize();
        item.setSize(ss + frac*(item.getEndSize() - ss));       
    }

} // end of class SizeAnimator
