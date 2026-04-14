package com.mzanzibuilds.backend.onboarding.dto;

import java.util.List;

public class UserSummaryResponse {

  private final Long userId;
  private final String name;
  private final String username;
  private final String bio;
  private final String location;
  private final String profilePictureUrl;
  private final List<String> skills;

  public UserSummaryResponse(
      Long userId,
      String name,
      String username,
      String bio,
      String location,
      String profilePictureUrl,
      List<String> skills
  ) {
    this.userId = userId;
    this.name = name;
    this.username = username;
    this.bio = bio;
    this.location = location;
    this.profilePictureUrl = profilePictureUrl;
    this.skills = skills;
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

  public String getBio() {
    return bio;
  }

  public String getLocation() {
    return location;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public List<String> getSkills() {
    return skills;
  }
}
