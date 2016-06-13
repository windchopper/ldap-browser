package name.wind.tools.ldap.browser.ldap;

import javafx.util.StringConverter;

import java.util.ResourceBundle;

public enum AuthMethod {

    NONE("none", false, Bundle.bundle.getString("ConnectionStageController.authMethod.none")),
    SIMPLE("simple", true, Bundle.bundle.getString("ConnectionStageController.authMethod.simple")),
    STRONG("strong", true, Bundle.bundle.getString("ConnectionStageController.authMethod.strong")),
    ;

    private static class Bundle {

        private static final ResourceBundle bundle = ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages");

    }

    public static class Converter extends StringConverter<AuthMethod> {

        @Override public String toString(AuthMethod authMethod) {
            return authMethod.title;
        }

        @Override public AuthMethod fromString(String string) {
            throw new UnsupportedOperationException();
        }

    }

    private final String value;
    private final boolean credentialsNeeded;
    private final String title;

    AuthMethod(String value, boolean credentialsNeeded, String title) {
        this.value = value;
        this.credentialsNeeded = credentialsNeeded;
        this.title = title;
    }

    public String value() {
        return value;
    }

    public boolean credentialsNeeded() {
        return credentialsNeeded;
    }

    public String title() {
        return title;
    }

}
