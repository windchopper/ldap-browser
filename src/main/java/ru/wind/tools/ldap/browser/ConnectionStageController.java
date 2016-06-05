package ru.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.*;
import ru.wind.common.fx.builder.ContextMenuBuilder;
import ru.wind.common.fx.builder.MenuItemBuilder;
import ru.wind.common.fx.builder.TableViewBuilder;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.MessageBundle;
import ru.wind.tools.ldap.browser.cdi.PreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.PrimaryStage;
import ru.wind.tools.ldap.browser.preferences.ConnectionSetPreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry connectionStageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry connectionStageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry connectionStageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry connectionStageHeight;

    @Inject @PreferencesEntry("connections") private ConnectionSetPreferencesEntry connections;

    @Inject private MessageBundle messageBundle;

    private boolean selectedConnectionCountMatches(int min, int max) {
        return Optional.ofNullable(lookup("#connectionTableView", TableView.class))
            .map(connectionTableView -> connectionTableView.getSelectionModel().getSelectedIndices().size())
            .map(count -> count >= min && count <= max)
            .orElse(false);
    }

    @SuppressWarnings("unchecked") private Parent buildSceneRoot(Stage stage) {
        return new BorderPaneBuilder(BorderPane::new)
            .center(
                new TableViewBuilder<>(TableView<Connection>::new)
                    .identifier("connectionTableView")
                    .columns(
                        new TableColumn<Connection, String>("Name"),
                        new TableColumn<Connection, String>("Host"))
                    .contextMenu(
                        new ContextMenuBuilder(ContextMenu::new)
                            .addAll(
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connectionTable.contextMenu.connect"))
                                    .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                    .get(),
                                new MenuItemBuilder(SeparatorMenuItem::new)
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connectionTable.contextMenu.clone"))
                                    .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connectionTable.contextMenu.add"))
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connectionTable.contextMenu.remove"))
                                    .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, Integer.MAX_VALUE)).not())
                                    .get(),
                                new MenuItemBuilder(SeparatorMenuItem::new)
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connectionTable.contextMenu.properties"))
                                    .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                    .get())
                            .get())
                    .items()
                    .get())
            .get();
    }

    protected void start(@Observes @PrimaryStage Stage primaryStage) {
        super.start(primaryStage);

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
            .title(messageBundle.bundleString("ConnectionStageController.title"))
            .scene(buildSceneRoot(primaryStage))
            .get().show();
    }

}
