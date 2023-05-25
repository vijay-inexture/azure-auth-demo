package com.inexture.azureauthgradle.config.security;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inexture.azureauthgradle.repository.UserAuthRepository;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service 
public class TokenService { 
	
	@Autowired
	UserAuthRepository userAuthRepository;
	
	public String extractUsername(String token) throws Exception {
		return (String) extractClaim(token, "unique_name");
	}
	
	public Object extractClaim(String token, String claim) throws Exception {
		final Claims claims = extractAllClaims(token);
		return claims.get(claim, Object.class);
	}
	
	private Claims extractAllClaims(String token) throws Exception {
		JWT jwt = JWTParser.parse(token);
		Map<String,Object> claims=jwt.getJWTClaimsSet().getClaims();
		Claims jwtClaims = new DefaultClaims(claims);
		return jwtClaims;		
	}
	
	public boolean isTokenValid(String token) throws Exception {
		boolean existsByAccessToken = userAuthRepository.existsByAccessToken(token);
		return (existsByAccessToken && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) throws Exception {
		long expiration = extractExpiration(token);
		Instant expirationInstant = Instant.ofEpochSecond(expiration);
		boolean isExpired = expirationInstant.isBefore(Instant.now());
		log.info("isExpired:"+isExpired);
		return isExpired;
	}

	private long extractExpiration(String token) throws Exception {
		return (long) extractClaim(token, "exp");
	}
	
}
