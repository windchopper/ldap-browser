package ru.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.config.ConfigurationKey;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStageLiteral;

public class Launcher extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    public static void main(String... args) {
        launch(args);
    }

    @Override public void init() throws Exception {
        super.init();

        weld = new Weld();
        weld.property(ConfigurationKey.PROXY_IGNORE_FINAL_METHODS.get(), "java.util.ResourceBundle");
        weldContainer = weld.initialize();
    }

    @Override public void start(Stage primaryStage) throws Exception {
        weldContainer.getBeanManager().fireEvent(primaryStage, new PrimaryStageLiteral());
    }

    @Override public void stop() throws Exception {
        weldContainer.close();
        super.stop();
    }

}
