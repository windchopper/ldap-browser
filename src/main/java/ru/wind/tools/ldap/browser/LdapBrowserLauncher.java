package ru.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStageAnnotation;

import javax.enterprise.inject.spi.CDI;

public class LdapBrowserLauncher extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    public static void main(String... args) {
        launch(args);
    }

    @Override public void init() throws Exception {
        super.init();

        weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Override public void start(Stage primaryStage) throws Exception {
        CDI.current().getBeanManager().fireEvent(primaryStage, new PrimaryStageAnnotation());
    }

    @Override public void stop() throws Exception {
        weldContainer.close();
        weld.shutdown();

        super.stop();
    }

}
