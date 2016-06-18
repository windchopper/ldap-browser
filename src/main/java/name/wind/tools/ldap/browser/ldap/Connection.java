package name.wind.tools.ldap.browser.ldap;

import javafx.beans.property.*;
import name.wind.common.fx.util.PropertyUtils;
import name.wind.common.preferences.ObjectCollectionPreferencesEntry;
import name.wind.common.preferences.StructuredPreferencesEntry.StructuredValue;
import name.wind.common.util.Optional;
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

    private static final String VAL__IDENTIFIER = "identifier";
    private static final String VAL__NAME = "name";
    private static final String VAL__HOST = "host";
    private static final String VAL__PORT = "port";
    private static final String VAL__USERNAME = "username";
    private static final String VAL__PASSWORD = "password";
    private static final String VAL__TRANSPORT_SECURITY = "transportSecurity";
    private static final String VAL__AUTH_METHOD = "authMethod";
    private static final String VAL__BASE = "base";

    private static final String TYPE__LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private String identifier;

    public final StringProperty nameProperty = new SimpleStringProperty(this, VAL__NAME);
    public final StringProperty hostProperty = new SimpleStringProperty(this, VAL__HOST);
    public final IntegerProperty portProperty = new SimpleIntegerProperty(this, VAL__PORT);
    public final ObjectProperty<TransportSecurity> transportSecurityProperty = new SimpleObjectProperty<>(this, VAL__TRANSPORT_SECURITY);
    public final ObjectProperty<AuthMethod> authMethodProperty = new SimpleObjectProperty<>(this, VAL__AUTH_METHOD);
    public final ObjectProperty<LdapName> baseProperty = new SimpleObjectProperty<>(this, VAL__BASE);
    public final StringProperty usernameProperty = new SimpleStringProperty(this, VAL__USERNAME);
    public final StringProperty passwordProperty = new SimpleStringProperty(this, VAL__PASSWORD);

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
        identifier = values.get(VAL__IDENTIFIER);

        nameProperty.set(values.get(VAL__NAME));
        hostProperty.set(values.get(VAL__HOST));
        usernameProperty.set(values.get(VAL__USERNAME));
        passwordProperty.set(values.get(VAL__PASSWORD));

        Optional.of(values.get(VAL__PORT))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    portProperty.set(Integer.parseInt(values.get(VAL__PORT)));
                } catch (NumberFormatException ignored) {
                }
            });

        Optional.of(values.get(VAL__TRANSPORT_SECURITY))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    transportSecurityProperty.set(
                        TransportSecurity.valueOf(values.get(VAL__TRANSPORT_SECURITY)));
                } catch (IllegalArgumentException ignored) {
                }
            });

        Optional.of(values.get(VAL__AUTH_METHOD))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    authMethodProperty.set(
                        AuthMethod.valueOf(values.get(VAL__AUTH_METHOD)));
                } catch (IllegalArgumentException ignored) {
                }
            });

        Optional.of(values.get(VAL__BASE))
            .filter(value -> value != null && value.length() > 0)
            .ifPresent(value -> {
                try {
                    baseProperty.set(
                        new LdapName(values.get(VAL__BASE)));
                } catch (InvalidNameException ignored) {
                }
            });
    }

    public void save(Map<String, String> values) {
        values.put(VAL__IDENTIFIER, identifier);
        values.put(VAL__NAME, nameProperty.get());
        values.put(VAL__HOST, hostProperty.get());
        values.put(VAL__PORT, Optional.of(portProperty.get()).map(Object::toString).orElse(null));
        values.put(VAL__USERNAME, usernameProperty.get());
        values.put(VAL__PASSWORD, passwordProperty.get());
        values.put(VAL__TRANSPORT_SECURITY, Optional.of(transportSecurityProperty.get()).map(Enum::name).orElse(null));
        values.put(VAL__AUTH_METHOD, Optional.of(authMethodProperty.get()).map(Enum::name).orElse(null));
        values.put(VAL__BASE, Optional.of(baseProperty.get()).map(Object::toString).orElse(null));
    }

    public static ObjectCollectionPreferencesEntry<Connection, List<Connection>> connectionListPreferencesEntry(Class<?> invoker, String name) {
        return new ObjectCollectionPreferencesEntry<>(
            invoker,
            name,
            ArrayList::new,
            structuredValue -> Value.of(
                    new Connection())
                .with(connection -> connection.load(structuredValue))
                .get(),
            connection -> Value.of(
                    new StructuredValue(connection.identifier))
                .with(connection::save)
                .get()
        );
    }

    public DirContext newDirContext() throws URISyntaxException, NamingException {
        Properties environment = new Properties();

        URI uri = new URI(
            transportSecurityProperty.get().scheme,
            null,
            hostProperty.get(),
            portProperty.get(),
            baseProperty.get().toString(),
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
