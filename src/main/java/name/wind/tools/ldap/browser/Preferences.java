package name.wind.tools.ldap.browser;

import com.github.windchopper.common.preferences.PlatformPreferencesStorage;
import com.github.windchopper.common.preferences.PreferencesEntry;
import com.github.windchopper.common.preferences.PreferencesStorage;
import com.github.windchopper.common.preferences.types.StructuralCollectionType;
import name.wind.tools.ldap.browser.ldap.Connection;
import name.wind.tools.ldap.browser.ldap.ConnectionPreferencesEntryType;

import javax.enterprise.context.Dependent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Dependent public class Preferences {

    final PreferencesStorage storage = new PlatformPreferencesStorage(java.util.prefs.Preferences.userNodeForPackage(Preferences.class));

    public final PreferencesEntry<List<Connection>> connections = new PreferencesEntry<>(storage, "connections",
        new StructuralCollectionType<>(ArrayList::new, new ConnectionPreferencesEntryType()), Duration.ZERO);

}
