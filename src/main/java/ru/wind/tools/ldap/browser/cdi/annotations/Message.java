package ru.wind.tools.ldap.browser.cdi.annotations;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({
    FIELD,
    PARAMETER,
    METHOD
}) public @interface Message {
    @Nonbinding String value();
}