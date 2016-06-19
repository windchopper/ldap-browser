package name.wind.tools.ldap.browser;

import javafx.scene.Node;
import javafx.stage.Stage;
import name.wind.common.fx.CommonProperties;
import name.wind.common.fx.behavior.StageFreezeSizeOnShowingBehavior;
import name.wind.common.fx.behavior.WindowApplyStoredBoundsBehavior;
import name.wind.common.util.Optional;

@SuppressWarnings("WeakerAccess") public abstract class AbstractStageController {

    protected Stage stage;

    protected void start(Stage stage, String identifier) {
        this.stage = stage;
        stage.getProperties().put(CommonProperties.PROPERTY__IDENTIFIER, identifier);
        new WindowApplyStoredBoundsBehavior().apply(stage);
        new StageFreezeSizeOnShowingBehavior().apply(stage);
    }

    protected <T extends Node> T lookup(String selector, Class<T> type) {
        return Optional.of(
                stage.getScene().lookup(selector))
            .filter(type::isInstance)
            .map(type::cast)
            .orElse(null);
    }

}
