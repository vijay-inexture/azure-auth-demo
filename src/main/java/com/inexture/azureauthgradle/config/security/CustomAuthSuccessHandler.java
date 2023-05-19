package com.inexture.azureauthgradle.config.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.inexture.azureauthgradle.entity.User;
import com.inexture.azureauthgradle.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthSuccessHandler  implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserRepository userRepository;

    private final OAuth2AuthorizedClientService clientService;

    public CustomAuthSuccessHandler(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        

        OAuth2AuthenticatedPrincipal principal = oauthToken.getPrincipal();
        String email = "";
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
            email = oauth2User.getAttribute("preferred_username");
        }
        System.out.println("user="+email);
        
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();
        long expiresAt = client.getAccessToken().getExpiresAt().toEpochMilli();
        
        System.out.println("accessToken="+accessToken);
        System.out.println("refreshToken="+refreshToken);
        System.out.println("expiredAt="+client.getAccessToken().getExpiresAt());
        
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;
        if(optionalUser.isPresent()) {
        	user = optionalUser.get();	
        }else {
        	user = new User();
        	user.setEmail(email);
        }
        user.setAccessToken(accessToken);
    	user.setRefreshToken(refreshToken);
    	user.setExpiresAt(expiresAt);
    	userRepository.save(user);
        
        HttpSession session = request.getSession();
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);


        response.sendRedirect("/");

    }


}
