package com.frontend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configures security with username, password, and different redirection options
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and().authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                .anyRequest().authenticated()

                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    /**
     * Configures the Login Information and credentials to the app.
     * <p>
     * Currently using temporary security for demonstration purposes.
     */
    @Bean
    @Override
    /*
     * Can be made more secure with this
     * https://dzone.com/articles/spring-security-authentication
     */
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withUsername("shourya.bansal")
                        .password("{noop}password")
                        .roles("USER")
                        .build();
    
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Creates a configuration which allows usage of certain things while offline
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                //Vaadin files
                "/VAADIN/**",

                //Icon
                "/favicon.ico",

                //Standard Text File
                "/robots.txt",

                //Offline Working
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",

                //Icons, Images, and Styles
                "/icons/**",
                "/images/**",
                "/styles/**",

                //Frontend Resources (Development Mode)
                "/frontend/**",

                //H2 Debuggin (Development Mode)
                "/h2-console/**",

                //Static Resources (Production Mode)
                "/frontend-es5/**",
                "/frontend-es6/**",

                //Data files
                "/data/**");
    }
}