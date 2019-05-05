package teemoDevs.OAuth2AuthorizationServer.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.stereotype.Component;

@Component
@EnableAuthorizationServer
public class AuthorizationServerConfigurerAdapterImpl extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;

    public AuthorizationServerConfigurerAdapterImpl(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("foo")
                .secret("bar")
                //.authorities("USER")
                .authorizedGrantTypes("authorization_code", "implicit", "refresh_token")
                .autoApprove(true)
                .redirectUris("http://localhost:8080/oauth2/authorization/teemo")
                .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
        // Token 정보를 API(/oauth/check_token)를 활성화 시킨다. ( 기본은 denyAll )
    }
}
