package name.wind.tools.ldap.browser;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.cdi.NamedStage;
import name.wind.tools.ldap.browser.cdi.NamedStageLiteral;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@ApplicationScoped public class ConnectionListStageController extends AbstractStageController {

    @Inject private Preferences preferences;

    private boolean selectedConnectionCountMatches(int min, int max) {
        return Optional.ofNullable(lookup("#connectionTableView", TableView.class))
            .map(connectionTableView -> connectionTableView.getSelectionModel().getSelectedIndices().size())
            .map(count -> count >= min && count <= max)
            .orElse(false);
    }

    private void add(ActionEvent event) {
        CDI.current().getBeanManager().fireEvent(
            Builder.direct(Stage::new)
                .set(target -> target::initOwner, stage)
                .set(target -> target::initModality, Modality.APPLICATION_MODAL)
                .set(target -> target::setUserData, "connectionStage")
                .get(),
            new NamedStageLiteral("connectionStage"));
    }

    private Scene buildScene() throws IOException {
        return new Scene(
            Builder.direct(BorderPane::new)
                .set(target -> target::setCenter, Builder.direct(TableView<Connection>::new)
                    .set(target -> target::setId, "connectionTableView")
                    .add(target -> target::getColumns, asList(
                        new TableColumn<Connection, String>("Name"),
                        new TableColumn<Connection, String>("Host")
                    ))
                    .set(target -> target::setContextMenu, Builder.direct(ContextMenu::new)
                        .add(target -> target::getItems, asList(
                            Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.connect"))
                                .accept(target -> target.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not()))
                                .get(),
                            Builder.direct(SeparatorMenuItem::new)
                                .get(),
                            Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.clone"))
                                .accept(target -> target.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not()))
                                .get(),
                            Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.add"))
                                .set(target -> target::setOnAction, this::add)
                                .get(),
                            Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.remove"))
                                .accept(target -> target.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, Integer.MAX_VALUE)).not()))
                                .get(),
                            Builder.direct(SeparatorMenuItem::new)
                                .get(),
                            Builder.direct(MenuItem::new)
                                .set(target -> target::setText, bundle.bundleString("ConnectionListStageController.connectionTable.contextMenu.properties"))
                                .accept(target -> target.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedConnectionCountMatches(1, 1)).not()))
                                .get()))
                        .get())
                    .add(target -> target::getItems, preferences.connections.get())
                    .accept(target -> target.getItems().addListener((Change<? extends Connection> change) -> preferences.connections.accept(
                        change.getList().stream().collect(
                            Collectors.toList()))))
                    .get())
                .get());
    }

    @Override protected void start(@Observes @NamedStage("connectionListStage") Stage stage) throws IOException {
        super.start(stage);
        Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.bundleString("ConnectionListStageController.title"))
            .set(target -> target::setScene, buildScene())
            .get().show();
    }

}
