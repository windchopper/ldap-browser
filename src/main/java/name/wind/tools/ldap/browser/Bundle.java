package name.wind.tools.ldap.browser;

import javax.enterprise.context.ApplicationScoped;
import java.util.ResourceBundle;

@ApplicationScoped public class Bundle {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("name.wind.tools.ldap.browser.i18n.messages");

    public String bundleString(String key, Object... parameters) {
        return String.format(
            resourceBundle.getString(key),
            parameters);
    }

}
