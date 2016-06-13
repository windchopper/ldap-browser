package name.wind.tools.ldap.browser.events;

import javafx.geometry.Dimension2D;
import javafx.stage.Stage;

public class AfterStageConstructed {

    public static final String IDENTIFIER__CONNECTION_LIST = "connectionListStage";
    public static final String IDENTIFIER__CONNECTION = "connectionStage";

    private final Stage stage;
    private final String identifier;
    private final Dimension2D preferredSize;

    public AfterStageConstructed(Stage stage, String identifier, Dimension2D preferredSize) {
        this.stage = stage;
        this.identifier = identifier;
        this.preferredSize = preferredSize;
    }

    public Stage stage() {
        return stage;
    }

    public String identifier() {
        return identifier;
    }

    public Dimension2D preferredSize() {
        return preferredSize;
    }

}
