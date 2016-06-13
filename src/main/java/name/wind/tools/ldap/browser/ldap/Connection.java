package name.wind.tools.ldap.browser.ldap;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapName;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

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
    private String name;
    private String host;
    private Number port;
    private String username;
    private String password;
    private TransportSecurity transportSecurity;
    private AuthMethod authMethod;
    private LdapName base;

    public Connection() {
    }

    public Connection(Connection connection) {
        this();
        copyFrom(connection);
    }

    @Override public Connection clone() {
        return new Connection(this);
    }

    public void copyFrom(Connection that) {
        identifier = that.identifier;
        name = that.name;
        host = that.host;
        port = that.port;
        username = that.username;
        password = that.password;
        transportSecurity = that.transportSecurity;
        authMethod = that.authMethod;
        base = that.base;
    }

    public void load(Map<String, String> values) {
        identifier = values.get(VAL__IDENTIFIER);
        name = values.get(VAL__NAME);
        host = values.get(VAL__HOST);

        if (values.containsKey(VAL__PORT))
            try {
                port = Integer.parseInt(values.get(VAL__PORT));
            } catch (NumberFormatException ignored) {
            }

        username = values.get(VAL__USERNAME);
        password = values.get(VAL__PASSWORD);

        if (values.containsKey(VAL__TRANSPORT_SECURITY))
            try {
                transportSecurity = TransportSecurity.valueOf(values.get(VAL__TRANSPORT_SECURITY));
            } catch (IllegalArgumentException ignored) {
            }

        if (values.containsKey(VAL__AUTH_METHOD))
            try {
                authMethod = AuthMethod.valueOf(values.get(VAL__AUTH_METHOD));
            } catch (IllegalArgumentException ignored) {
            }

        if (values.containsKey(VAL__BASE))
            try {
                base = new LdapName(values.get(VAL__BASE));
            } catch (InvalidNameException ignored) {
            }
    }

    public void save(Map<String, String> values) {
        values.put(VAL__IDENTIFIER, identifier);
        values.put(VAL__NAME, name);
        values.put(VAL__HOST, host);
        values.put(VAL__PORT, port == null ? null : port.toString());
        values.put(VAL__USERNAME, username);
        values.put(VAL__PASSWORD, password);
        values.put(VAL__TRANSPORT_SECURITY, transportSecurity == null ? null : transportSecurity.name());
        values.put(VAL__AUTH_METHOD, authMethod == null ? null : authMethod.name());
        values.put(VAL__BASE, base == null ? null : base.toString());
    }

    public DirContext newDirContext() throws URISyntaxException, NamingException {
        Properties environment = new Properties();

        URI uri = new URI(
            transportSecurity.scheme,
            username,
            host,
            port.intValue(),
            base.toString(),
            null,
            null);

        environment.setProperty(InitialDirContext.INITIAL_CONTEXT_FACTORY, TYPE__LDAP_CONTEXT_FACTORY);
        environment.setProperty(InitialDirContext.PROVIDER_URL, uri.toString());
        environment.setProperty(InitialDirContext.SECURITY_AUTHENTICATION, authMethod.value());

        if (authMethod.credentialsNeeded()) {
            environment.setProperty(InitialDirContext.SECURITY_PRINCIPAL, username);
            environment.setProperty(InitialDirContext.SECURITY_CREDENTIALS, password);
        }

        return new InitialDirContext(environment);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Number getPort() {
        return port;
    }

    public void setPort(Number port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TransportSecurity getTransportSecurity() {
        return transportSecurity;
    }

    public void setTransportSecurity(TransportSecurity transportSecurity) {
        this.transportSecurity = transportSecurity;
    }

    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(AuthMethod authMethod) {
        this.authMethod = authMethod;
    }

    public LdapName getBase() {
        return base;
    }

    public void setBase(LdapName base) {
        this.base = base;
    }

    @Override public int hashCode() {
        int result = 0;

        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (transportSecurity != null ? transportSecurity.hashCode() : 0);
        result = 31 * result + (authMethod != null ? authMethod.hashCode() : 0);
        result = 31 * result + (base != null ? base.hashCode() : 0);

        return result;
    }

    @Override public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Connection that = (Connection) object;

        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (transportSecurity != that.transportSecurity) return false;
        if (authMethod != that.authMethod) return false;
        if (base != null ? !base.equals(that.base) : that.base != null) return false;

        return true;
    }

}