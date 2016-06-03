package ru.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.wind.common.fx.builder.*;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.MessageBundle;
import ru.wind.tools.ldap.browser.cdi.PreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.PrimaryStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped public class ConnectionStageController {

    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry connectionStageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry connectionStageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry connectionStageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry connectionStageHeight;

    @Inject private MessageBundle messageBundle;

    /*
     *
     */

    @Inject private Parent buildSceneRoot() {
        return new BorderPaneBuilder(BorderPane::new)
            .center(
                new TableViewBuilder<>(TableView<Connection>::new)
                    .identifier("connectionTableView")
                    .contextMenu(
                        new ContextMenuBuilder(ContextMenu::new)
                            .addAll(
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connection.contextMenu.connect"))
                                    .bindDisableProperty(Bindings.isNotNull(LazyObservable.lookup("connectionTableView/selectionModel/selectedItemProperty()")))
                                    .get(),
                                new MenuItemBuilder(SeparatorMenuItem::new)
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connection.contextMenu.clone"))
                                    .bindDisableProperty(Bindings.isNotNull(connectionTableView.getSelectionModel().selectedItemProperty()))
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connection.contextMenu.add"))
                                    .bindDisableProperty(Bindings.isNotNull(connectionTableView.getSelectionModel().selectedItemProperty()))
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connection.contextMenu.remove"))
                                    .bindDisableProperty(Bindings.isNotNull(connectionTableView.getSelectionModel().selectedItemProperty()))
                                    .get(),
                                new MenuItemBuilder(SeparatorMenuItem::new)
                                    .get(),
                                new MenuItemBuilder(MenuItem::new)
                                    .text(messageBundle.bundleString("ConnectionStageController.connection.contextMenu.properties"))
                                    .bindDisableProperty(Bindings.isNotNull(connectionTableView.getSelectionModel().selectedItemProperty()))
                                    .get())
                            .get()
                    )
                    .get())
            .get();
    }

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
            .title(messageBundle.bundleString("ConnectionStageController.title"))
            .scene(buildSceneRoot())
            .get().show();
    }

}
