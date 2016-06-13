package name.wind.tools.ldap.browser;

import javafx.geometry.Dimension2D;
import javafx.stage.Stage;
import name.wind.common.fx.behavior.CommonProperties;
import name.wind.common.fx.behavior.WindowPreferencedBounds;

public abstract class AbstractStageController {

    protected Stage stage;

    protected void start(Stage stage, String identifier, Dimension2D preferredSize) {
        this.stage = stage;
        stage.getProperties().put(CommonProperties.PROPERTY__IDENTIFIER, identifier);
        stage.getProperties().put(CommonProperties.PROPERTY__WINDOW__PREFERRED_SIZE, preferredSize);
        new WindowPreferencedBounds().apply(stage);
    }

}
