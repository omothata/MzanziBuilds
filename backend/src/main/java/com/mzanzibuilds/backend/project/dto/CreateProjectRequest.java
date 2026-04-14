package com.mzanzibuilds.backend.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateProjectRequest {

  @NotNull
  private Long userId;

  @NotBlank
  @Size(max = 150)
  private String name;

  @NotBlank
  @Size(max = 2000)
  private String description;

  @NotBlank
  @Size(max = 80)
  private String stage;

  @Size(max = 255)
  private String githubUrl;

  @Size(max = 255)
  private String liveUrl;

  @Size(max = 1000)
  private String supportRequired;

  @Size(max = 255)
  private String imageUrl;

  private List<String> stack;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getGithubUrl() {
    return githubUrl;
  }

  public void setGithubUrl(String githubUrl) {
    this.githubUrl = githubUrl;
  }

  public String getLiveUrl() {
    return liveUrl;
  }

  public void setLiveUrl(String liveUrl) {
    this.liveUrl = liveUrl;
  }

  public String getSupportRequired() {
    return supportRequired;
  }

  public void setSupportRequired(String supportRequired) {
    this.supportRequired = supportRequired;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<String> getStack() {
    return stack;
  }

  public void setStack(List<String> stack) {
    this.stack = stack;
  }
}
