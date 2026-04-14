package com.mzanzibuilds.backend.project.dto;

public class ToggleLikeResponse {

  private final Long projectId;
  private final Long likeCount;
  private final boolean likedByCurrentUser;

  public ToggleLikeResponse(Long projectId, Long likeCount, boolean likedByCurrentUser) {
    this.projectId = projectId;
    this.likeCount = likeCount;
    this.likedByCurrentUser = likedByCurrentUser;
  }

  public Long getProjectId() {
    return projectId;
  }

  public Long getLikeCount() {
    return likeCount;
  }

  public boolean isLikedByCurrentUser() {
    return likedByCurrentUser;
  }
}
