package com.mzanzibuilds.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SigninRequest {

  @NotBlank
  @Size(max = 100)
  private String identifier;

  @NotBlank
  @Size(min = 8, max = 255)
  private String password;

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
