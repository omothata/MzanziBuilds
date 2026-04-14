package com.mzanzibuilds.backend.onboarding.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class FollowAccountsRequest {

  @NotNull
  private Long userId;

  @NotNull
  private List<Long> accountIds;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<Long> getAccountIds() {
    return accountIds;
  }

  public void setAccountIds(List<Long> accountIds) {
    this.accountIds = accountIds;
  }
}
