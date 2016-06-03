package ru.wind.tools.ldap.browser.cdi;

import java.util.ResourceBundle;

public class MessageBundle {

    private final ResourceBundle bundle;

    public MessageBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String bundleString(String key, Object... parameters) {
        return String.format(
            bundle.getString(key),
            parameters);
    }

}
