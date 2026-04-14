package com.mzanzibuilds.backend.user;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(length = 500)
  private String bio;

  @Column(length = 120)
  private String location;

  @Column(length = 255)
  private String profilePictureUrl;

  @Column(length = 255)
  private String websiteUrl;

  @Column(length = 255)
  private String githubUrl;

  @Column(length = 255)
  private String linkedinUrl;

  @Column(length = 255)
  private String xUrl;

  @ElementCollection
  private List<String> skills = new ArrayList<>();

  @ElementCollection
  private List<String> followedSkills = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "user_follows",
      joinColumns = @JoinColumn(name = "follower_id"),
      inverseJoinColumns = @JoinColumn(name = "followed_id")
  )
  private Set<User> following = new HashSet<>();

  @Column(nullable = false)
  private boolean onboardingCompleted = false;

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
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

  public List<String> getFollowedSkills() {
    return followedSkills;
  }

  public void setFollowedSkills(List<String> followedSkills) {
    this.followedSkills = followedSkills;
  }

  public Set<User> getFollowing() {
    return following;
  }

  public void setFollowing(Set<User> following) {
    this.following = following;
  }

  public boolean isOnboardingCompleted() {
    return onboardingCompleted;
  }

  public void setOnboardingCompleted(boolean onboardingCompleted) {
    this.onboardingCompleted = onboardingCompleted;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
