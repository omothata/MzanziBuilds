package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.project.dto.ProjectUpdateRequest;
import com.mzanzibuilds.backend.project.dto.ProjectUpdateResponse;
import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectUpdateService {

    @Autowired private ProjectUpdateRepository updateRepo;
    @Autowired private ProjectRepository projectRepo;
    @Autowired private UserRepository userRepo;

    public ProjectUpdateResponse createUpdate(Long projectId, ProjectUpdateRequest req, String username) {

        User user = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow();
        Project project = projectRepo.findById(projectId).orElseThrow();

        ProjectUpdate update = new ProjectUpdate();
        update.setProject(project);
        update.setUser(user);
        update.setTitle(req.title);
        update.setContent(req.content);
        update.setProgressPercent(req.progressPercent);

        updateRepo.save(update);

        return toResponse(update);
    }

    public List<ProjectUpdateResponse> getProjectUpdates(Long projectId) {
        return updateRepo.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProjectUpdateResponse> getFeed() {
        return updateRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProjectUpdateResponse toResponse(ProjectUpdate u) {
        ProjectUpdateResponse res = new ProjectUpdateResponse();
        res.id = u.getId();
        res.title = u.getTitle();
        res.content = u.getContent();
        res.progressPercent = u.getProgressPercent();

        res.projectId = u.getProject().getId();
        res.projectName = u.getProject().getName();

        res.username = u.getUser().getUsername();
        res.createdAt = u.getCreatedAt();

        return res;
    }
}