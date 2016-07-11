package name.wind.tools.ldap.browser;

import name.wind.common.preferences.ComplexValue;
import name.wind.common.preferences.PreferencesEntry;
import name.wind.common.preferences.store.DefaultComplexPreferencesStorage;
import name.wind.common.preferences.store.PreferencesStorage;
import name.wind.tools.ldap.browser.ldap.Connection;

import javax.enterprise.context.Dependent;
import java.util.List;

@Dependent public class Preferences {

    public final PreferencesStorage<ComplexValue> complexStorage = new DefaultComplexPreferencesStorage("name/wind/tools/ldap/browser/ldapBrowserPreferences");
    public final PreferencesEntry<List<Connection>> connections = Connection.connectionListPreferencesEntry(complexStorage, "connections");

}
