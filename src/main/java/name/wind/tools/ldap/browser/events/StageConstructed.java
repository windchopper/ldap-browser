package name.wind.tools.ldap.browser.events;

import javafx.stage.Stage;

public class StageConstructed {

    public static final String IDENTIFIER__CONNECTION_LIST = "connectionListStage";
    public static final String IDENTIFIER__CONNECTION = "connectionStage";

    private final Stage stage;
    private final String identifier;

    public StageConstructed(Stage stage, String identifier) {
        this.stage = stage;
        this.identifier = identifier;
    }

    public Stage stage() {
        return stage;
    }

    public String identifier() {
        return identifier;
    }

}
