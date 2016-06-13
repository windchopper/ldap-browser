package name.wind.tools.ldap.browser;

import name.wind.tools.ldap.browser.events.ConnectionPulled;
import name.wind.tools.ldap.browser.ldap.Connection;
import name.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import java.util.List;
import java.util.Optional;

@Dependent public class Preferences {

    public final ConnectionListPreferencesEntry connections = new ConnectionListPreferencesEntry(Preferences.class, "connections");

    private void connectionPulled(@Observes ConnectionPulled connectionPulled) {
        List<Connection> connectionList = connections.get();

        Optional<Connection> existingConnection = connectionList.stream()
            .filter(connection -> connection.getIdentifier().equals(connectionPulled.connection().getIdentifier()))
            .findAny();

        if (existingConnection.isPresent()) {
            existingConnection.get().copyFrom(connectionPulled.connection());
        } else {
            connectionList.add(connectionPulled.connection());
        }

        connections.accept(connectionList);
    }

}
