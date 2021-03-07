package name.wind.tools.ldap.browser;

import com.github.windchopper.common.fx.util.PropertyUtils;
import com.github.windchopper.common.util.Pipeliner;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.wind.tools.ldap.browser.events.ConnectionEdit;
import name.wind.tools.ldap.browser.events.ConnectionEditCommitted;
import name.wind.tools.ldap.browser.events.StageConstructed;
import name.wind.tools.ldap.browser.ldap.Connection;
import org.jboss.weld.literal.NamedLiteral;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.ResourceBundle;
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

    private Parent buildSceneRoot() {
        return Pipeliner.of(BorderPane::new)
            .set(target -> target::setCenter, connectionTableView = Pipeliner.of(TableView<Connection>::new)
                .set(target -> target.getSelectionModel()::setSelectionMode, SelectionMode.SINGLE)
                .set(target -> target::setColumnResizePolicy, TableView.CONSTRAINED_RESIZE_POLICY)
                .add(target -> target::getColumns, asList(
                    Pipeliner.of(TableColumn<Connection, String>::new)
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.name"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 50.)
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 50.)
                        .set(target -> target::setCellValueFactory, features -> features.getValue().nameProperty)
                        .get(),
                    Pipeliner.of(TableColumn<Connection, String>::new)
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.host"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 30.)
                        .set(target -> target::setCellValueFactory, features -> features.getValue().hostProperty)
                        .get(),
                    Pipeliner.of(TableColumn<Connection, Number>::new)
                        .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.columns.port"))
                        .set(target -> target::setMaxWidth, Integer.MAX_VALUE * 20.)
                        .set(target -> target::setCellValueFactory, features -> features.getValue().portProperty)
                        .get()))
                .set(target -> target::setContextMenu, Pipeliner.of(ContextMenu::new)
                    .add(target -> target::getItems, asList(
                        connectMenuItem = Pipeliner.of(MenuItem::new)
                            .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.connect"))
                            .get(),
                        Pipeliner.of(SeparatorMenuItem::new)
                            .get(),
                        cloneMenuItem = Pipeliner.of(MenuItem::new)
                            .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.clone"))
                            .set(target -> target::setOnAction, this::clone)
                            .get(),
                        addMenuItem = Pipeliner.of(MenuItem::new)
                            .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.add"))
                            .set(target -> target::setOnAction, this::add)
                            .get(),
                        removeMenuItem = Pipeliner.of(MenuItem::new)
                            .set(target -> target::setText, bundle.getString("ConnectionListStageController.connectionTable.contextMenu.remove"))
                            .set(target -> target::setOnAction, this::remove)
                            .get(),
                        Pipeliner.of(SeparatorMenuItem::new)
                            .get(),
                        propertiesMenuItem = Pipeliner.of(MenuItem::new)
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
                Pipeliner.of(Stage::new)
                    .set(target -> target::initOwner, stage)
                    .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                    .set(target -> target::setResizable, false)
                    .get(),
                StageConstructed.IDENTIFIER__CONNECTION),
            new NamedLiteral(
                StageConstructed.IDENTIFIER__CONNECTION));
    }

    private void clone(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                connectionTableView.getSelectionModel().getSelectedItem().copy()));
    }

    private void add(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                new Connection()));
    }

    private void remove(ActionEvent event) {
        Connection selectedItem = connectionTableView.getSelectionModel().getSelectedItem();
        connectionTableView.getItems().remove(selectedItem);
        preferences.connections.accept(connectionTableView.getItems());
    }

    private void properties(ActionEvent event) {
        openEdit();
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEdit(
                connectionTableView.getSelectionModel().getSelectedItem()));
    }

    private void connectionEditCommited(@Observes ConnectionEditCommitted connectionEditCommitted) {
        Connection committed = connectionEditCommitted.connection();
        Connection existentConnection = connectionTableView.getItems().stream()
            .filter(existent -> PropertyUtils.valuesEquals(existent.nameProperty, committed.nameProperty))
            .findFirst()
            .orElse(null);

        if (existentConnection != null) {
            existentConnection.copyFrom(committed);
        } else {
            connectionTableView.getItems().add(committed);
            preferences.connections.accept(connectionTableView.getItems());
        }
    }

    private void start(@Observes @Named(StageConstructed.IDENTIFIER__CONNECTION_LIST) StageConstructed stageConstructed) {
        super.start(
            stageConstructed.stage(),
            stageConstructed.identifier(),
            stageConstructed.preferredSize());

        Stage connectionListStage = Pipeliner.of(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionListStageController.title"))
            .set(target -> target::setScene, Pipeliner.of(() -> new Scene(buildSceneRoot()))
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
