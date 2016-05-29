package ru.wind.tools.ldap.browser;

import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.StageBuilder;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ResourceBundle;

public class LdapBrowser {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("tools.ldap.browser.i18n.messages");

    @Inject @Named("connectionStageSceneRoot") private Parent connectionStageSceneRoot;
    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry connectionStageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry connectionStageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry connectionStageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry connectionStageHeight;

    public void start(@Observes @PrimaryStage Stage primaryStage) {
        new StageBuilder(() -> primaryStage)
            .preferredLocationAndSize(
                primaryStage.getX(), primaryStage.getY(), Screen.getPrimary().getVisualBounds().getWidth() / 3, Screen.getPrimary().getVisualBounds().getHeight() / 3,
                connectionStageX, connectionStageY, connectionStageWidth, connectionStageHeight)
            .fixMinSize()
            .title(bundle.getString("tools.ldap.browser.Main.title"))
            .scene(connectionStageSceneRoot)
            .get().show();
    }

}
