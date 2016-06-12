package name.wind.tools.ldap.browser;

import javax.naming.CompoundName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

public class Connection {

    private static final String CONTEXT_IMPL = "com.sun.jndi.ldap.LdapCtxFactory";

    public enum TransportSecurity {

        NONE("ldap"),
        SECURED("ldaps");

        final String scheme;

        TransportSecurity(String scheme) {
            this.scheme = scheme;
        }

    }

    public enum AuthMethod {

        NONE("none"),
        SIMPLE("simple"),
        STRONG("strong");

        final String value;

        AuthMethod(String value) {
            this.value = value;
        }

    }

    private String displayName;
    private String host;
    private Number port;
    private String username;
    private String password;
    private TransportSecurity transportSecurity;
    private AuthMethod authMethod;
    private Name base;

    public void load(Map<String, String> values) {
        displayName = values.get("displayName");
        host = values.get("host");
        port = Integer.parseInt(values.get("port"));
        username = values.get("username");
        password = values.get("password");
        transportSecurity = TransportSecurity.valueOf(values.get("transportSecurity"));
        authMethod = AuthMethod.valueOf(values.get("authMethod"));
        try {
            base = new CompoundName(values.get("base"), null); // todo
        } catch (InvalidNameException ignored) {
        }
    }

    public void save(Map<String, String> values) {

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

        environment.setProperty(InitialDirContext.INITIAL_CONTEXT_FACTORY, CONTEXT_IMPL);
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

    public Name getBase() {
        return base;
    }

    public void setBase(Name base) {
        this.base = base;
    }

}
