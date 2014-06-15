
package prefux.visual.tuple;

import javafx.application.Application
import javafx.application.Platform;
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.stage.Stage
import prefux.Visualization
import prefux.data.Graph
import prefux.data.io.GraphMLReader
import prefux.visual.VisualItem
import prefux.visual.VisualTable
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

public class TableVisualItemTest extends Specification {

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }

    def setupSpec() {
        Thread t = new Thread("JavaFX Init Thread") {
                    public void run() {
                        Application.launch(AsNonApp.class, new String[0]);
                    }
                };
        t.setDaemon(true);
        t.start();
    }

    @Shared
    TableVisualItem item

    @Shared
    VisualTable vt

    def setup() {
        Graph graph = new GraphMLReader().readGraph("data/socialnet.xml");
        Visualization vis = new Visualization();
        vt = new VisualTable(vis,"test");
        vt.addRow();
        item = new TableVisualItem()
        item.init(vt, graph, 0)
    }

    @Unroll
    def "check fx #prop properties with numbers: #a, #b, #c"() {
        /*
         * Using synchronized to get changes by the fx thread.
         * May lead to errors if the JavaFx run queue has more elements
         */
        
        when:
        item."set${prop}"(a)

        then:
        synchronized(item) {
            item."get${prop}Property"().get()==a
        }

        when:
        vt.setDouble(0, VisualItem."${prop.toUpperCase()}", b)

        then:
        synchronized(item) {
            item."get${prop}Property"().get()==b
        }

        when:
        Double newV=-1.0
        item."get${prop}Property"().addListener({ ObservableValue o,Object oldVal, Object newVal ->
            newV=newVal
        } as ChangeListener<Double>)
        item."set${prop}"(c)

        then:
        synchronized(item) {
            item."get${prop}Property"().get()==c
            newV==c
        }

        where:
        a      |    b    |     c     |   prop
        255.75 |  315.77 |  8885.25  |   "X"
        255.75 |  315.77 |  8885.25  |   "Y"
        255.75 |  315.77 |  8885.25  |   "StartX"
        255.75 |  315.77 |  8885.25  |   "StartY"
        255.75 |  315.77 |  8885.25  |   "EndX"
        255.75 |  315.77 |  8885.25  |   "EndY"
        15.01 |   44.44 |    84.124 |   "X"
        15.01 |   44.44 |    84.124 |   "Y"
        15.01 |   44.44 |    84.124 |   "StartX"
        15.01 |   44.44 |    84.124 |   "StartY"
        15.01 |   44.44 |    84.124 |   "EndX"
        15.01 |   44.44 |    84.124 |   "EndY"
    }
}
