package ru.wind.tools.ldap.browser;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.wind.tools.ldap.browser.cdi.MessageBundle;

import javax.inject.Inject;
import java.util.Optional;

public abstract class AbstractStageController {

    @Inject protected MessageBundle messageBundle;

    protected Stage parentStage;

    protected void start(Stage parentStage) {
        this.parentStage = parentStage;
    }

    protected <T extends Node> T lookup(String identifier, Class<T> type) {
        return Optional.ofNullable(parentStage)
            .map(Window::getScene)
            .map(scene -> scene.lookup(identifier))
            .filter(type::isInstance)
            .map(type::cast)
            .orElse(null);
    }

}
