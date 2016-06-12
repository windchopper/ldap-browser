package name.wind.tools.ldap.browser;

import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.Dependent;
import java.util.HashMap;
import java.util.Map;

@Dependent public class Preferences {

    public final ConnectionListPreferencesEntry connections = new ConnectionListPreferencesEntry(Preferences.class, "connections");

    @SuppressWarnings("Convert2MethodRef") public final Map<String, DoublePreferencesEntry> stageBounds =
        Builder.direct(() -> new HashMap<String, DoublePreferencesEntry>())
            .set(target -> value -> target.put("connectionListStageX", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageX"))
            .set(target -> value -> target.put("connectionListStageY", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageY"))
            .set(target -> value -> target.put("connectionListStageWidth", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageWidth"))
            .set(target -> value -> target.put("connectionListStageHeight", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageHeight"))
            .set(target -> value -> target.put("connectionStageX", value), new DoublePreferencesEntry(Preferences.class, "connectionStageX"))
            .set(target -> value -> target.put("connectionStageY", value), new DoublePreferencesEntry(Preferences.class, "connectionStageY"))
            .set(target -> value -> target.put("connectionStageWidth", value), new DoublePreferencesEntry(Preferences.class, "connectionStageWidth"))
            .set(target -> value -> target.put("connectionStageHeight", value), new DoublePreferencesEntry(Preferences.class, "connectionStageHeight"))
            .get();

}
