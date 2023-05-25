package com.inexture.azureauthgradle.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    CustomAuthSuccessHandler customAuthSuccessHandler;
    
    @Autowired
	TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/api/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login(login -> login
                        .successHandler(customAuthSuccessHandler))
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(azureAdLogoutHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    public LogoutSuccessHandler azureAdLogoutHandler() {
        return (request, response, authentication) -> {
            // Perform any custom logout logic here
            // For Azure AD, you can redirect the user to the Azure AD logout endpoint
            try {
                response.sendRedirect("https://login.microsoftonline.com/a40ee758-1021-4d42-b0f0-f4f554472af6/oauth2/logout");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
