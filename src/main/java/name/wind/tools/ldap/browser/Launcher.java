package name.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import name.wind.tools.ldap.browser.cdi.NamedStageLiteral;

public class Launcher extends Application {

    private WeldContainer weldContainer;

    @Override public void init() throws Exception {
        super.init();
        weldContainer = new Weld().initialize();
    }

    @Override public void stop() throws Exception {
        weldContainer.close();
        super.stop();
    }

    @Override public void start(Stage primaryStage) throws Exception {
        primaryStage.setUserData("connectionListStage");
        weldContainer.getBeanManager().fireEvent(primaryStage, new NamedStageLiteral("connectionListStage"));
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}
