package com.powerkickstkd.my_application_backend.transaction.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // Create admin account (add support for JDBC)

    // Inject data source Auto-Configured by Spring Boot
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        // Tell Spring Security to use JDBC authentication with our data source
        return new JdbcUserDetailsManager(dataSource);
    }


    // Customize our own LOGIN FORM
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize ->
                authorize
                        // Restrict based on Roles
                        .requestMatchers("/homepage").hasRole("DEVELOPER")
                        // Only Developer role can access /transactions and its sub-path
                        .requestMatchers(HttpMethod.GET, "/transactions/showTransactionFormAdd").hasRole("DEVELOPER")
                        // Request to permit all access to our static folder
                        .requestMatchers("/css/**", "icons/**","/js/**", "/img/**", "/static/**")
                        // Allow access to static resources
                        .permitAll()
                        // Any request coming to the app must be authenticated (the user must be login)
                        .anyRequest().authenticated()
                )
                // Customizing the form login process
                .formLogin(form ->
                        form
                                // Show our custom form at the request mapping (Need to create a controller)
                                .loginPage("/showMyLoginPage")
                                // Login form should POST data to this URL for processing (check user id and password)
                                .loginProcessingUrl("/authenticateTheAdmin")
                                // Redirect to dashboard or homepage after successful login
                                .defaultSuccessUrl("/homepage", true)
                                // Allow everyone to see login page (No need to be logged in)
                                .permitAll()

                )
                // Add logout support for default URL
                .logout(logout ->
                        logout
                                // Custom logout URL
                                .logoutUrl("/logout")
                                // Redirect to a specified logout success URL
                                .logoutSuccessUrl("/showMyLoginPage?logout")
                                .permitAll()
                );




        return http.build();
    }
}
