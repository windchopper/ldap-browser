package ru.wind.tools.ldap.browser;

import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.BorderPaneBuilder;
import ru.wind.common.fx.builder.StageBuilder;
import ru.wind.common.fx.builder.TableViewBuilder;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.Message;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PrimaryStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped public class ConnectionStageController {

    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry connectionStageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry connectionStageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry connectionStageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry connectionStageHeight;

    private TableView<Connection> connectionTableView;

    /*
     *
     */

    private Parent buildSceneRoot() {
        return new BorderPaneBuilder(BorderPane::new)
            .center(
                new TableViewBuilder<>(TableView<Connection>::new)
                    .get())
            .get();
    }

    /*
     *
     */

    public void start(@Observes @PrimaryStage Stage primaryStage, @Message("title") String title) {
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
            .title(title)
            .scene(buildSceneRoot())
            .get().show();
    }

}
