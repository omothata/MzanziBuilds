package com.mzanzibuilds.backend.auth.dto;

public class SigninResponse {

  private final String message;
  private final Long userId;
  private final String name;
  private final String username;
  private final String email;
  private final boolean onboardingCompleted;

  public SigninResponse(
      String message,
      Long userId,
      String name,
      String username,
      String email,
      boolean onboardingCompleted
  ) {
    this.message = message;
    this.userId = userId;
    this.name = name;
    this.username = username;
    this.email = email;
    this.onboardingCompleted = onboardingCompleted;
  }

  public String getMessage() {
    return message;
  }

  public Long getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public boolean isOnboardingCompleted() {
    return onboardingCompleted;
  }
}
