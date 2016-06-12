package name.wind.tools.ldap.browser;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.wind.common.fx.Alignment;
import name.wind.common.fx.Fill;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.annotations.SpecialStage;
import name.wind.tools.ldap.browser.ldap.AuthMethod;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;

import static java.util.Arrays.asList;
import static name.wind.tools.ldap.browser.annotations.SpecialStage.Special;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages");
    private static final Insets insets = new Insets(4);

    @SuppressWarnings("Convert2MethodRef") private Scene buildScene() {
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
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.displayNameLabel"))
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
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.hostLabel"))
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
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.portLabel"))
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
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.transportSecurityCheckBox"))
                        .accept(target -> GridPane.setConstraints(target, 0, 4, 2, 1))
                        .accept(target -> GridPane.setMargin(target, new Insets(4, 4, 40, 4)))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.usernameLabel"))
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
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.authMethodLabel"))
                        .accept(target -> GridPane.setConstraints(target, 1, 5, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(() -> new ComboBox<AuthMethod>())
                        .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                        .accept(target -> GridPane.setConstraints(target, 1, 6, 1, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .add(target -> target::getItems, Arrays.asList(AuthMethod.values()))
                        .set(target -> target::setConverter, new AuthMethod.Converter())
                        .get(),
                    Builder.direct(Label::new)
                        .set(target -> target::setText, bundle.getString("ConnectionStageController.baseLabel"))
                        .accept(target -> GridPane.setConstraints(target, 0, 7, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.NONE::apply)
                        .get(),
                    Builder.direct(ComboBox::new)
                        .set(target -> target::setEditable, true)
                        .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                        .accept(target -> GridPane.setConstraints(target, 0, 8, 2, 1))
                        .accept(target -> GridPane.setMargin(target, insets))
                        .accept(Alignment.LEFT_BASELINE::apply)
                        .accept(Fill.HORIZONTAL::apply)
                        .get()))
                .get());
    }

    private void start(@Observes @SpecialStage(Special.CONNECTION) Stage stage) throws IOException {
        start(stage, new Dimension2D(
            Screen.getPrimary().getVisualBounds().getWidth() / 2,
            Screen.getPrimary().getVisualBounds().getHeight() / 3
        ));

        Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionStageController.title"))
            .set(target -> target::setScene, buildScene())
            .get().show();
    }

}
