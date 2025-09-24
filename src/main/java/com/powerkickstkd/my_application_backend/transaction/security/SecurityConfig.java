package com.powerkickstkd.my_application_backend.transaction.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.sql.DataSource;
import java.io.IOException;

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
                                // Add your custom success handler here
                                .successHandler(customAuthenticationSuccessHandler())
                                // Configures the authentication failure handler to manage what happens when login attempts fail
                                // This custom handler provides specific behavior for handling failed authentication attempts
                                .failureHandler(customAuthenticationFailureHandler())
                                // Allow everyone to see login page (No need to be logged in)
                                .permitAll()

                )
                // Add logout support for default URL
                .logout(logout ->
                        logout
                                // Custom logout URL
                                .logoutUrl("/logout")
                                // Configures the logout success handler to manage what happens after a successful logout
                                // This custom handler provides specific behavior for post-logout redirection and cleanup
                                .logoutSuccessHandler(customLogoutSuccessHandler())
                                .permitAll()
                )
                // Configure exception handling for security-related exceptions
                // This section handles what happens when access is denied or authentication fails
                .exceptionHandling(exception -> exception
                        // Set custom handler for access denied scenarios (403 errors)
                        // When users try to access resources they don't have permission for
                        .accessDeniedHandler(customAccessDeniedHandler())
                        // Set custom entry point for authentication requirements
                        // When unauthenticated users try to access protected resources
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                );

        return http.build();
    }

    /*
        HttpServletRequest request:
            - Contains all information about the incoming HTTP request that failed authentication
            - Provides access to form data (like username/password), headers, cookies, and request parameters
            - Used here to retrieve the failed login attempt details for logging or error handling
        HttpServletResponse response:
            - Allows the handler to send back a response to the client when authentication fails
            - Used to redirect users back to the login page with error information
            - Enables setting HTTP status codes, headers, and response content
    */

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {

            // Custom logic for handling authentication failure
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response,
                                                AuthenticationException exception)
                    throws IOException {

                String errorMessage = null;

                if (exception instanceof BadCredentialsException) {

                    errorMessage = "Invalid credentials provided";

                } else if (exception instanceof DisabledException) {

                    errorMessage = "Account is disabled";

                }

                // Add flash attribute
                request.getSession().setAttribute("loginErrorMessage", errorMessage);

                // Redirect to login page
                response.sendRedirect(request.getContextPath() + "/showMyLoginPage");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {

                // Log successful login
                System.out.println("User " + authentication.getName() + " logged in successfully");


                // Get the HTTP session
                HttpSession session = request.getSession();

                // Set session timeout to 30 minutes
                session.setMaxInactiveInterval(2 * 60); // 30 minutes in seconds

                // Set the target URL where it's the dashboard (this is what super would do)
                setDefaultTargetUrl("/homepage");

                // Add custom logic like updating last login time
                // or setting up user session attributes
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    // TODO: Implement custom access denied handler
    // Error handling for access denied (403)
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request,
                               HttpServletResponse response,
                               AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                request.getRequestDispatcher("/error/403").forward(request, response);
            }
        };
    }

    // Error handling for authentication entry points
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        // Return to the default login page
        return new LoginUrlAuthenticationEntryPoint("/showMyLoginPage");
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

                // Get session to store logout success message
                HttpSession session = request.getSession();

                // Add logout success message to session
                session.setAttribute("logoutSuccessMessage", "You have been successfully logged out.");

                // Optional: Log the logout event
                if (authentication != null) {
                    String username = authentication.getName();
                    // You can add logging here if needed
                    System.out.println("User " + username + " has logged out successfully");
                }

                // Build the redirect URL
                String redirectUrl = request.getContextPath() + "/showMyLoginPage?logout=true";
                System.out.println("Redirecting to: " + redirectUrl); // Debug log

                // Redirect to login page with logout parameter
                response.sendRedirect(redirectUrl);
            }
        };
    }
}
