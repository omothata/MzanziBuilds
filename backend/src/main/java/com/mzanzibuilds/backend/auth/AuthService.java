package com.mzanzibuilds.backend.auth;

import com.mzanzibuilds.backend.auth.dto.SigninRequest;
import com.mzanzibuilds.backend.auth.dto.SigninResponse;
import com.mzanzibuilds.backend.auth.dto.SignupRequest;
import com.mzanzibuilds.backend.auth.dto.SignupResponse;
import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import java.util.Optional;
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
        savedUser.getName(),
        savedUser.getUsername(),
        savedUser.getEmail(),
        savedUser.isOnboardingCompleted()
    );
  }

  @Transactional(readOnly = true)
  public SigninResponse signin(SigninRequest request) {
    String identifier = request.getIdentifier().trim().toLowerCase();

    Optional<User> user = identifier.contains("@")
        ? userRepository.findByEmailIgnoreCase(identifier)
        : userRepository.findByUsernameIgnoreCase(identifier);

    User foundUser = user.orElseThrow(() ->
        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/email or password"));

    if (!passwordEncoder.matches(request.getPassword(), foundUser.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/email or password");
    }

    return new SigninResponse(
        "Sign-in successful",
        foundUser.getId(),
        foundUser.getName(),
        foundUser.getUsername(),
        foundUser.getEmail(),
        foundUser.isOnboardingCompleted()
    );
  }
}
