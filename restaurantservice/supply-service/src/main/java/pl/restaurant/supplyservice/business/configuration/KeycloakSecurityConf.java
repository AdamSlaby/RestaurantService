package pl.restaurant.supplyservice.business.configuration;

import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import pl.restaurant.supplyservice.business.exception.CustomKeycloakAuthenticationHandler;
import pl.restaurant.supplyservice.business.exception.RestAccessDeniedHandler;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@AllArgsConstructor
public class KeycloakSecurityConf extends KeycloakWebSecurityConfigurerAdapter {
    private RestAccessDeniedHandler restAccessDeniedHandler;
    private CustomKeycloakAuthenticationHandler customKeycloakAuthenticationHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests()
                //invoice
                .antMatchers("/invoice/{nr}").hasAnyRole(Role.ADMIN.toString(), Role.MANAGER.toString())
                .antMatchers("/invoice/list").hasAnyRole(Role.ADMIN.toString(), Role.MANAGER.toString())
                .antMatchers("/invoice/").hasAnyRole(Role.ADMIN.toString(), Role.MANAGER.toString())
                //supply
                .antMatchers("/list").hasAnyRole(Role.ADMIN.toString(), Role.MANAGER.toString())
                .antMatchers("/").hasAnyRole(Role.ADMIN.toString(), Role.MANAGER.toString())
                .anyRequest().permitAll();

        http.exceptionHandling().accessDeniedHandler(restAccessDeniedHandler);
    }

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(this.authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy());
        filter.setAuthenticationFailureHandler(customKeycloakAuthenticationHandler);
        return filter;
    }
}
