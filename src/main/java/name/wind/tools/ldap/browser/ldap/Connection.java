package name.wind.tools.ldap.browser.ldap;

import javafx.beans.property.*;
import name.wind.common.fx.util.PropertyUtils;
import name.wind.common.preferences.ObjectCollectionPreferencesEntry;
import name.wind.common.preferences.StructuredPreferencesEntry.StructuredValue;
import name.wind.common.util.Value;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapName;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Connection implements Cloneable {

    private static final String KEY__IDENTIFIER = "identifier";
    private static final String KEY__NAME = "name";
    private static final String KEY__HOST = "host";
    private static final String KEY__PORT = "port";
    private static final String KEY__USERNAME = "username";
    private static final String KEY__PASSWORD = "password";
    private static final String KEY__TRANSPORT_SECURITY = "transportSecurity";
    private static final String KEY__AUTH_METHOD = "authMethod";
    private static final String KEY__BASE = "base";

    private static final String TYPE__LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private String identifier;

    public final StringProperty nameProperty = new SimpleStringProperty(this, KEY__NAME);
    public final StringProperty hostProperty = new SimpleStringProperty(this, KEY__HOST);
    public final IntegerProperty portProperty = new SimpleIntegerProperty(this, KEY__PORT);
    public final ObjectProperty<TransportSecurity> transportSecurityProperty = new SimpleObjectProperty<>(this, KEY__TRANSPORT_SECURITY);
    public final ObjectProperty<AuthMethod> authMethodProperty = new SimpleObjectProperty<>(this, KEY__AUTH_METHOD);
    public final ObjectProperty<LdapName> baseProperty = new SimpleObjectProperty<>(this, KEY__BASE);
    public final StringProperty usernameProperty = new SimpleStringProperty(this, KEY__USERNAME);
    public final StringProperty passwordProperty = new SimpleStringProperty(this, KEY__PASSWORD);

    /*
     *
     */

    public Connection() {
    }

    public Connection(Connection connection) {
        nameProperty.set(connection.nameProperty.get());
        hostProperty.set(connection.hostProperty.get());
        portProperty.set(connection.portProperty.get());
        transportSecurityProperty.set(connection.transportSecurityProperty.get());
        authMethodProperty.set(connection.authMethodProperty.get());
        baseProperty.set(connection.baseProperty.get());
        usernameProperty.set(connection.usernameProperty.get());
        passwordProperty.set(connection.passwordProperty.get());
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") @Override public Connection clone() {
        return new Connection(this);
    }

    public void load(Map<String, String> values) {
        identifier = values.get(KEY__IDENTIFIER);

        nameProperty.set(values.get(KEY__NAME));
        hostProperty.set(values.get(KEY__HOST));
        usernameProperty.set(values.get(KEY__USERNAME));
        passwordProperty.set(values.get(KEY__PASSWORD));

        Value.of(values.get(KEY__PORT))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    portProperty.set(Integer.parseInt(value));
                } catch (NumberFormatException ignored) {
                }
            });

        Value.of(values.get(KEY__TRANSPORT_SECURITY))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    transportSecurityProperty.set(TransportSecurity.valueOf(value));
                } catch (IllegalArgumentException ignored) {
                }
            });

        Value.of(values.get(KEY__AUTH_METHOD))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    authMethodProperty.set(AuthMethod.valueOf(value));
                } catch (IllegalArgumentException ignored) {
                }
            });

        Value.of(values.get(KEY__BASE))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    baseProperty.set(new LdapName(value));
                } catch (InvalidNameException ignored) {
                }
            });
    }

    public void save(Map<String, String> values) {
        values.put(KEY__IDENTIFIER, identifier);
        values.put(KEY__NAME, nameProperty.get());
        values.put(KEY__HOST, hostProperty.get());
        values.put(KEY__PORT, Value.of(portProperty.get()).map(Object::toString).orElse(null));
        values.put(KEY__USERNAME, usernameProperty.get());
        values.put(KEY__PASSWORD, passwordProperty.get());
        values.put(KEY__TRANSPORT_SECURITY, Value.of(transportSecurityProperty.get()).map(Enum::name).orElse(null));
        values.put(KEY__AUTH_METHOD, Value.of(authMethodProperty.get()).map(Enum::name).orElse(null));
        values.put(KEY__BASE, Value.of(baseProperty.get()).map(Object::toString).orElse(null));
    }

    public static ObjectCollectionPreferencesEntry<Connection, List<Connection>> connectionListPreferencesEntry(Class<?> invoker, String name) {
        return new ObjectCollectionPreferencesEntry<>(
            invoker,
            name,
            ArrayList::new,
            structuredValue -> Value.of(
                    new Connection())
                .ifPresent(connection -> connection.load(structuredValue))
                .get(),
            connection -> Value.of(
                    new StructuredValue(connection.identifier))
                .ifPresent(connection::save)
                .get()
        );
    }

    public DirContext newDirContext() throws URISyntaxException, NamingException {
        Properties environment = new Properties();

        URI uri = new URI(
            transportSecurityProperty.get().scheme(),
            null,
            hostProperty.get(),
            portProperty.get(),
            Value.of(baseProperty.get())
                .map(Object::toString)
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

    public boolean same(Connection connection) {
        return Objects.equals(identifier, connection.identifier);
    }

    @Override public int hashCode() {
        int result = 0;

        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + PropertyUtils.hashCode(nameProperty);
        result = 31 * result + PropertyUtils.hashCode(hostProperty);
        result = 31 * result + PropertyUtils.hashCode(portProperty);
        result = 31 * result + PropertyUtils.hashCode(transportSecurityProperty);
        result = 31 * result + PropertyUtils.hashCode(authMethodProperty);
        result = 31 * result + PropertyUtils.hashCode(baseProperty);
        result = 31 * result + PropertyUtils.hashCode(usernameProperty);
        result = 31 * result + PropertyUtils.hashCode(passwordProperty);

        return result;
    }

    @Override public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Connection connection = (Connection) object;

        return identifier != null ? identifier.equals(connection.identifier) : connection.identifier == null &&
            PropertyUtils.equals(nameProperty, connection.nameProperty) &&
            PropertyUtils.equals(hostProperty, connection.hostProperty) &&
            PropertyUtils.equals(portProperty, connection.portProperty) &&
            PropertyUtils.equals(transportSecurityProperty, connection.transportSecurityProperty) &&
            PropertyUtils.equals(authMethodProperty, connection.authMethodProperty) &&
            PropertyUtils.equals(baseProperty, connection.baseProperty) &&
            PropertyUtils.equals(usernameProperty, connection.usernameProperty) &&
            PropertyUtils.equals(passwordProperty, connection.passwordProperty);
    }

}
