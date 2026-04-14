package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.project.dto.CreateCommentRequest;
import com.mzanzibuilds.backend.project.dto.CreateProjectRequest;
import com.mzanzibuilds.backend.project.dto.ProjectCardResponse;
import com.mzanzibuilds.backend.project.dto.ProjectCommentResponse;
import com.mzanzibuilds.backend.project.dto.ProjectDetailResponse;
import com.mzanzibuilds.backend.project.dto.ToggleLikeRequest;
import com.mzanzibuilds.backend.project.dto.ToggleLikeResponse;
import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProjectService {

  private static final Pattern GITHUB_REPO_PATTERN = Pattern.compile("^https?://github\\.com/([^/]+)/([^/#?]+).*$");

  private final ProjectRepository projectRepository;
  private final ProjectCommentRepository projectCommentRepository;
  private final ProjectLikeRepository projectLikeRepository;
  private final UserRepository userRepository;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  public ProjectService(
      ProjectRepository projectRepository,
      ProjectCommentRepository projectCommentRepository,
      ProjectLikeRepository projectLikeRepository,
      UserRepository userRepository
  ) {
    this.projectRepository = projectRepository;
    this.projectCommentRepository = projectCommentRepository;
    this.projectLikeRepository = projectLikeRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public ProjectDetailResponse createProject(CreateProjectRequest request) {
    User owner = getUser(request.getUserId());

    Project project = new Project();
    project.setOwner(owner);
    project.setName(normalizeRequired(request.getName(), "Project name is required"));
    project.setDescription(normalizeRequired(request.getDescription(), "Project description is required"));
    project.setStage(normalizeRequired(request.getStage(), "Project stage is required"));
    project.setGithubUrl(normalizeNullable(request.getGithubUrl()));
    project.setLiveUrl(normalizeNullable(request.getLiveUrl()));
    project.setSupportRequired(normalizeNullable(request.getSupportRequired()));
    project.setImageUrl(normalizeNullable(request.getImageUrl()));
    project.setStack(normalizeList(request.getStack()));
    project.setUpdatedAt(Instant.now());

    if (project.getGithubUrl() != null) {
      project.setReadmeContent(fetchGithubReadme(project.getGithubUrl()));
      project.setReadmeFetchedAt(project.getReadmeContent() == null ? null : Instant.now());
    }

    Project savedProject = projectRepository.save(project);
    return toProjectDetail(savedProject, owner.getId());
  }

  @Transactional(readOnly = true)
  public List<ProjectCardResponse> getMainFeed(Long userId) {
    User user = getUser(userId);
    List<Long> followingIds = user.getFollowing().stream().map(User::getId).toList();
    List<Project> projects = followingIds.isEmpty()
        ? projectRepository.findTop20ByOrderByCreatedAtDesc()
        : projectRepository.findTop20ByOwnerIdInOrderByCreatedAtDesc(followingIds);

    return projects.stream().map(this::toProjectCard).toList();
  }

  @Transactional(readOnly = true)
  public List<ProjectCardResponse> getExploreFeed(Long userId) {
    User user = getUser(userId);
    List<String> followedSkills = user.getFollowedSkills().stream()
        .map(skill -> skill.toLowerCase(Locale.ROOT))
        .toList();

    List<Project> projects = followedSkills.isEmpty()
        ? projectRepository.findTop20ByOrderByCreatedAtDesc()
        : projectRepository.findTop20ByStackIn(followedSkills);

    return projects.stream().map(this::toProjectCard).toList();
  }

  @Transactional(readOnly = true)
  public ProjectDetailResponse getProject(Long projectId, Long currentUserId) {
    Project project = getProjectEntity(projectId);
    return toProjectDetail(project, currentUserId);
  }

  @Transactional
  public ProjectCommentResponse addComment(Long projectId, CreateCommentRequest request) {
    Project project = getProjectEntity(projectId);
    User author = getUser(request.getUserId());

    ProjectComment comment = new ProjectComment();
    comment.setProject(project);
    comment.setAuthor(author);
    comment.setContent(normalizeRequired(request.getContent(), "Comment content is required"));

    ProjectComment savedComment = projectCommentRepository.save(comment);
    return toCommentResponse(savedComment);
  }

  @Transactional
  public ToggleLikeResponse toggleLike(Long projectId, ToggleLikeRequest request) {
    Project project = getProjectEntity(projectId);
    User user = getUser(request.getUserId());

    boolean liked;
    projectLikeRepository.findByProjectIdAndUserId(project.getId(), user.getId())
        .ifPresentOrElse(projectLikeRepository::delete, () -> {
          ProjectLike like = new ProjectLike();
          like.setProject(project);
          like.setUser(user);
          projectLikeRepository.save(like);
        });

    liked = projectLikeRepository.existsByProjectIdAndUserId(project.getId(), user.getId());

    return new ToggleLikeResponse(
        project.getId(),
        projectLikeRepository.countByProjectId(project.getId()),
        liked
    );
  }

  private Project getProjectEntity(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private ProjectCardResponse toProjectCard(Project project) {
    return new ProjectCardResponse(
        project.getId(),
        project.getName(),
        project.getDescription(),
        project.getImageUrl(),
        List.copyOf(project.getStack()),
        project.getStage(),
        project.getOwner().getId(),
        project.getOwner().getName(),
        project.getOwner().getUsername()
    );
  }

  private ProjectDetailResponse toProjectDetail(Project project, Long currentUserId) {
    List<ProjectCommentResponse> comments = projectCommentRepository.findByProjectIdOrderByCreatedAtAsc(project.getId()).stream()
        .map(this::toCommentResponse)
        .toList();

    long likeCount = projectLikeRepository.countByProjectId(project.getId());
    boolean likedByCurrentUser = currentUserId != null
        && projectLikeRepository.existsByProjectIdAndUserId(project.getId(), currentUserId);

    return new ProjectDetailResponse(
        project.getId(),
        project.getOwner().getId(),
        project.getOwner().getName(),
        project.getOwner().getUsername(),
        project.getName(),
        project.getDescription(),
        project.getStage(),
        project.getGithubUrl(),
        project.getLiveUrl(),
        project.getSupportRequired(),
        project.getImageUrl(),
        List.copyOf(project.getStack()),
        project.getReadmeContent(),
        likeCount,
        likedByCurrentUser,
        comments,
        project.getCreatedAt()
    );
  }

  private ProjectCommentResponse toCommentResponse(ProjectComment comment) {
    return new ProjectCommentResponse(
        comment.getId(),
        comment.getAuthor().getId(),
        comment.getAuthor().getName(),
        comment.getAuthor().getUsername(),
        comment.getContent(),
        comment.getCreatedAt()
    );
  }

  private String normalizeRequired(String value, String message) {
    String normalized = normalizeNullable(value);
    if (normalized == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
    return normalized;
  }

  private String normalizeNullable(String value) {
    if (value == null) {
      return null;
    }

    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private List<String> normalizeList(List<String> values) {
    if (values == null) {
      return new ArrayList<>();
    }

    return values.stream()
        .map(this::normalizeNullable)
        .filter(value -> value != null)
        .map(value -> value.toLowerCase(Locale.ROOT))
        .distinct()
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private String fetchGithubReadme(String githubUrl) {
    Matcher matcher = GITHUB_REPO_PATTERN.matcher(githubUrl);

    if (!matcher.matches()) {
      return null;
    }

    String owner = matcher.group(1);
    String repo = matcher.group(2).replace(".git", "");
    String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/readme";

    try {
      HttpRequest request = HttpRequest.newBuilder(new URI(apiUrl))
          .header("Accept", "application/vnd.github.raw+json")
          .header("X-GitHub-Api-Version", "2022-11-28")
          .GET()
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        return response.body();
      }
    } catch (IOException | InterruptedException | URISyntaxException ignored) {
      if (ignored instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }

    return null;
  }

  public List<CelebrationUserResponse> getCelebrationWall() {
    return projectRepository.getCelebrationWall();
}
}
