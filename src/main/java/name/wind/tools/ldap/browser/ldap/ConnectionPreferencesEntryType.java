package name.wind.tools.ldap.browser.ldap;


import com.github.windchopper.common.preferences.types.StructuralType;
import com.github.windchopper.common.util.Pipeliner;
import com.github.windchopper.common.util.stream.FailableFunction;
import com.github.windchopper.common.util.stream.FailableFunctionResult;

import javax.json.Json;
import javax.naming.ldap.LdapName;
import java.util.Optional;

public class ConnectionPreferencesEntryType extends StructuralType<Connection> {

    private static final String KEY__IDENTIFIER = "identifier";
    private static final String KEY__NAME = "name";
    private static final String KEY__HOST = "host";
    private static final String KEY__PORT = "port";
    private static final String KEY__TRANSPORT_SECURITY = "transportSecurity";
    private static final String KEY__AUTH_METHOD = "authMethod";
    private static final String KEY__BASE = "base";
    private static final String KEY__USERNAME = "username";
    private static final String KEY__PASSWORD = "password";

    public ConnectionPreferencesEntryType() {
        super(
            source -> new Connection(
                source.getString(KEY__IDENTIFIER),
                source.getString(KEY__NAME),
                source.getString(KEY__HOST),
                source.getInt(KEY__PORT, Connection.DEFAULT_PORT),
                Optional.ofNullable(source.getString(KEY__TRANSPORT_SECURITY))
                    .map(FailableFunction.wrap(TransportSecurity::valueOf))
                    .flatMap(FailableFunctionResult::result)
                    .orElse(Connection.DEFAULT_TRANSPORT_SECURITY),
                Optional.ofNullable(source.getString(KEY__AUTH_METHOD))
                    .map(FailableFunction.wrap(AuthMethod::valueOf))
                    .flatMap(FailableFunctionResult::result)
                    .orElse(Connection.DEFAULT_AUTH_METHOD),
                Optional.ofNullable(source.getString(KEY__BASE))
                    .map(FailableFunction.wrap(LdapName::new))
                    .flatMap(FailableFunctionResult::result)
                    .orElse(null),
                source.getString(KEY__USERNAME),
                source.getString(KEY__PASSWORD)),
            source -> Pipeliner.of(Json::createObjectBuilder)
                .set(target -> value -> target.add(KEY__IDENTIFIER, value), source.identifier)
                .set(target -> value -> target.add(KEY__NAME, value), source.nameProperty.get())
                .set(target -> value -> target.add(KEY__HOST, value), source.hostProperty.get())
                .set(target -> value -> target.add(KEY__PORT, value), source.portProperty.get())
                .accept(target -> Optional.ofNullable(source.transportSecurityProperty.get())
                    .map(TransportSecurity::name)
                    .ifPresent(value -> target.add(KEY__TRANSPORT_SECURITY, value)))
                .accept(target -> Optional.ofNullable(source.authMethodProperty.get())
                    .map(AuthMethod::name)
                    .ifPresent(value -> target.add(KEY__AUTH_METHOD, value)))
                .accept(target -> Optional.ofNullable(source.baseProperty.get())
                    .map(LdapName::toString)
                    .ifPresent(value -> target.add(KEY__BASE, value)))
                .get()
                    .build());
    }

}
