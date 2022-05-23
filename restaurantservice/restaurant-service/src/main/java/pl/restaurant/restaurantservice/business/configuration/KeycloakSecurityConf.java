package pl.restaurant.restaurantservice.business.configuration;

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
import pl.restaurant.restaurantservice.business.exception.CustomKeycloakAuthenticationHandler;
import pl.restaurant.restaurantservice.business.exception.RestAccessDeniedHandler;

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
                //restaurant
                .antMatchers("/info/{id}").permitAll()
                .antMatchers("/exist/{id}").permitAll()
                .antMatchers("/table").hasAnyRole(Role.ADMIN.toString())
                .antMatchers("/details/{id}").hasAnyRole(Role.ADMIN.toString())
                .antMatchers("/short/{id}").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/new").hasAnyRole(Role.ADMIN.toString())
                .antMatchers("/{id}").hasAnyRole(Role.ADMIN.toString())
                .antMatchers("/{id}").hasAnyRole(Role.ADMIN.toString())
                //reservation
                .antMatchers("/reservation/info/{id}").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/reservation/list").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
                .antMatchers("/reservation/hours").permitAll()
                .antMatchers("/reservation/").permitAll()
                .antMatchers("/reservation/{id}").hasAnyRole(Role.MANAGER.toString(), Role.ADMIN.toString())
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
