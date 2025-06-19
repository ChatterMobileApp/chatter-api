package com.chatter.chatter_app.modules.auth.dto;

import com.chatter.chatter_app.modules.user.dto.UserDTO;

public class AuthResponse {
  private final String token;
  private final UserDTO user;
  private final String tokenType = "Bearer";
  
  public AuthResponse(String token, UserDTO user) {
    this.token = token;
    this.user = user;
  }
  
  // Getters
  public String getToken() { return token; }
  public UserDTO getUser() { return user; }
  public String getTokenType() { return tokenType; }
}