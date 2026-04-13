package com.mzanzibuilds.backend.auth;

import com.mzanzibuilds.backend.auth.dto.SignupRequest;
import com.mzanzibuilds.backend.auth.dto.SignupResponse;
import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public SignupResponse signup(SignupRequest request) {
    String name = request.getName().trim();
    String email = request.getEmail().trim().toLowerCase();
    String username = request.getUsername().trim().toLowerCase();

    if (userRepository.existsByEmailIgnoreCase(email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
    }

    if (userRepository.existsByUsernameIgnoreCase(username)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
    }

    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setUsername(username);
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

    User savedUser = userRepository.save(user);

    return new SignupResponse(
        "Account created successfully",
        savedUser.getId(),
        savedUser.getUsername(),
        savedUser.getEmail()
    );
  }
}
