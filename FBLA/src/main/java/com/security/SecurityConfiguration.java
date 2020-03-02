package com.security;

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

                //Register Custom Request Cache
                .requestCache().requestCache(new CustomRequestCache())

                //Restrict access to application
                .and().authorizeRequests()

                //Allow Vaadin internal requests
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                //Allows all requests by logged in users
                .anyRequest().authenticated()

                //Configure the login page
                .and().formLogin()
                    .loginPage(LOGIN_URL).permitAll()
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)

                //Configure Logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}password")
                        .roles("USER")
                        .build();

        //TODO need to get better authentication source
        return new InMemoryUserDetailsManager(user);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                //Vaadin static resources
                "/VAADIN/**",

                //the standard favicon URI
                "/favicon.ico",

                //the robots exclusion standard
                "/robots.txt",

                //web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",

                //icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",

                //static resources (development mode)
                "/frontend/**",

                //H2 Debugging (development mode)
                "/h2-console/**",

                //static resources (production mode)
                "/frontend-es5/**", "/frontend-es6/**");
    }
}
