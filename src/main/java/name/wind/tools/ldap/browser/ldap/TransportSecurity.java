package name.wind.tools.ldap.browser.ldap;

public enum TransportSecurity {

    NONE("ldap"),
    SECURED("ldaps");

    final String scheme;

    TransportSecurity(String scheme) {
        this.scheme = scheme;
    }

}
