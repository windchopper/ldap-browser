package ru.wind.tools.ldap.browser.preferences;

import ru.wind.common.preferences.ObjectCollectionPreferencesEntry;
import ru.wind.tools.ldap.browser.Connection;

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
