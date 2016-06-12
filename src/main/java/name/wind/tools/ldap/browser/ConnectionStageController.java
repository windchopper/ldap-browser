package name.wind.tools.ldap.browser;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.cdi.NamedStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import static java.util.Arrays.asList;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    private static final Insets defaultInsets = new Insets(4);

    private Scene buildScene() {
        return new Scene(
            Builder.direct(GridPane::new)
                .add(target -> target::getColumnConstraints, asList(
                    Builder.direct(ColumnConstraints::new)
                        .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                        .get(),
                    Builder.direct(ColumnConstraints::new)
                        .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                        .get()))
                .add(gridPane -> gridPane::getChildren, asList(
                    Builder.direct(Label::new)
                        .set(target -> target::setText, messageBundle.bundleString("ConnectionStageController.displayNameLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 0, 2, 1, HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.NEVER, defaultInsets))
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.NEVER, defaultInsets))
                        .get()))
                .get());
    }

    private void start(@Observes @NamedStage("connectionStage") Stage stage) {
        Builder.direct(() -> this.stage = stage)
            .set(target -> target::setTitle, messageBundle.bundleString("ConnectionStageController.title"))
            .set(target -> target::setScene, buildScene())
            .get().show();
    }

}
