package com.mzanzibuilds.backend.project.dto;

import java.time.Instant;

public class ProjectCommentResponse {

  private final Long commentId;
  private final Long authorId;
  private final String authorName;
  private final String authorUsername;
  private final String content;
  private final Instant createdAt;

  public ProjectCommentResponse(
      Long commentId,
      Long authorId,
      String authorName,
      String authorUsername,
      String content,
      Instant createdAt
  ) {
    this.commentId = commentId;
    this.authorId = authorId;
    this.authorName = authorName;
    this.authorUsername = authorUsername;
    this.content = content;
    this.createdAt = createdAt;
  }

  public Long getCommentId() {
    return commentId;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getAuthorUsername() {
    return authorUsername;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
