package name.wind.tools.ldap.browser.ldap;

import com.github.windchopper.common.fx.util.PropertyUtils;
import javafx.beans.property.*;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapName;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Properties;

public class Connection implements Cloneable {

    public static final int DEFAULT_PORT = 389;
    public static final TransportSecurity DEFAULT_TRANSPORT_SECURITY = TransportSecurity.NONE;
    public static final AuthMethod DEFAULT_AUTH_METHOD = AuthMethod.NONE;

    private static final String TYPE__LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    /*
     *
     */

    public final StringProperty nameProperty = new SimpleStringProperty();
    public final StringProperty hostProperty = new SimpleStringProperty();
    public final IntegerProperty portProperty = new SimpleIntegerProperty();
    public final ObjectProperty<TransportSecurity> transportSecurityProperty = new SimpleObjectProperty<>();
    public final ObjectProperty<AuthMethod> authMethodProperty = new SimpleObjectProperty<>();
    public final ObjectProperty<LdapName> baseProperty = new SimpleObjectProperty<>();
    public final StringProperty usernameProperty = new SimpleStringProperty();
    public final StringProperty passwordProperty = new SimpleStringProperty();

    /*
     *
     */

    public Connection(String name, String host, int port, TransportSecurity transportSecurity, AuthMethod authMethod, LdapName base, String username, String password) {
        nameProperty.set(name);
        hostProperty.set(host);
        portProperty.set(port);
        transportSecurityProperty.set(transportSecurity);
        authMethodProperty.set(authMethod);
        baseProperty.set(base);
        usernameProperty.set(username);
        passwordProperty.set(password);
    }

    public Connection() {
        this(null, null, DEFAULT_PORT, DEFAULT_TRANSPORT_SECURITY,
            DEFAULT_AUTH_METHOD, null, null, null);
    }

    public Connection(Connection connection) {
        copyFrom(connection);
    }

    public Connection copy() {
        return null;
    }

    public void copyFrom(Connection connection) {
        nameProperty.set(connection.nameProperty.get());
        hostProperty.set(connection.hostProperty.get());
        portProperty.set(connection.portProperty.get());
        transportSecurityProperty.set(connection.transportSecurityProperty.get());
        authMethodProperty.set(connection.authMethodProperty.get());
        baseProperty.set(connection.baseProperty.get());
        usernameProperty.set(connection.usernameProperty.get());
        passwordProperty.set(connection.passwordProperty.get());
    }

    public DirContext newDirContext() throws URISyntaxException, NamingException {
        Properties environment = new Properties();

        URI uri = new URI(
            transportSecurityProperty.get().scheme(),
            null,
            hostProperty.get(),
            portProperty.get(),
            Optional.ofNullable(baseProperty.get())
                .map(LdapName::toString)
                .orElse(null),
            null,
            null);

        environment.setProperty(InitialDirContext.INITIAL_CONTEXT_FACTORY, TYPE__LDAP_CONTEXT_FACTORY);
        environment.setProperty(InitialDirContext.PROVIDER_URL, uri.toString());
        environment.setProperty(InitialDirContext.SECURITY_AUTHENTICATION, authMethodProperty.get().value());

        if (authMethodProperty.get().credentialsNeeded()) {
            environment.setProperty(InitialDirContext.SECURITY_PRINCIPAL, usernameProperty.get());
            environment.setProperty(InitialDirContext.SECURITY_CREDENTIALS, passwordProperty.get());
        }

        return new InitialDirContext(environment);
    }

    @Override public int hashCode() {
        int result = 0;

        result = 31 * result + PropertyUtils.valueHashCode(nameProperty);
        result = 31 * result + PropertyUtils.valueHashCode(hostProperty);
        result = 31 * result + PropertyUtils.valueHashCode(portProperty);
        result = 31 * result + PropertyUtils.valueHashCode(transportSecurityProperty);
        result = 31 * result + PropertyUtils.valueHashCode(authMethodProperty);
        result = 31 * result + PropertyUtils.valueHashCode(baseProperty);
        result = 31 * result + PropertyUtils.valueHashCode(usernameProperty);
        result = 31 * result + PropertyUtils.valueHashCode(passwordProperty);

        return result;
    }

    @Override public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Connection connection = (Connection) object;

        return
            PropertyUtils.valuesEquals(nameProperty, connection.nameProperty) &&
            PropertyUtils.valuesEquals(hostProperty, connection.hostProperty) &&
            PropertyUtils.valuesEquals(portProperty, connection.portProperty) &&
            PropertyUtils.valuesEquals(transportSecurityProperty, connection.transportSecurityProperty) &&
            PropertyUtils.valuesEquals(authMethodProperty, connection.authMethodProperty) &&
            PropertyUtils.valuesEquals(baseProperty, connection.baseProperty) &&
            PropertyUtils.valuesEquals(usernameProperty, connection.usernameProperty) &&
            PropertyUtils.valuesEquals(passwordProperty, connection.passwordProperty);
    }

}
