package com.mzanzibuilds.backend.project;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {

  long countByProjectId(Long projectId);

  boolean existsByProjectIdAndUserId(Long projectId, Long userId);

  Optional<ProjectLike> findByProjectIdAndUserId(Long projectId, Long userId);
}
