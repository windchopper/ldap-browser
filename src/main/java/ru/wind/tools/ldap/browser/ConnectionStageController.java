package ru.wind.tools.ldap.browser;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.wind.common.fx.Alignment;
import name.wind.common.fx.Fill;
import name.wind.common.fx.builder.*;
import name.wind.common.preferences.DoublePreferencesEntry;
import ru.wind.tools.ldap.browser.cdi.NamedStage;
import ru.wind.tools.ldap.browser.cdi.PreferencesEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped public class ConnectionStageController extends AbstractStageController {

    private static final int PADDING = 4;

    @Inject @PreferencesEntry("connectionStageX") private DoublePreferencesEntry stageX;
    @Inject @PreferencesEntry("connectionStageY") private DoublePreferencesEntry stageY;
    @Inject @PreferencesEntry("connectionStageWidth") private DoublePreferencesEntry stageWidth;
    @Inject @PreferencesEntry("connectionStageHeight") private DoublePreferencesEntry stageHeight;

    private Parent buildSceneRoot() {
        return new GridPaneBuilder(GridPane::new)
            .columnConstraints(0, new ColumnConstraintsBuilder(ColumnConstraints::new)
                .fillWidth(true))
            .columnConstraints(1, new ColumnConstraintsBuilder(ColumnConstraints::new)
                .fillWidth(true))
            .add(
                new LabelBuilder(Label::new)
                    .text(messageBundle.bundleString("ConnectionStageController.displayNameLabel"))
                    .get(),
                0, 0, 2, 1,
                Alignment.LEFT_CENTER,
                Fill.NONE,
                new InsetsBuilder()
                    .all(PADDING)
                    .get())
            .add(
                new TextFieldBuilder(TextField::new)
                    .get(),
                0, 1, 2, 1,
                Alignment.CENTER_CENTER,
                Fill.HORIZONTAL,
                new InsetsBuilder()
                    .all(PADDING)
                    .get())
            .get();
    }

    @Override protected void start(@Observes @NamedStage("connection") Stage stage) {
        super.start(stage);

        new StageBuilder(() -> stage)
            .preferredLocationAndSize(
                stage.getX(),
                stage.getY(),
                Screen.getPrimary().getVisualBounds().getWidth() / 3,
                Screen.getPrimary().getVisualBounds().getHeight() / 3,
                stageX,
                stageY,
                stageWidth,
                stageHeight)
            .fixMinSize()
            .title(messageBundle.bundleString("ConnectionStageController.title"))
            .scene(buildSceneRoot())
            .get().show();
    }

}
