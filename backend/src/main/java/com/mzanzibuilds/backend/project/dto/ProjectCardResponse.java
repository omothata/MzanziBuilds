package com.mzanzibuilds.backend.project.dto;

import java.util.List;

public class ProjectCardResponse {

  private final Long projectId;
  private final String name;
  private final String description;
  private final String imageUrl;
  private final List<String> stack;
  private final String stage;
  private final Long ownerId;
  private final String ownerName;
  private final String ownerUsername;

  public ProjectCardResponse(
      Long projectId,
      String name,
      String description,
      String imageUrl,
      List<String> stack,
      String stage,
      Long ownerId,
      String ownerName,
      String ownerUsername
  ) {
    this.projectId = projectId;
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
    this.stack = stack;
    this.stage = stage;
    this.ownerId = ownerId;
    this.ownerName = ownerName;
    this.ownerUsername = ownerUsername;
  }

  public Long getProjectId() {
    return projectId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public List<String> getStack() {
    return stack;
  }

  public String getStage() {
    return stage;
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
}
