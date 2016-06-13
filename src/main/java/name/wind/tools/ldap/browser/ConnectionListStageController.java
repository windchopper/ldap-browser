package name.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.events.AfterStageConstructed;
import name.wind.tools.ldap.browser.events.ConnectionPulled;
import name.wind.tools.ldap.browser.ldap.AuthMethod;
import name.wind.tools.ldap.browser.ldap.Connection;
import name.wind.tools.ldap.browser.ldap.TransportSecurity;
import org.jboss.weld.literal.NamedLiteral;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@ApplicationScoped public class ConnectionListStageController extends AbstractStageController {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages");

    @Inject private Preferences preferences;
    private TableView<Connection> connectionTableView;
    private MenuItem connectMenuItem;
    private MenuItem cloneMenuItem;
    private MenuItem addMenuItem;
    private MenuItem removeMenuItem;
    private MenuItem propertiesMenuItem;

    private boolean selectedConnectionCountMatches(int min, int max) {
        return Optional.ofNullable(connectionTableView)
            .map(connectionTableView -> connectionTableView.getSelectionModel().getSelectedIndices().size())
            .map(count -> count >= min && count <= max)
            .orElse(false);
    }

    private void newConnectionStage() {
        CDI.current().getBeanManager().fireEvent(
            new AfterStageConstructed(
                Builder.direct(Stage::new)
                    .set(target -> target::initOwner, stage)
                    .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                    .get(),
                AfterStageConstructed.IDENTIFIER__CONNECTION,
                new Dimension2D(
                    Screen.getPrimary().getVisualBounds().getWidth() / 4, Screen.getPrimary().getVisualBounds().getHeight() / 4)),
            new NamedLiteral(
                AfterStageConstructed.IDENTIFIER__CONNECTION));
    }

    private void clone(ActionEvent event) {
        newConnectionStage();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionPulled(
                Builder.direct(connectionTableView.getSelectionModel().getSelectedItem()::clone)
                    .set(target -> target::setIdentifier, UUID.randomUUID().toString())
                    .get()));
    }

    private void add(ActionEvent event) {
        newConnectionStage();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionPulled(
                Builder.direct(Connection::new)
                    .set(target -> target::setIdentifier, UUID.randomUUID().toString())
                    .set(target -> target::setTransportSecurity, TransportSecurity.NONE)
                    .set(target -> target::setAuthMethod, AuthMethod.SIMPLE)
                    .set(target -> target::setPort, 389)
                    .get()));
    }

    private Scene buildScene() {
        return new Scene(
            Builder.direct(BorderPane::new)
                .set(target -> target::setCenter, connectionTableView = Builder.direct(TableView<Connection>::new)
                    .set(target -> target.getSelectionModel()::setSelectionMode, SelectionMode.SINGLE)
                    .add(target -> target::getColumns, asList(
                        new TableColumn<Connection, String>(bundle.getString("ConnectionListStageController.connectionTable.columns.name")),
                        new TableColumn<Connection, String>(bundle.getString("ConnectionListStageController.connectionTable.columns.host")),
                        new TableColumn<Connection, String>(bundle.getString("ConnectionListStageController.connectionTable.columns.port"))))
                    .set(target -> target::setContextMenu, Builder.direct(ContextMenu::new)
                        .add(target -> target::getItems, asList(
                            connectMenuItem = Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.connect"))
                                .get(),
                            Builder.direct(SeparatorMenuItem::new)
                                .get(),
                            cloneMenuItem = Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.clone"))
                                .set(target -> target::setOnAction, this::clone)
                                .get(),
                            addMenuItem = Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.add"))
                                .set(target -> target::setOnAction, this::add)
                                .get(),
                            removeMenuItem = Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.remove"))
                                .get(),
                            Builder.direct(SeparatorMenuItem::new)
                                .get(),
                            propertiesMenuItem = Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.properties"))
                                .get()))
                        .get())
                    .add(target -> target::getItems, preferences.connections.get())
                    .get())
                .get());
    }

    private void start(@Observes @Named(AfterStageConstructed.IDENTIFIER__CONNECTION_LIST) AfterStageConstructed afterStageConstructed) {
        super.start(
            afterStageConstructed.stage(),
            afterStageConstructed.identifier(),
            afterStageConstructed.preferredSize());

        Stage connectionListStage = Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionListStageController.title"))
            .set(target -> target::setScene, buildScene())
            .get();

        BooleanBinding selectionEmptyBinding = Bindings.isNull(
            connectionTableView.getSelectionModel().selectedItemProperty());
        Stream.of(connectMenuItem, cloneMenuItem, removeMenuItem, propertiesMenuItem).forEach(
            item -> item.disableProperty().bind(selectionEmptyBinding));

        connectionListStage.show();
    }

}
