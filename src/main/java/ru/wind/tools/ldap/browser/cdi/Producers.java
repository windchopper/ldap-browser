package ru.wind.tools.ldap.browser.cdi;

import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.Launcher;
import ru.wind.tools.ldap.browser.cdi.annotations.Messages;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.ResourceBundle;

@ApplicationScoped public class Producers {

    @Produces @ApplicationScoped @Messages public ResourceBundle createMessagesBundle() {
        return ResourceBundle.getBundle("tools.ldap.browser.i18n.messages");
    }

    @Produces @PreferencesEntry("*") public DoublePreferencesEntry createDoublePreferencesEntry(InjectionPoint injectionPoint) {
        return new DoublePreferencesEntry(Launcher.class, injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

}
