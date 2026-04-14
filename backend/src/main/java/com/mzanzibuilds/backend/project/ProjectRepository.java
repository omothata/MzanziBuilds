package com.mzanzibuilds.backend.project;

import com.mzanzibuilds.backend.onboarding.dto.UserSummaryResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  @EntityGraph(attributePaths = {"owner"})
  List<Project> findTop20ByOwnerIdInOrderByCreatedAtDesc(Collection<Long> ownerIds);

  @EntityGraph(attributePaths = {"owner"})
  List<Project> findTop20ByOrderByCreatedAtDesc();

  @EntityGraph(attributePaths = {"owner"})
  @Query("select distinct p from Project p join p.stack stack where lower(stack) in ?1 order by p.createdAt desc")
  List<Project> findTop20ByStackIn(List<String> followedSkills);

  @EntityGraph(attributePaths = {"owner"})
  List<Project> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    @Query("""
    SELECT new com.mzanzibuilds.backend.onboarding.dto.UserSummaryResponse(u.id, u.username, u.name, COUNT(p))
    FROM Project p
    JOIN p.owner u
    WHERE p.stage = 'COMPLETED'
    GROUP BY u.id, u.username, u.name
    ORDER BY COUNT(p) DESC
  """)
  List<UserSummaryResponse> getCelebrationWall();
}

