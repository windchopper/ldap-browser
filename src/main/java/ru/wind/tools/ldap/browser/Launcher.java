package ru.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStageLiteral;

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
        weldContainer.getBeanManager().fireEvent(primaryStage, new PrimaryStageLiteral());
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}