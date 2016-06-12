package name.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Stage;
import name.wind.tools.ldap.browser.annotations.SpecialStage;
import name.wind.tools.ldap.browser.annotations.SpecialStageLiteral;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import static name.wind.tools.ldap.browser.annotations.SpecialStage.Special;

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
        primaryStage.setUserData(Special.CONNECTION_LIST);
        weldContainer.getBeanManager().fireEvent(
            primaryStage, new SpecialStageLiteral(SpecialStage.Special.CONNECTION_LIST));
    }

    /*
     *
     */

    public static void main(String... args) {
        launch(args);
    }

}
