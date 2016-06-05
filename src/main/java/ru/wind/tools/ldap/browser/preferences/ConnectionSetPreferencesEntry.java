package ru.wind.tools.ldap.browser.preferences;

import ru.wind.common.preferences.ObjectCollectionPreferencesEntry;
import ru.wind.tools.ldap.browser.Connection;

import java.util.HashSet;
import java.util.Set;

public class ConnectionSetPreferencesEntry extends ObjectCollectionPreferencesEntry<Connection, Set<Connection>> {

    public ConnectionSetPreferencesEntry(Class<?> invoker, String name) {
        super(
            invoker,
            name,
            HashSet::new,
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
