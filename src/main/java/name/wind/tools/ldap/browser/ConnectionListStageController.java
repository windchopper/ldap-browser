package name.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.wind.common.util.Builder;
import name.wind.common.util.ListUtils;
import name.wind.common.util.Optional;
import name.wind.tools.ldap.browser.events.ConnectionEdit;
import name.wind.tools.ldap.browser.events.ConnectionEditCommitted;
import name.wind.tools.ldap.browser.events.StageConstructed;
import name.wind.tools.ldap.browser.ldap.AuthMethod;
import name.wind.tools.ldap.browser.ldap.Connection;
import name.wind.tools.ldap.browser.ldap.TransportSecurity;
import org.jboss.weld.literal.NamedLiteral;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
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

    @SuppressWarnings("Convert2MethodRef") private Parent buildSceneRoot() {
        return Builder.direct(BorderPane::new)
            .set(target -> target::setCenter, connectionTableView = Builder.direct(TableView<Connection>::new)
                .set(target -> target.getSelectionModel()::setSelectionMode, SelectionMode.SINGLE)
                .set(target -> target::setColumnResizePolicy, TableView.CONSTRAINED_RESIZE_POLICY)
                .add(target -> target::getColumns, asList(
                    Builder.direct(() -> new TableColumn<Connection, String>())
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.name"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 50.)
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 50.)
                        .set(target -> target::setCellValueFactory, features -> new ReadOnlyStringWrapper(features.getValue().getName()))
                        .get(),
                    Builder.direct(() -> new TableColumn<Connection, String>())
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.host"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 30.)
                        .set(target -> target::setCellValueFactory, features -> new ReadOnlyStringWrapper(features.getValue().getHost()))
                        .get(),
                    Builder.direct(() -> new TableColumn<Connection, String>())
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.port"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 20.)
                        .set(target -> target::setCellValueFactory, features -> new ReadOnlyStringWrapper(
                            Optional.of(features.getValue().getPort())
                                .map(Object::toString)
                                .orElse("")))
                        .get()))
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
                            .set(target -> target::setOnAction, this::remove)
                            .get(),
                        Builder.direct(SeparatorMenuItem::new)
                            .get(),
                        propertiesMenuItem = Builder.direct(MenuItem::new)
                            .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.properties"))
                            .set(target -> target::setOnAction, this::properties)
                            .get()))
                    .get())
                .add(target -> target::getItems, preferences.connections.get())
                .get())
            .get();
    }

    private void openEdit() {
        CDI.current().getBeanManager().fireEvent(
            new StageConstructed(
                Builder.direct(Stage::new)
                    .set(target -> target::initOwner, stage)
                    .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                    .get(),
                StageConstructed.IDENTIFIER__CONNECTION,
                new Dimension2D(
                    Screen.getPrimary().getVisualBounds().getWidth() / 4, Screen.getPrimary().getVisualBounds().getHeight() / 4)),
            new NamedLiteral(
                StageConstructed.IDENTIFIER__CONNECTION));
    }

    private void clone(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                Builder.direct(connectionTableView.getSelectionModel().getSelectedItem()::clone)
                    .set(target -> target::setIdentifier, UUID.randomUUID().toString())
                    .get()));
    }

    private void add(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                Builder.direct(Connection::new)
                    .set(target -> target::setIdentifier, UUID.randomUUID().toString())
                    .set(target -> target::setTransportSecurity, TransportSecurity.NONE)
                    .set(target -> target::setAuthMethod, AuthMethod.SIMPLE)
                    .set(target -> target::setPort, 389)
                    .get()));
    }

    private void remove(ActionEvent event) {
        preferences.connections.accept(list -> {
            Connection selectedItem = connectionTableView.getSelectionModel().getSelectedItem();

            list.removeIf(existent -> Objects.equals(existent.getIdentifier(), selectedItem.getIdentifier()));
            connectionTableView.getItems().removeIf(item -> Objects.equals(item.getIdentifier(), selectedItem.getIdentifier()));

            return list;
        });
    }

    private void properties(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                connectionTableView.getSelectionModel().getSelectedItem()));
    }

    private void connectionEditCommited(@Observes ConnectionEditCommitted connectionEditCommitted) {
        preferences.connections.accept(list -> {
            persistCommitted(list, connectionEditCommitted.connection());
            return list;
        });
    }

    private void persistCommitted(List<Connection> list, Connection committed) {
        Optional.convert(list.stream()
                .filter(existent -> Objects.equals(existent.getIdentifier(), committed.getIdentifier()))
                .findAny())
            .ifPresent(existent -> {
                existent.copyFrom(committed);
                connectionTableView.getItems().set(
                    ListUtils.indexOf(connectionTableView.getItems(), item -> Objects.equals(
                        item.getIdentifier(), committed.getIdentifier())),
                    committed);
            })
            .ifNotPresent(() -> {
                list.add(committed);
                connectionTableView.getItems().add(committed);
            });
    }

    private void start(@Observes @Named(StageConstructed.IDENTIFIER__CONNECTION_LIST) StageConstructed stageConstructed) {
        super.start(
            stageConstructed.stage(),
            stageConstructed.identifier(),
            stageConstructed.preferredSize());

        Stage connectionListStage = Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionListStageController.title"))
            .set(target -> target::setScene, Builder.direct(() -> new Scene(buildSceneRoot()))
                .add(target -> target::getStylesheets, Collections.singletonList("/name/wind/tools/ldap/browser/connectionListStage.css"))
                .get())
            .get();

        BooleanBinding selectionEmptyBinding = Bindings.isNull(
            connectionTableView.getSelectionModel().selectedItemProperty());
        Stream.of(connectMenuItem, cloneMenuItem, removeMenuItem, propertiesMenuItem).forEach(
            item -> item.disableProperty().bind(selectionEmptyBinding));

        connectionListStage.show();
    }

}
