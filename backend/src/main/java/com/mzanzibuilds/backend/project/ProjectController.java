package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.project.dto.CreateCommentRequest;
import com.mzanzibuilds.backend.project.dto.CreateProjectRequest;
import com.mzanzibuilds.backend.project.dto.ProjectCardResponse;
import com.mzanzibuilds.backend.project.dto.ProjectCommentResponse;
import com.mzanzibuilds.backend.project.dto.ProjectDetailResponse;
import com.mzanzibuilds.backend.project.dto.ToggleLikeRequest;
import com.mzanzibuilds.backend.project.dto.ToggleLikeResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectDetailResponse createProject(@Valid @RequestBody CreateProjectRequest request) {
    return projectService.createProject(request);
  }

  @GetMapping("/feed")
  public List<ProjectCardResponse> getMainFeed(@RequestParam Long userId) {
    return projectService.getMainFeed(userId);
  }

  @GetMapping("/explore")
  public List<ProjectCardResponse> getExploreFeed(@RequestParam Long userId) {
    return projectService.getExploreFeed(userId);
  }

  @GetMapping("/{projectId}")
  public ProjectDetailResponse getProject(
      @PathVariable Long projectId,
      @RequestParam(required = false) Long userId
  ) {
    return projectService.getProject(projectId, userId);
  }

  @PostMapping("/{projectId}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public ProjectCommentResponse addComment(
      @PathVariable Long projectId,
      @Valid @RequestBody CreateCommentRequest request
  ) {
    return projectService.addComment(projectId, request);
  }

  @PostMapping("/{projectId}/likes")
  public ToggleLikeResponse toggleLike(
      @PathVariable Long projectId,
      @Valid @RequestBody ToggleLikeRequest request
  ) {
    return projectService.toggleLike(projectId, request);
  }

  @GetMapping("/celebration-wall")
public List<CelebrationUserResponse> getCelebrationWall() {
    return projectService.getCelebrationWall();
}
}
