package com.inexture.azureauthgradle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name="auth_user")
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	private String email;
	@Column(columnDefinition = "text") 
	private String accessToken;
	@Column(columnDefinition = "text") 
	private String refreshToken;
	@Column(columnDefinition = "bigint")
	private Long expiresAt;
	
	public User() {
		super();
	}
	
	public User(Long id, String accessToken, String refreshToken, Long expiresAt) {
		super();
		this.id = id;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public Long getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
