package name.wind.tools.ldap.browser.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class NamedStageLiteral extends AnnotationLiteral<NamedStage> implements NamedStage {

    private final Name value;

    public NamedStageLiteral(Name value) {
        this.value = value;
    }

    @Override public Name value() {
        return value;
    }

}
