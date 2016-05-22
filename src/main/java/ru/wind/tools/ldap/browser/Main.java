package ru.wind.tools.ldap.browser;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.StageBuilder;
import ru.wind.common.preferences.DoublePreferencesEntry;

import java.util.ResourceBundle;

public class Main extends Application {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("tools.ldap.browser.i18n.messages");

    private static final DoublePreferencesEntry connectionWindowX = new DoublePreferencesEntry(Main.class, "connectionWindowX");
    private static final DoublePreferencesEntry connectionWindowY = new DoublePreferencesEntry(Main.class, "connectionWindowY");
    private static final DoublePreferencesEntry connectionWindowWidth = new DoublePreferencesEntry(Main.class, "connectionWindowWidth");
    private static final DoublePreferencesEntry connectionWindowHeight = new DoublePreferencesEntry(Main.class, "connectionWindowHeight");

    public static void main(String... args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);

        new StageBuilder(() -> primaryStage)
            .preferredLocationAndSize(
                primaryStage.getX(), primaryStage.getY(), Screen.getPrimary().getVisualBounds().getWidth() / 3, Screen.getPrimary().getVisualBounds().getHeight() / 3,
                connectionWindowX, connectionWindowY, connectionWindowWidth, connectionWindowHeight)
            .fixMinSize()
            .title(bundle.getString("tools.ldap.browser.Main.title"))
            .get().show();
    }

}
