package ru.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import name.wind.common.fx.builder.*;
import name.wind.common.fx.builder.ContextMenuBuilder;
import name.wind.common.fx.builder.MenuItemBuilder;
import name.wind.common.fx.builder.TableViewBuilder;
import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.common.util.Builder;
import ru.wind.tools.ldap.browser.cdi.NamedStage;
import ru.wind.tools.ldap.browser.cdi.NamedStageLiteral;
import ru.wind.tools.ldap.browser.cdi.PreferencesEntry;
import ru.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped public class ConnectionListStageController extends AbstractStageController {

    @Inject @PreferencesEntry("connectionListStageX") private DoublePreferencesEntry stageX;
    @Inject @PreferencesEntry("connectionListStageY") private DoublePreferencesEntry stageY;
    @Inject @PreferencesEntry("connectionListStageWidth") private DoublePreferencesEntry stageWidth;
    @Inject @PreferencesEntry("connectionListStageHeight") private DoublePreferencesEntry stageHeight;

    @Inject @PreferencesEntry("connections") private ConnectionListPreferencesEntry connections;

    private boolean selectedConnectionCountMatches(int min, int max) {
        return Optional.ofNullable(lookup("#connectionTableView", TableView.class))
            .map(connectionTableView -> connectionTableView.getSelectionModel().getSelectedIndices().size())
            .map(count -> count >= min && count <= max)
            .orElse(false);
    }

    private void add(ActionEvent event) {
        CDI.current().getBeanManager().fireEvent(
            new StageBuilder(Stage::new)
                .owner(parentStage)
                .modality(Modality.APPLICATION_MODAL)
                .get(),
            new NamedStageLiteral("connection"));
    }

    @SuppressWarnings("unchecked") private Scene buildSceneRoot() {
        return new Scene(
            new BorderPaneBuilder(BorderPane::new)
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
                                        .text(messageBundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.connect"))
                                        .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                        .get(),
                                    new MenuItemBuilder(SeparatorMenuItem::new)
                                        .get(),
                                    new MenuItemBuilder(MenuItem::new)
                                        .text(messageBundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.clone"))
                                        .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                        .get(),
                                    new MenuItemBuilder(MenuItem::new)
                                        .text(messageBundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.add"))
                                        .actionHandler(this::add)
                                        .get(),
                                    new MenuItemBuilder(MenuItem::new)
                                        .text(messageBundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.remove"))
                                        .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, Integer.MAX_VALUE)).not())
                                        .get(),
                                    new MenuItemBuilder(SeparatorMenuItem::new)
                                        .get(),
                                    new MenuItemBuilder(MenuItem::new)
                                        .text(messageBundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.properties"))
                                        .bindDisableProperty(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not())
                                        .get())
                                .get())
                        .items()
                        .get())
                .get());
    }

    @Override protected void start(@Observes @NamedStage("connectionList") Stage primaryStage) {
        super.start(primaryStage);

        Builder.direct(() -> primaryStage)
            .set(target -> target::setWidth, Screen.getPrimary().getVisualBounds().getWidth() / 3)
            .set(target -> target::setHeight, Screen.getPrimary().getVisualBounds().getHeight() / 3)
            .transform(target -> target::setMinWidth, Window::getWidth)
            .transform(target -> target::setMinHeight, Window::getHeight)
            .accept(target -> {
                target.xProperty().addListener((property, oldX, newX) -> stageX.accept(newX.doubleValue()));
                target.yProperty().addListener((property, oldY, newY) -> stageY.accept(newY.doubleValue()));
                target.widthProperty().addListener((property, oldWidth, newWidth) -> stageWidth.accept(newWidth.doubleValue()));
                target.heightProperty().addListener((property, oldHeight, newHeight) -> stageHeight.accept(newHeight.doubleValue()));
            })
            .set(target -> target::setTitle, messageBundle.bundleString("ConnectionListStageController.title"))
            .set(target -> target::setScene, buildSceneRoot())
            .get().show();
    }

}
