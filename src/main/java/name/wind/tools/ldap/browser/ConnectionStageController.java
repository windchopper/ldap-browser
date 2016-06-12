package name.wind.tools.ldap.browser;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import name.wind.common.fx.Alignment;
import name.wind.common.fx.Fill;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.cdi.NamedStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static name.wind.tools.ldap.browser.cdi.NamedStage.Name;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    private static final Insets insets = new Insets(4);

    private Scene buildScene() {
        return new Scene(
            Builder.direct(GridPane::new)
                .set(target -> target::setPadding, insets)
                .add(target -> target::getColumnConstraints, asList(
                    Builder.direct(ColumnConstraints::new)
                        .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                        .get(),
                    Builder.direct(ColumnConstraints::new)
                        .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                        .get()))
                .add(gridPane -> gridPane::getChildren, asList(
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.displayNameLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 0, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 0, 1, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.CENTER_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.hostLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 2, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 0, 3, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.CENTER_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.portLabel"))
                        .accept(target -> GridPane.setConstraints(target, 1, 2, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 1, 3, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.CENTER_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get(),
                    Builder.direct(CheckBox::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.transportSecurityCheckBox"))
                        .accept(target -> GridPane.setConstraints(target, 0, 4, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.usernameLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 5, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 0, 6, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.CENTER_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.authMethodLabel"))
                        .accept(target -> GridPane.setConstraints(target, 1, 5, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(ComboBox::new)
                        .accept(target -> GridPane.setConstraints(target, 1, 6, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .add(target -> target::getItems, Arrays.asList(AuthMethod.values()))
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.bundleString("ConnectionStageController.baseLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 7, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(TextField::new)
                        .accept(target -> GridPane.setConstraints(target, 0, 8, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.CENTER_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get()))
                .get());
    }

    @Override protected void start(@Observes @NamedStage(Name.CONNECTION) Stage stage) throws IOException {
        super.start(stage);
        Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.bundleString("ConnectionStageController.title"))
            .set(target -> target::setScene, buildScene())
            .get().show();
    }

}
