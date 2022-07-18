package pl.restaurant.employeeservice.business.configuration;

import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import pl.restaurant.employeeservice.business.exception.CustomKeycloakAuthenticationHandler;
import pl.restaurant.employeeservice.business.exception.RestAccessDeniedHandler;

@Profile("dev")
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@AllArgsConstructor
public class KeycloakSecurityConf extends KeycloakWebSecurityConfigurerAdapter {
    RestAccessDeniedHandler restAccessDeniedHandler;
    CustomKeycloakAuthenticationHandler customKeycloakAuthenticationHandler;

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
                .antMatchers("/login").permitAll()
                .antMatchers("/list").hasAnyRole(Role.MANAGER.toString(), "admin")
                .antMatchers("/info/{id}").hasAnyRole(Role.MANAGER.toString(), "admin")
                .antMatchers("/schedule/new").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/logout").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString(), Role.WAITER.toString())
                .antMatchers("/").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/schedule").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/{id}").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/dismiss/{id}").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/schedule/{id}").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/name/{id}").hasAnyRole(Role.ADMIN.toString())
                .antMatchers("/exist/{id}").permitAll()
                .antMatchers("/workstation").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
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
