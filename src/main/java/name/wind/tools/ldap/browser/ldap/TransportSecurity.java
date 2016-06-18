package name.wind.tools.ldap.browser.ldap;

public enum TransportSecurity {

    NONE("ldap", false),
    SECURED("ldaps", true);

    private final String scheme;
    private final boolean secured;

    TransportSecurity(String scheme, boolean secured) {
        this.scheme = scheme;
        this.secured = secured;
    }

    public String scheme() {
        return scheme;
    }

    public boolean secured() {
        return secured;
    }

}
