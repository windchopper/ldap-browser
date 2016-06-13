package name.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.wind.tools.ldap.browser.events.AfterStageConstructed;
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
            new AfterStageConstructed(
                primaryStage,
                AfterStageConstructed.IDENTIFIER__CONNECTION_LIST,
                new Dimension2D(
                    Screen.getPrimary().getVisualBounds().getWidth() / 2, Screen.getPrimary().getVisualBounds().getHeight() / 2)),
            new NamedLiteral(
                AfterStageConstructed.IDENTIFIER__CONNECTION_LIST));
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}
