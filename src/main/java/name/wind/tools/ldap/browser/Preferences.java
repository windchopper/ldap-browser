package name.wind.tools.ldap.browser;

import name.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.Dependent;

@Dependent public class Preferences {

    public final ConnectionListPreferencesEntry connections = new ConnectionListPreferencesEntry(Preferences.class, "connections");

}
