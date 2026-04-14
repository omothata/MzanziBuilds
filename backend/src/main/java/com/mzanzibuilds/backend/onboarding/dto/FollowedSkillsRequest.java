package com.mzanzibuilds.backend.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class FollowedSkillsRequest {

  @NotNull
  private Long userId;

  @NotNull
  private List<@NotBlank String> followedSkills;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<String> getFollowedSkills() {
    return followedSkills;
  }

  public void setFollowedSkills(List<String> followedSkills) {
    this.followedSkills = followedSkills;
  }
}
