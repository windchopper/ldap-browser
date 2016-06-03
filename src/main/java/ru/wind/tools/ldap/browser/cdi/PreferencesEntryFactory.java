package ru.wind.tools.ldap.browser.cdi;

import ru.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.annotations.PreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped public class PreferencesEntryFactory {

    @Produces @PreferencesEntry("*") public DoublePreferencesEntry createDoublePreferencesEntry(InjectionPoint injectionPoint) {
        return new DoublePreferencesEntry(
            injectionPoint.getMember().getDeclaringClass(),
            injectionPoint.getAnnotated().getAnnotation(PreferencesEntry.class).value());
    }

}
