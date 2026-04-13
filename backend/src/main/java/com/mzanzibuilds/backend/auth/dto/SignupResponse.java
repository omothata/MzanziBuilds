package com.mzanzibuilds.backend.auth.dto;

public class SignupResponse {

  private final String message;
  private final Long userId;
  private final String username;
  private final String email;

  public SignupResponse(String message, Long userId, String username, String email) {
    this.message = message;
    this.userId = userId;
    this.username = username;
    this.email = email;
  }

  public String getMessage() {
    return message;
  }

  public Long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }
}
