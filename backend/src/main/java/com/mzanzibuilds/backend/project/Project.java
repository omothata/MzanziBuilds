package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(nullable = false, length = 150)
  private String name;

  @Column(nullable = false, length = 2000)
  private String description;

  @Column(nullable = false, length = 80)
  private String stage;

  @Column(length = 255)
  private String githubUrl;

  @Column(length = 255)
  private String liveUrl;

  @Column(length = 1000)
  private String supportRequired;

  @Column(length = 255)
  private String imageUrl;

  @ElementCollection
  private List<String> stack = new ArrayList<>();

  @Lob
  @Column(columnDefinition = "TEXT")
  private String readmeContent;

  private Instant readmeFetchedAt;

  @OneToMany(mappedBy = "project")
  private List<ProjectComment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "project")
  private List<ProjectLike> likes = new ArrayList<>();

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(nullable = false)
  private Instant updatedAt = Instant.now();

  public Long getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
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

  public String getReadmeContent() {
    return readmeContent;
  }

  public void setReadmeContent(String readmeContent) {
    this.readmeContent = readmeContent;
  }

  public Instant getReadmeFetchedAt() {
    return readmeFetchedAt;
  }

  public void setReadmeFetchedAt(Instant readmeFetchedAt) {
    this.readmeFetchedAt = readmeFetchedAt;
  }

  public List<ProjectComment> getComments() {
    return comments;
  }

  public List<ProjectLike> getLikes() {
    return likes;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
