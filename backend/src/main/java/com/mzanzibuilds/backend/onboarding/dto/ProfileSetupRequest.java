package com.mzanzibuilds.backend.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProfileSetupRequest {

  @NotNull
  private Long userId;

  @Size(max = 500)
  private String bio;

  @Size(max = 120)
  private String location;

  @Size(max = 255)
  private String profilePictureUrl;

  @Size(max = 255)
  private String websiteUrl;

  @Size(max = 255)
  private String githubUrl;

  @Size(max = 255)
  private String linkedinUrl;

  @Size(max = 255)
  private String xUrl;

  @NotNull
  private List<@NotBlank String> skills;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getGithubUrl() {
    return githubUrl;
  }

  public void setGithubUrl(String githubUrl) {
    this.githubUrl = githubUrl;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getXUrl() {
    return xUrl;
  }

  public void setXUrl(String xUrl) {
    this.xUrl = xUrl;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }
}
