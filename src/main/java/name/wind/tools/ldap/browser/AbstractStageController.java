package name.wind.tools.ldap.browser;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.tools.ldap.browser.annotations.NamedStage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractStageController {

    @Inject protected Bundle bundle;
    @Inject protected Preferences preferences;

    protected Stage stage;

    protected void start(Stage stage) throws IOException {
        this.stage = stage;

        NamedStage.Name name = (NamedStage.Name) stage.getUserData();

        DoublePreferencesEntry stageX = preferences.stageBounds.get(name.preferencesEntryName("X"));
        DoublePreferencesEntry stageY = preferences.stageBounds.get(name.preferencesEntryName("Y"));
        DoublePreferencesEntry stageWidth = preferences.stageBounds.get(name.preferencesEntryName("Width"));
        DoublePreferencesEntry stageHeight = preferences.stageBounds.get(name.preferencesEntryName("Height"));

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
