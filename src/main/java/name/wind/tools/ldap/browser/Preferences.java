package name.wind.tools.ldap.browser;

import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.common.util.Builder;
import name.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.Dependent;
import java.util.HashMap;
import java.util.Map;

@Dependent public class Preferences {

    public final ConnectionListPreferencesEntry connections = new ConnectionListPreferencesEntry(Preferences.class, "connections");

    @SuppressWarnings("Convert2MethodRef") public final Map<String, DoublePreferencesEntry> connectionListStageBounds =
        Builder.direct(() -> new HashMap<String, DoublePreferencesEntry>())
            .set(target -> value -> target.put("x", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageX"))
            .set(target -> value -> target.put("y", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageY"))
            .set(target -> value -> target.put("width", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageWidth"))
            .set(target -> value -> target.put("height", value), new DoublePreferencesEntry(Preferences.class, "connectionListStageHeight"))
            .get();

    @SuppressWarnings("Convert2MethodRef") public final Map<String, DoublePreferencesEntry> connectionStageBounds =
        Builder.direct(() -> new HashMap<String, DoublePreferencesEntry>())
            .set(target -> value -> target.put("x", value), new DoublePreferencesEntry(Preferences.class, "connectionStageX"))
            .set(target -> value -> target.put("y", value), new DoublePreferencesEntry(Preferences.class, "connectionStageY"))
            .set(target -> value -> target.put("width", value), new DoublePreferencesEntry(Preferences.class, "connectionStageWidth"))
            .set(target -> value -> target.put("height", value), new DoublePreferencesEntry(Preferences.class, "connectionStageHeight"))
            .get();

    @SuppressWarnings("Convert2MethodRef") public final Map<String, Map<String, DoublePreferencesEntry>> stageBounds =
        Builder.direct(() -> new HashMap<String, Map<String, DoublePreferencesEntry>>())
            .set(target -> value -> target.put("connectionListStage", value), connectionListStageBounds)
            .set(target -> value -> target.put("connectionStage", value), connectionStageBounds)
            .get();

}
