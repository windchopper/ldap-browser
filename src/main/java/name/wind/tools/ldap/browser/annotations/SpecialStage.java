package name.wind.tools.ldap.browser.annotations;

import javafx.stage.Screen;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME) @Target({
    FIELD, PARAMETER
}) public @interface SpecialStage {

    enum Special {

        CONNECTION_LIST("connectionListStage", Screen.getPrimary().getVisualBounds().getWidth() / 3, Screen.getPrimary().getVisualBounds().getHeight() / 2),
        CONNECTION("connectionStage", Screen.getPrimary().getVisualBounds().getWidth() / 4, Screen.getPrimary().getVisualBounds().getHeight() / 3);

        private final String preferencesPrefix;
        private final double minWidth;
        private final double minHeight;

        Special(String preferencesPrefix, double minWidth, double minHeight) {
            this.preferencesPrefix = preferencesPrefix;
            this.minWidth = minWidth;
            this.minHeight = minHeight;
        }

        public String preferencesEntryName(String name) {
            return preferencesPrefix.concat(name);
        }

        public double minWidth() {
            return minWidth;
        }

        public double minHeight() {
            return minHeight;
        }

    }

    Special value();

}
