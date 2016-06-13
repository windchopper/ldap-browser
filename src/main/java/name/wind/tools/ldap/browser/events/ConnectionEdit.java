package name.wind.tools.ldap.browser.events;

import name.wind.tools.ldap.browser.ldap.Connection;

public class ConnectionEdit {

    private final Connection connection;

    public ConnectionEdit(Connection connection) {
        this.connection = connection;
    }

    public Connection connection() {
        return connection;
    }

}
