package name.wind.tools.ldap.browser;

public enum AuthMethod {

    NONE("none"),
    SIMPLE("simple"),
    STRONG("strong");

    final String value;

    AuthMethod(String value) {
        this.value = value;
    }

}
