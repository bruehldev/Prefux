package prefux.controls;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import prefux.FxDisplay;
import prefux.data.Table;
import prefux.data.event.EventConstants;
import prefux.data.event.TableListener;
import prefux.visual.VisualItem;

/**
 * Created by Daniel on 10/19/2016.
 */
public class OwnControl extends ControlAdapter implements TableListener {

    private VisualItem activeItem;
    protected boolean resetItem;
    private List<Tooltip> toolTipList = new ArrayList<>();


    private static final Logger log = LogManager.getLogger(DragControl.class);
    private Delta delta = new Delta();

    @Override
    public void tableChanged(Table t, int start, int end, int col, int type) {
        if (activeItem == null || type != EventConstants.UPDATE
                || col != t.getColumnNumber(VisualItem.FIXED))
            return;
        int row = activeItem.getRow();
        if (row >= start && row <= end)
            resetItem = false;
    }

    public void itemEvent(VisualItem item, FxDisplay display, BorderPane root) {
        //VisualItem item = display.findItem(item);
        activeItem = item;
        //  Start Copy
        if (item.get("name") != null) {



            Tooltip mousePositionToolTip = new Tooltip("");
            toolTipList.add(mousePositionToolTip);
            System.out.println("parent"+ display.localToScreen(item.getX(),item.getY()));



            String msg = item.get("name").toString();
            mousePositionToolTip.setGraphicTextGap(0);
            mousePositionToolTip.setText(msg);
            Node node = (Node) item.getNode();


            mousePositionToolTip.show(root.getScene().getWindow(),
                    display.localToScreen(item.getX(),item.getY()).getX() ,
                    display.localToScreen(item.getX(),item.getY()).getY());
            mousePositionToolTip.xProperty().doubleExpression(node.translateXProperty());
            mousePositionToolTip.yProperty().doubleExpression(node.translateYProperty());

            //mousePositionToolTip.xProperty().doubleExpression(((ObservableDoubleValue) display.localToScreen(item.getX(), item.getY())));
            //mousePositionToolTip.yProperty().doubleExpression(((ObservableDoubleValue) display.localToScreen(item.getX(), item.getY())));



        }
        // Copy end
    }

    public void hideToolTips() {
        toolTipList.forEach(Tooltip -> {
            Tooltip.hide();
        });
    }
}
