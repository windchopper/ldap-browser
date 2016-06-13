package name.wind.tools.ldap.browser.events;

import name.wind.tools.ldap.browser.ldap.Connection;

public class ConnectionEditCommitted {

    private final Connection connection;

    public ConnectionEditCommitted(Connection connection) {
        this.connection = connection;
    }

    public Connection connection() {
        return connection;
    }

}
