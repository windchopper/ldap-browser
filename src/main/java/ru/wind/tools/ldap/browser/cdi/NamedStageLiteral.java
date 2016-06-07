package ru.wind.tools.ldap.browser.cdi;

import javax.enterprise.util.AnnotationLiteral;

public class NamedStageLiteral extends AnnotationLiteral<NamedStage> implements NamedStage {

    private final String value;

    public NamedStageLiteral(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

}
