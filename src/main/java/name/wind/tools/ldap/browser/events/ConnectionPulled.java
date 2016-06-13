package name.wind.tools.ldap.browser.events;

import name.wind.tools.ldap.browser.ldap.Connection;

public class ConnectionPulled {

    private final Connection connection;

    public ConnectionPulled(Connection connection) {
        this.connection = connection;
    }

    public Connection connection() {
        return connection;
    }

}
