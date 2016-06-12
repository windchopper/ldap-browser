package name.wind.tools.ldap.browser.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class PreferencesEntryLiteral extends AnnotationLiteral<PreferencesEntry> implements PreferencesEntry {

    private final String value;

    public PreferencesEntryLiteral(String value) {
        this.value = value;
    }

    @Override public String value() {
        return value;
    }

}
