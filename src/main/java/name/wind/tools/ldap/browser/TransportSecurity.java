package name.wind.tools.ldap.browser;

public enum TransportSecurity {

    NONE("ldap"),
    SECURED("ldaps");

    final String scheme;

    TransportSecurity(String scheme) {
        this.scheme = scheme;
    }

}
