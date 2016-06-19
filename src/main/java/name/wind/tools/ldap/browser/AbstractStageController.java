package name.wind.tools.ldap.browser;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.stage.Stage;
import name.wind.common.fx.CommonProperties;
import name.wind.common.fx.behavior.WindowApplyStoredBoundsBehavior;
import name.wind.common.util.Value;

@SuppressWarnings("WeakerAccess") public abstract class AbstractStageController {

    protected Stage stage;

    protected void start(Stage stage, String identifier, Dimension2D preferredSize) {
        this.stage = stage;

        stage.getProperties().put(CommonProperties.PROPERTY__IDENTIFIER, identifier);
        stage.getProperties().put(CommonProperties.PROPERTY__PREFERRED_SIZE, preferredSize);

        new WindowApplyStoredBoundsBehavior().apply(stage);
    }

    protected <T extends Node> T lookup(String selector, Class<T> type) {
        return Value.of(stage.getScene().lookup(selector))
            .filter(type::isInstance)
            .map(type::cast)
            .orElse(null);
    }

}
