package teemoDevs.OAuth2AuthorizationServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/authorize", "/oauth/token", "/login**").permitAll()
                //.antMatchers("/**").permitAll()
                .and().formLogin().permitAll();
                //;
    }
}
