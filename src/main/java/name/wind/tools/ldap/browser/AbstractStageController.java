package name.wind.tools.ldap.browser;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import name.wind.common.preferences.DoublePreferencesEntry;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

public abstract class AbstractStageController {

    @Inject protected MessageBundle messageBundle;

    protected Stage stage;

    public void setup(@Observes Stage stage) {
        String identifier = (String) stage.getUserData();

        DoublePreferencesEntry stageX = new DoublePreferencesEntry(AbstractStageController.class, identifier + "X");
        DoublePreferencesEntry stageY = new DoublePreferencesEntry(AbstractStageController.class, identifier + "Y");
        DoublePreferencesEntry stageWidth = new DoublePreferencesEntry(AbstractStageController.class, identifier + "Width");
        DoublePreferencesEntry stageHeight = new DoublePreferencesEntry(AbstractStageController.class, identifier + "Height");

        stage.setX(Optional.ofNullable(stageX.get()).orElse(stage.getX()));
        stage.setY(Optional.ofNullable(stageY.get()).orElse(stage.getY()));
        stage.setWidth(Optional.ofNullable(stageWidth.get()).orElse(stage.getWidth()));
        stage.setHeight(Optional.ofNullable(stageHeight.get()).orElse(stage.getHeight()));

        stage.xProperty().addListener((property, oldX, newX) -> stageX.accept(newX.doubleValue()));
        stage.yProperty().addListener((property, oldY, newY) -> stageY.accept(newY.doubleValue()));
        stage.widthProperty().addListener((property, oldWidth, newWidth) -> stageWidth.accept(newWidth.doubleValue()));
        stage.heightProperty().addListener((property, oldHeight, newHeight) -> stageHeight.accept(newHeight.doubleValue()));
    }

    protected <T extends Node> T lookup(String identifier, Class<T> type) {
        return Optional.ofNullable(stage)
            .map(Window::getScene)
            .map(scene -> scene.lookup(identifier))
            .filter(type::isInstance)
            .map(type::cast)
            .orElse(null);
    }

}
