package ru.wind.tools.ldap.browser;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.StageBuilder;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.Messages;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ResourceBundle;

@ApplicationScoped public class ConnectionStageController {

    @Inject @Messages private ResourceBundle bundle;

    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry connectionStageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry connectionStageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry connectionStageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry connectionStageHeight;

    /*
     *
     */

    public void start(@Observes @PrimaryStage Stage primaryStage) {
        new StageBuilder(() -> primaryStage)
            .preferredLocationAndSize(
                primaryStage.getX(),
                primaryStage.getY(),
                Screen.getPrimary().getVisualBounds().getWidth() / 3,
                Screen.getPrimary().getVisualBounds().getHeight() / 3,
                connectionStageX,
                connectionStageY,
                connectionStageWidth,
                connectionStageHeight)
            .fixMinSize()
            .title(bundle.getString("tools.ldap.browser.Main.title"))
            .scene(buildSceneRoot())
            .get().show();
    }

    /*
     *
     */

    private Parent buildSceneRoot() {
        return new BorderPane();
    }

}
