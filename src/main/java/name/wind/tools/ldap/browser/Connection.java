package name.wind.tools.ldap.browser;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapName;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

public class Connection {

    private static final String VAL__DISPLAY_NAME = "displayName";
    private static final String VAL__HOST = "host";
    private static final String VAL__PORT = "port";
    private static final String VAL__USERNAME = "username";
    private static final String VAL__PASSWORD = "password";
    private static final String VAL__TRANSPORT_SECURITY = "transportSecurity";
    private static final String VAL__AUTH_METHOD = "authMethod";
    private static final String VAL__BASE = "base";

    private static final String TYPE__LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private String displayName;
    private String host;
    private Number port = 389;
    private String username;
    private String password;
    private TransportSecurity transportSecurity = TransportSecurity.NONE;
    private AuthMethod authMethod = AuthMethod.SIMPLE;
    private LdapName base;

    public void load(Map<String, String> values) {
        displayName = values.get(VAL__DISPLAY_NAME);
        host = values.get(VAL__HOST);

        try {
            port = Integer.parseInt(values.get(VAL__PORT));
        } catch (NumberFormatException ignored) {
        }

        username = values.get(VAL__USERNAME);
        password = values.get(VAL__PASSWORD);

        try {
            transportSecurity = TransportSecurity.valueOf(values.get(VAL__TRANSPORT_SECURITY));
            authMethod = AuthMethod.valueOf(values.get(VAL__AUTH_METHOD));
        } catch (IllegalArgumentException ignored) {
        }

        try {
            base = new LdapName(values.get(VAL__BASE));
        } catch (InvalidNameException ignored) {
        }
    }

    public void save(Map<String, String> values) {
        values.put(VAL__DISPLAY_NAME, displayName);
        values.put(VAL__HOST, host);
        values.put(VAL__PORT, port.toString());
        values.put(VAL__USERNAME, username);
        values.put(VAL__PASSWORD, password);
        values.put(VAL__TRANSPORT_SECURITY, transportSecurity.name());
        values.put(VAL__AUTH_METHOD, authMethod.name());
        values.put(VAL__BASE, base.toString());
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
        environment.setProperty(InitialDirContext.SECURITY_AUTHENTICATION, authMethod.value);
        environment.setProperty(InitialDirContext.SECURITY_PRINCIPAL, username);
        environment.setProperty(InitialDirContext.SECURITY_CREDENTIALS, password);

        return new InitialDirContext(environment);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
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

        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
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
