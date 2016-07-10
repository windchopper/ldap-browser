package name.wind.tools.ldap.browser;

import name.wind.common.preferences.store.java.JavaStructuredPreferencesStore;
import name.wind.common.preferences.typical.ObjectCollectionPreferencesEntry;
import name.wind.tools.ldap.browser.ldap.Connection;

import javax.enterprise.context.Dependent;
import java.util.List;

@SuppressWarnings("WeakerAccess") @Dependent public class Preferences {

    public final ObjectCollectionPreferencesEntry<Connection, List<Connection>> connections = Connection.connectionListPreferencesEntry(
        new JavaStructuredPreferencesStore(Preferences.class, "ldapBrowserPreferences"), "connections");

}
