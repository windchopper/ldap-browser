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
import name.wind.common.fx.binding.UnifiedBidirectionalBinding;
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
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapName;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

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
    private TextField usernameTextField;
    private PasswordField passwordPasswordField;

    @SuppressWarnings("Convert2MethodRef") private Parent buildSceneRoot() {
        return Builder.direct(GridPane::new)
            .set(target -> target::setPadding, insets)
            .add(target -> target::getColumnConstraints, asList(
                Builder.direct(ColumnConstraints::new)
                    .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                    .set(constraints -> constraints::setPercentWidth, 25.)
                    .get(),
                Builder.direct(ColumnConstraints::new)
                    .set(constraints -> constraints::setHgrow, Priority.ALWAYS)
                    .set(constraints -> constraints::setPercentWidth, 25.)
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
                    .accept(target -> GridPane.setConstraints(target, 0, 0, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                nameTextField = Builder.direct(TextField::new)
                    .accept(target -> GridPane.setConstraints(target, 0, 1, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.hostLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 2, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                hostTextField = Builder.direct(TextField::new)
                    .accept(target -> GridPane.setConstraints(target, 0, 3, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.portLabel"))
                    .accept(target -> GridPane.setConstraints(target, 3, 2, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                portSpinner = Builder.direct(() -> new Spinner<Number>())
                    .accept(target -> GridPane.setConstraints(target, 3, 3, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .set(target -> target::setValueFactory, new FlexibleSpinnerValueFactory<>(NumberType.INTEGER, 0, Integer.MAX_VALUE, 0))
                    .get(),

                transportSecurityCheckBox = Builder.direct(CheckBox::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.transportSecurityCheckBox"))
                    .accept(target -> GridPane.setConstraints(target, 0, 4, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),

                Builder.direct(Separator::new)
                    .set(target -> target::setOrientation, Orientation.HORIZONTAL)
                    .accept(target -> GridPane.setConstraints(target, 0, 5, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_CENTER::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),

                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.baseLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 6, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                baseComboBox = Builder.direct(() -> new ComboBox<String>())
                    .set(target -> target::setEditable, true)
                    .accept(target -> GridPane.setConstraints(target, 0, 7, 3, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.fetchBaseButton"))
                    .set(target -> target::setOnAction, this::fetchBase)
                    .accept(target -> GridPane.setConstraints(target, 3, 7, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),

                Builder.direct(Separator::new)
                    .set(target -> target::setOrientation, Orientation.HORIZONTAL)
                    .accept(target -> GridPane.setConstraints(target, 0, 8, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_CENTER::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.authMethodLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 9, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                authMethodComboBox = Builder.direct(() -> new ComboBox<AuthMethod>())
                    .accept(target -> GridPane.setConstraints(target, 0, 10, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .add(target -> target::getItems, Arrays.asList(AuthMethod.values()))
                    .set(target -> target::setConverter, new AuthMethod.Converter())
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.usernameLabel"))
                    .accept(target -> GridPane.setConstraints(target, 0, 11, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                usernameTextField = Builder.direct(TextField::new)
                    .accept(target -> GridPane.setConstraints(target, 0, 12, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Label::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.passwordLabel"))
                    .accept(target -> GridPane.setConstraints(target, 2, 11, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.LEFT_BASELINE::apply)
                    .accept(Fill.NONE::apply)
                    .get(),
                passwordPasswordField = Builder.direct(PasswordField::new)
                    .accept(target -> GridPane.setConstraints(target, 2, 12, 2, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Separator::new)
                    .set(target -> target::setOrientation, Orientation.HORIZONTAL)
                    .accept(target -> GridPane.setConstraints(target, 0, 13, 4, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_CENTER::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.saveButton"))
                    .set(target -> target::setOnAction, this::save)
                    .set(target -> target::setDefaultButton, true)
                    .accept(target -> GridPane.setConstraints(target, 2, 14, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get(),
                Builder.direct(Button::new)
                    .set(target -> target::setText, bundle.getString("ConnectionStageController.cancelButton"))
                    .set(target -> target::setOnAction, this::cancel)
                    .set(target -> target::setCancelButton, true)
                    .accept(target -> GridPane.setConstraints(target, 3, 14, 1, 1))
                    .accept(target -> GridPane.setMargin(target, insets))
                    .accept(Alignment.CENTER_BASELINE::apply)
                    .accept(Fill.HORIZONTAL::apply)
                    .get()))
            .get();
    }

    private void fetchBase(ActionEvent event) {
        try {
            DirContext context = connection.newDirContext();

            try {
                Attributes attributes = context.getAttributes("", new String[] { "namingContexts" });
                NamingEnumeration<? extends Attribute> all = attributes.getAll();

                while (all.hasMoreElements()) {
                    System.out.println(all.next());
                }
            } finally {
                context.close();
            }
        } catch (Exception thrown) {
            thrown.printStackTrace();
        }
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

        new UnifiedBidirectionalBinding<>(
            transportSecurityCheckBox.selectedProperty(),
            connection.transportSecurityProperty,
            flag -> flag ? TransportSecurity.SECURED : TransportSecurity.NONE,
            TransportSecurity::secured);

        authMethodComboBox.valueProperty().bindBidirectional(connection.authMethodProperty);

        new UnifiedBidirectionalBinding<>(
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

        usernameTextField.textProperty().bindBidirectional(connection.usernameProperty);
        passwordPasswordField.textProperty().bindBidirectional(connection.passwordProperty);
    }

    private void start(@Observes @Named(StageConstructed.IDENTIFIER__CONNECTION) StageConstructed stageConstructed) {
        super.start(
            stageConstructed.stage(),
            stageConstructed.identifier(),
            stageConstructed.preferredSize());

        Builder.direct(() -> stage)
            .set(target -> target::setTitle, bundle.getString("ConnectionStageController.title"))
            .set(target -> target::setScene, Builder.direct(() -> new Scene(buildSceneRoot()))
                .add(target -> target::getStylesheets, singletonList("/name/wind/tools/ldap/browser/connectionStage.css"))
                .get())
            .get().show();
    }

}
