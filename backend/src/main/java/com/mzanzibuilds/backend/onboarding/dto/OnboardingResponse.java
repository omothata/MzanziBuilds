package com.mzanzibuilds.backend.onboarding.dto;

import java.util.List;

public class OnboardingResponse {

  private final Long userId;
  private final boolean onboardingCompleted;
  private final List<String> skills;
  private final List<String> followedSkills;
  private final List<Long> followingIds;

  public OnboardingResponse(
      Long userId,
      boolean onboardingCompleted,
      List<String> skills,
      List<String> followedSkills,
      List<Long> followingIds
  ) {
    this.userId = userId;
    this.onboardingCompleted = onboardingCompleted;
    this.skills = skills;
    this.followedSkills = followedSkills;
    this.followingIds = followingIds;
  }

  public Long getUserId() {
    return userId;
  }

  public boolean isOnboardingCompleted() {
    return onboardingCompleted;
  }

  public List<String> getSkills() {
    return skills;
  }

  public List<String> getFollowedSkills() {
    return followedSkills;
  }

  public List<Long> getFollowingIds() {
    return followingIds;
  }
}
