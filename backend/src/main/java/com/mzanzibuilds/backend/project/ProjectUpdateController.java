package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.project.dto.CreateCommentRequest;
import com.mzanzibuilds.backend.project.dto.CreateProjectRequest;
import com.mzanzibuilds.backend.project.dto.ProjectCardResponse;
import com.mzanzibuilds.backend.project.dto.ProjectCommentResponse;
import com.mzanzibuilds.backend.project.dto.ProjectDetailResponse;
import com.mzanzibuilds.backend.project.dto.ProjectUpdateRequest;
import com.mzanzibuilds.backend.project.dto.ProjectUpdateResponse;
import com.mzanzibuilds.backend.project.dto.ToggleLikeRequest;
import com.mzanzibuilds.backend.project.dto.ToggleLikeResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api")
public class ProjectUpdateController {

    @Autowired private ProjectUpdateService service;

    @PostMapping("/projects/{projectId}/updates")
    public ResponseEntity<?> createUpdate(
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest request,
            Principal principal) {

        return ResponseEntity.ok(
                service.createUpdate(projectId, request, principal.getName())
        );
    }

    @GetMapping("/projects/{projectId}/updates")
    public List<ProjectUpdateResponse> getProjectUpdates(@PathVariable Long projectId) {
        return service.getProjectUpdates(projectId);
    }

    @GetMapping("/feed")
    public List<ProjectUpdateResponse> getFeed() {
        return service.getFeed();
    }
}