package name.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import name.wind.tools.ldap.browser.events.StageConstructed;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.literal.NamedLiteral;

public class Launcher extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    @Override public void init() throws Exception {
        super.init();
        weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Override public void stop() throws Exception {
        weld.shutdown();
        super.stop();
    }

    @Override public void start(Stage primaryStage) throws Exception {
        weldContainer.getBeanManager().fireEvent(
            new StageConstructed(
                primaryStage,
                StageConstructed.IDENTIFIER__CONNECTION_LIST),
            new NamedLiteral(
                StageConstructed.IDENTIFIER__CONNECTION_LIST));
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}
