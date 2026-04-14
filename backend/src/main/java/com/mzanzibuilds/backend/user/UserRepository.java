package com.mzanzibuilds.backend.user;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmailIgnoreCase(String email);

  boolean existsByUsernameIgnoreCase(String username);

  Optional<User> findByEmailIgnoreCase(String email);

  Optional<User> findByUsernameIgnoreCase(String username);

  List<User> findTop5ByIdNotOrderByCreatedAtDesc(Long id);
}
