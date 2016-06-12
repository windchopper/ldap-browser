package name.wind.tools.ldap.browser;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.stage.Stage;
import name.wind.common.preferences.DoublePreferencesEntry;
import name.wind.tools.ldap.browser.annotations.SpecialStage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractStageController {

    @Inject protected Preferences preferences;
    protected Stage stage;

    protected void start(Stage stage, Dimension2D preferredSize) throws IOException {
        this.stage = stage;

        stage.sceneProperty().addListener((sceneProperty, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    stage.sizeToScene();

                    SpecialStage.Special special = (SpecialStage.Special) stage.getUserData();

                    DoublePreferencesEntry stageX = preferences.stageBounds.get(special.preferencesEntryName("X"));
                    DoublePreferencesEntry stageY = preferences.stageBounds.get(special.preferencesEntryName("Y"));
                    DoublePreferencesEntry stageWidth = preferences.stageBounds.get(special.preferencesEntryName("Width"));
                    DoublePreferencesEntry stageHeight = preferences.stageBounds.get(special.preferencesEntryName("Height"));

                    stage.setX(Optional.ofNullable(stageX.get()).orElse(stage.getX()));
                    stage.setY(Optional.ofNullable(stageY.get()).orElse(stage.getY()));

                    stage.setMinWidth(stage.getWidth());
                    stage.setMinHeight(stage.getHeight());

                    stage.setWidth(Optional.ofNullable(stageWidth.get()).orElse(preferredSize.getWidth()));
                    stage.setHeight(Optional.ofNullable(stageHeight.get()).orElse(preferredSize.getHeight()));

                    stage.xProperty().addListener((property, oldX, newX) -> stageX.accept(newX.doubleValue()));
                    stage.yProperty().addListener((property, oldY, newY) -> stageY.accept(newY.doubleValue()));
                    stage.widthProperty().addListener((property, oldWidth, newWidth) -> stageWidth.accept(newWidth.doubleValue()));
                    stage.heightProperty().addListener((property, oldHeight, newHeight) -> stageHeight.accept(newHeight.doubleValue()));
                });
            }
        });
    }

}
