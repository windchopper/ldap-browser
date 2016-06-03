package ru.wind.tools.ldap.browser.cdi;

import ru.wind.tools.ldap.browser.cdi.annotations.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.ResourceBundle;

@ApplicationScoped public class MessageFactory {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("ru.wind.tools.ldap.browser.i18n.messages");

    @Produces @Message("*") public String createMessage(InjectionPoint injectionPoint) {
        return bundle.getString(
            injectionPoint.getMember().getDeclaringClass().getName() + "." +
                injectionPoint.getAnnotated().getAnnotation(Message.class).value());
    }

}
