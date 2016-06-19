package name.wind.tools.ldap.browser;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import name.wind.common.fx.Alignment;
import name.wind.common.fx.Fill;
import name.wind.common.fx.spinner.FlexibleSpinnerValueFactory;
import name.wind.common.fx.spinner.NumberType;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.events.ConnectionEdit;
import name.wind.tools.ldap.browser.events.ConnectionEditCommitted;
import name.wind.tools.ldap.browser.events.StageConstructed;
import name.wind.tools.ldap.browser.ldap.AuthMethod;
import name.wind.tools.ldap.browser.ldap.Connection;
import name.wind.tools.ldap.browser.ldap.TransportSecurity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Named;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.util.*;

import static java.util.Arrays.asList;
import static name.wind.common.fx.binding.UnifiedBidirectionalBinding.bindBidirectional;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages");
    private static final Insets insets = new Insets(4.);

    private Connection connection;
    private Map<String, String> backup = new HashMap<>();

    private TextField nameTextField;
    private TextField hostTextField;
    private Spinner<Number> portSpinner;
    private ComboBox<String> baseComboBox;
    private CheckBox transportSecurityCheckBox;
    private ComboBox<AuthMethod> authMethodComboBox;

    @SuppressWarnings("Convert2MethodRef") private Parent buildSceneRoot() {
        return Builder.direct(GridPane::new)
            .set(target -> target::setPadding, insets)
            .add(target -> target::getColumnConstraints, asList(
                Builder.direct(ColumnConstraints::new)
                    .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                    .set(constraints -> constraints::setPercentWidth, 75.)
                    .get(),
                Builder.direct(ColumnConstraints::new)
                    .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                    .set(constraints -> constraints::setPercentWidth, 25.)
                    .get(),
                Builder.direct(ColumnConstraints::new)
                    .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                    .set(constraints -> constraints::setPercentWidth, 25.)
                    .get()))
            .add(gridPane -> gridPane::getChildren, asList(
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.nameLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 0, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                nameTextField = Builder.direct(TextField::new)
                    .accept(target -> GridPane.setConstraints(target, 0, 1, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.hostLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 2, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                hostTextField = Builder.direct(TextField::new)
                    .set(target -> target::setMinWidth, 20.)
                    .accept(target -> GridPane.setConstraints(target, 0, 3, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.portLabel"))
                    .accept(target -> GridPane.setConstraints(target, 2, 2, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                portSpinner = Builder.direct(() -> new Spinner<Number>())
                    .set(target -> target::setMinWidth, 20.)
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .accept(target -> GridPane.setConstraints(target, 2, 3, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .set(target -> target::setValueFactory, new FlexibleSpinnerValueFactory<>(NumberType.INTEGER, 0, Integer.MAX_VALUE, 0))
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.baseLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 4, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                baseComboBox = Builder.direct(() -> new ComboBox<String>())
                    .set(target -> target::setEditable, true)
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .accept(target -> GridPane.setConstraints(target, 0, 5, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.fetchBaseButton"))
                    .accept(target -> GridPane.setConstraints(target, 2, 5, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                transportSecurityCheckBox = Builder.direct(CheckBox::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.transportSecurityCheckBox"))
                    .accept(target -> GridPane.setConstraints(target, 0, 6, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.authMethodLabel"))
                    .accept(target -> GridPane.setConstraints(target, 1, 6, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.RIGHT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                authMethodComboBox = Builder.direct(() -> new ComboBox<AuthMethod>())
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .accept(target -> GridPane.setConstraints(target, 2, 6, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .add(target -> target::getItems, Arrays.asList(AuthMethod.values()))
                    .set(target -> target::setConverter, new AuthMethod.Converter())
                    .get(),
                Builder.direct(Separator::new)
                    .set(target -> target::setOrientation, Orientation.HORIZONTAL)
                    .accept(target -> GridPane.setConstraints(target, 0, 7, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_CENTER::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.saveButton"))
                    .set(target -> target::setOnAction, this::save)
                    .set(target -> target::setDefaultButton, true)
                    .accept(target -> GridPane.setConstraints(target, 1, 8, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setMaxWidth, Double.POSITIVE_INFINITY)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.cancelButton"))
                    .set(target -> target::setOnAction, this::cancel)
                    .set(target -> target::setCancelButton, true)
                    .accept(target -> GridPane.setConstraints(target, 2, 8, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get()))
            .get();
    }

    private void save(ActionEvent event) {
        CDI.current().getBeanManager().fireEvent(
            new ConnectionEditCommitted(connection));
        stage.close();
    }

    private void cancel(ActionEvent event) {
        connection.load(backup);
        stage.close();
    }

    private void connectionEdit(@Observes ConnectionEdit connectionEdit) {
        connection = connectionEdit.connection();
        connection.save(backup);

        nameTextField.textProperty().bindBidirectional(connection.nameProperty);
        hostTextField.textProperty().bindBidirectional(connection.hostProperty);

        portSpinner.getValueFactory().valueProperty().bindBidirectional(connection.portProperty);

        bindBidirectional(
            transportSecurityCheckBox.selectedProperty(),
            connection.transportSecurityProperty,
            flag -> flag ? TransportSecurity.SECURED : TransportSecurity.NONE,
            TransportSecurity::secured);

        authMethodComboBox.valueProperty().bindBidirectional(connection.authMethodProperty);

        bindBidirectional(
            baseComboBox.valueProperty(),
            connection.baseProperty,
            string -> {
                if (string != null)
                    try {
                        return new LdapName(string);
                    } catch (InvalidNameException ignored) {
                    }
                return null;
            },
            ldapName -> {
                if (ldapName != null)
                    return ldapName.toString();
                return null;
            });
    }

    private void start(@Observes @Named(StageConstructed.IDENTIFIER__CONNECTION) StageConstructed stageConstructed) {
        super.start(
            stageConstructed.stage(),
            stageConstructed.identifier(),
            stageConstructed.preferredSize());

        Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionStageController.title"))
            .set(target -> target::setScene, Builder.direct(() -> new Scene(buildSceneRoot()))
                .add(target -> target::getStylesheets, Collections.singletonList("/name/wind/tools/ldap/browser/connectionStage.css"))
                .get())
            .get().show();
    }

}
