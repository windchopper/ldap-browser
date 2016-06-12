package name.wind.tools.ldap.browser;

import name.wind.common.fx.preferences.RectanglePreferencesEntry;
import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.tools.ldap.browser.cdi.PreferencesEntry;
import name.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.ResourceBundle;

@ApplicationScoped public class Producers {

    @Produces @ApplicationScoped public MessageBundle createMessageBundle() {
        return new MessageBundle(ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages"));
    }

    /*
     *
     */

    @Produces @PreferencesEntry("*") public DoublePreferencesEntry createDoublePreferencesEntry(InjectionPoint injectionPoint) {
        return new DoublePreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getQualifiers().stream().filter(PreferencesEntry.class::isInstance).map(PreferencesEntry.class::cast).findFirst().get().value());
    }

    @Produces @PreferencesEntry("*") public RectanglePreferencesEntry createRectanglePreferencesEntry(InjectionPoint injectionPoint) {
        return new RectanglePreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getQualifiers().stream().filter(PreferencesEntry.class::isInstance).map(PreferencesEntry.class::cast).findFirst().get().value());
    }

    @Produces @PreferencesEntry("*") public ConnectionListPreferencesEntry createConnectionSetPreferencesEntry(InjectionPoint injectionPoint) {
        return new ConnectionListPreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getQualifiers().stream().filter(PreferencesEntry.class::isInstance).map(PreferencesEntry.class::cast).findFirst().get().value());
    }

}
