package name.wind.tools.ldap.browser;

import javafx.fxml.FXMLLoader;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import static name.wind.common.util.SyntaxBums.with;

@Dependent public class FXMLLoaderFactory {

    @Inject private Bundle bundle;

    @Produces public FXMLLoader createLoader() {
        return with(FXMLLoader::new, loader -> {
            loader.setControllerFactory(controllerType -> CDI.current().select(controllerType).get());
            loader.setResources(bundle.javaResourceBundle());
            return loader;
        });
    }

}
