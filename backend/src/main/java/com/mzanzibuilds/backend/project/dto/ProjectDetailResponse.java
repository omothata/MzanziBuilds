package com.mzanzibuilds.backend.project.dto;

import java.time.Instant;
import java.util.List;

public class ProjectDetailResponse {

  private final Long projectId;
  private final Long ownerId;
  private final String ownerName;
  private final String ownerUsername;
  private final String name;
  private final String description;
  private final String stage;
  private final String githubUrl;
  private final String liveUrl;
  private final String supportRequired;
  private final String imageUrl;
  private final List<String> stack;
  private final String readmeContent;
  private final Long likeCount;
  private final boolean likedByCurrentUser;
  private final List<ProjectCommentResponse> comments;
  private final Instant createdAt;

  public ProjectDetailResponse(
      Long projectId,
      Long ownerId,
      String ownerName,
      String ownerUsername,
      String name,
      String description,
      String stage,
      String githubUrl,
      String liveUrl,
      String supportRequired,
      String imageUrl,
      List<String> stack,
      String readmeContent,
      Long likeCount,
      boolean likedByCurrentUser,
      List<ProjectCommentResponse> comments,
      Instant createdAt
  ) {
    this.projectId = projectId;
    this.ownerId = ownerId;
    this.ownerName = ownerName;
    this.ownerUsername = ownerUsername;
    this.name = name;
    this.description = description;
    this.stage = stage;
    this.githubUrl = githubUrl;
    this.liveUrl = liveUrl;
    this.supportRequired = supportRequired;
    this.imageUrl = imageUrl;
    this.stack = stack;
    this.readmeContent = readmeContent;
    this.likeCount = likeCount;
    this.likedByCurrentUser = likedByCurrentUser;
    this.comments = comments;
    this.createdAt = createdAt;
  }

  public Long getProjectId() {
    return projectId;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getOwnerUsername() {
    return ownerUsername;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getStage() {
    return stage;
  }

  public String getGithubUrl() {
    return githubUrl;
  }

  public String getLiveUrl() {
    return liveUrl;
  }

  public String getSupportRequired() {
    return supportRequired;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public List<String> getStack() {
    return stack;
  }

  public String getReadmeContent() {
    return readmeContent;
  }

  public Long getLikeCount() {
    return likeCount;
  }

  public boolean isLikedByCurrentUser() {
    return likedByCurrentUser;
  }

  public List<ProjectCommentResponse> getComments() {
    return comments;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
