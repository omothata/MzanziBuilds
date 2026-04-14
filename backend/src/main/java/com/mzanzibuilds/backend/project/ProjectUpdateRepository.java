package com.mzanzibuilds.backend.project;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Long> {

    List<ProjectUpdate> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    List<ProjectUpdate> findAllByOrderByCreatedAtDesc();
}