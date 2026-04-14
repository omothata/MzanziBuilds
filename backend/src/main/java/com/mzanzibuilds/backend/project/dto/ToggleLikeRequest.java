package com.mzanzibuilds.backend.project.dto;

import jakarta.validation.constraints.NotNull;

public class ToggleLikeRequest {

  @NotNull
  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
