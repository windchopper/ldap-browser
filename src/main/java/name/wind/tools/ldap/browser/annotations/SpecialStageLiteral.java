package name.wind.tools.ldap.browser.annotations;

import javax.enterprise.util.AnnotationLiteral;

public class SpecialStageLiteral extends AnnotationLiteral<SpecialStage> implements SpecialStage {

    private final Special value;

    public SpecialStageLiteral(Special value) {
        this.value = value;
    }

    @Override public Special value() {
        return value;
    }

}
