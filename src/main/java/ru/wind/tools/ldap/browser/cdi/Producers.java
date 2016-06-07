package ru.wind.tools.ldap.browser.cdi;

import ru.wind.common.fx.preferences.RectanglePreferencesEntry;
import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.preferences.ConnectionListPreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.ResourceBundle;

@ApplicationScoped public class Producers {

    @Produces @ApplicationScoped public MessageBundle createMessageBundle() {
        return new MessageBundle(ResourceBundle.getBundle("ru.wind.tools.ldap.browser.i18n.messages"));
    }

    /*
     *
     */

    @Produces @PreferencesEntry("*") public DoublePreferencesEntry createDoublePreferencesEntry(InjectionPoint injectionPoint) {
        return new DoublePreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

    @Produces @PreferencesEntry("*") public RectanglePreferencesEntry createRectanglePreferencesEntry(InjectionPoint injectionPoint) {
        return new RectanglePreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

    @Produces @PreferencesEntry("*") public ConnectionListPreferencesEntry createConnectionSetPreferencesEntry(InjectionPoint injectionPoint) {
        return new ConnectionListPreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

}
