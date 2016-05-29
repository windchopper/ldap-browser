package ru.wind.tools.ldap.browser;

import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

public class LdapBrowserPreferences {

    @Produces @Singleton @PreferencesEntry("any") public DoublePreferencesEntry createDoublePreferencesEntry(InjectionPoint injectionPoint) {
        return new DoublePreferencesEntry(
            LdapBrowserPreferences.class,
            injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

}
