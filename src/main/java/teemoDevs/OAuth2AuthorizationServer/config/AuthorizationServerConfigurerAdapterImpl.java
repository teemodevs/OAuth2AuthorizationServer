package teemoDevs.OAuth2AuthorizationServer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

// https://projects.spring.io/spring-security-oauth/docs/oauth2.html
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurerAdapterImpl extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.teemoDevs-redirect-uri}")
    private String teemoDevsRedirectUri;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * @param clients a configurer that defines the client details service. Client details can be initialized, or you can just refer to an existing store.
     * */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("foo")
                .secret(passwordEncoder.encode("bar"))
                .authorizedGrantTypes("authorization_code", "password", "implicit", "refresh_token")
                .autoApprove(true)
                .redirectUris(teemoDevsRedirectUri)
                .scopes("read");
    }

    /**
     * @param endpoints defines the authorization and token endpoints and the token services.
     * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager);
    }

    /**
     * @param oauthServer defines the security constraints on the token endpoint.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
        // Token 정보를 API(/oauth/check_token)를 활성화 시킨다. ( 기본은 denyAll )
    }
}
