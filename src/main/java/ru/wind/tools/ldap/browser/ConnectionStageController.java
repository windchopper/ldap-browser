package ru.wind.tools.ldap.browser;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class ConnectionStageController {

    @Produces @Named("connectionStageSceneRoot") public Parent buildSceneRoot() {
        return new BorderPane();
    }

}
