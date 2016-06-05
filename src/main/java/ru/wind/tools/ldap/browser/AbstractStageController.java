package ru.wind.tools.ldap.browser;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public abstract class AbstractStageController {

    protected Stage stage;

    protected void start(Stage stage) {
        this.stage = stage;
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
