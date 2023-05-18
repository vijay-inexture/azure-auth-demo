package com.inexture.azureauthgradle.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    CustomAuthSuccessHandler customAuthSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//        	.authorizeHttpRequests()
              //  .addFilterBefore(customFilter(), OAuth2LoginAuthenticationFilter.class)
//                .authorizeRequests()
               // .antMatchers("/").permitAll()
        	 http
         		.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(customAuthSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(azureAdLogoutHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");
        return http.build();
    }

//    private AuthenticationSuccessHandler oauth2SuccessHandler() {
//        return (request, response, authentication) -> {
//            // Handle successful authentication
//            // Retrieve the access token and refresh token from the authentication object
//            System.out.println("success");
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
//                    oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
//
//            String accessToken = client.getAccessToken().getTokenValue();
//            String refreshToken = client.getRefreshToken().getTokenValue();
//            System.out.println("success="+refreshToken);
//            HttpSession session = request.getSession();
//            session.setAttribute("accessToken", accessToken);
//            session.setAttribute("refreshToken", refreshToken);
//            response.sendRedirect("/");
//        };
//    }

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
