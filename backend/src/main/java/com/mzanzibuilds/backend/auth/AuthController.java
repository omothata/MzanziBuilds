package com.mzanzibuilds.backend.auth;

import com.mzanzibuilds.backend.auth.dto.SigninRequest;
import com.mzanzibuilds.backend.auth.dto.SigninResponse;
import com.mzanzibuilds.backend.auth.dto.SignupRequest;
import com.mzanzibuilds.backend.auth.dto.SignupResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
    return authService.signup(request);
  }

  @PostMapping("/signin")
  public SigninResponse signin(@Valid @RequestBody SigninRequest request) {
    return authService.signin(request);
  }
}
