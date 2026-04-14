package com.mzanzibuilds.backend.project;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {

  @EntityGraph(attributePaths = {"author"})
  List<ProjectComment> findByProjectIdOrderByCreatedAtAsc(Long projectId);
}
