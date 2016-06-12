package name.wind.tools.ldap.browser.preferences;

import name.wind.common.preferences.ObjectCollectionPreferencesEntry;
import name.wind.tools.ldap.browser.ldap.Connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListPreferencesEntry extends ObjectCollectionPreferencesEntry<Connection, List<Connection>> {

    public ConnectionListPreferencesEntry(Class<?> invoker, String name) {
        super(
            invoker,
            name,
            ArrayList::new,
            structuredValue -> {
                Connection connection = new Connection();
                connection.load(structuredValue);
                return connection;
            },
            connection -> {
                StructuredValue structuredValue = new StructuredValue(connection.getDisplayName());
                connection.save(structuredValue);
                return structuredValue;
            });
    }

}
