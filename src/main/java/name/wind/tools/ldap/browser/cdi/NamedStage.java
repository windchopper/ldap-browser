package name.wind.tools.ldap.browser.cdi;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME) @Target({
    FIELD, PARAMETER
}) public @interface NamedStage {

    enum Name {

        CONNECTION_LIST("connectionListStage"),
        CONNECTION("connectionStage");

        private final String preferencesPrefix;

        Name(String preferencesPrefix) {
            this.preferencesPrefix = preferencesPrefix;
        }

        public String preferencesEntryName(String name) {
            return preferencesPrefix.concat(name);
        }

    }

    Name value();

}
