package com.mzanzibuilds.backend.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void signupCreatesUserAndHashesPassword() throws Exception {
    String payload = """
        {
          "name": "Ofentse",
          "email": "ofentse@example.com",
          "username": "ofentse",
          "password": "strongpassword123"
        }
        """;

    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Account created successfully"))
        .andExpect(jsonPath("$.username").value("ofentse"))
        .andExpect(jsonPath("$.email").value("ofentse@example.com"))
        .andExpect(jsonPath("$.userId").isNumber());

    User savedUser = userRepository.findByEmailIgnoreCase("ofentse@example.com").orElseThrow();

    assertThat(savedUser.getName()).isEqualTo("Ofentse");
    assertThat(savedUser.getUsername()).isEqualTo("ofentse");
    assertThat(savedUser.getPasswordHash()).isNotEqualTo("strongpassword123");
    assertThat(savedUser.getPasswordHash()).startsWith("$2");
  }

  @Test
  void signupRejectsDuplicateEmail() throws Exception {
    signup("""
        {
          "name": "Ofentse",
          "email": "ofentse@example.com",
          "username": "ofentse",
          "password": "strongpassword123"
        }
        """);

    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "Another User",
                  "email": "ofentse@example.com",
                  "username": "anotheruser",
                  "password": "strongpassword123"
                }
                """))
        .andExpect(status().isConflict());
  }

  @Test
  void signupRejectsDuplicateUsername() throws Exception {
    signup("""
        {
          "name": "Ofentse",
          "email": "ofentse@example.com",
          "username": "ofentse",
          "password": "strongpassword123"
        }
        """);

    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "Another User",
                  "email": "another@example.com",
                  "username": "ofentse",
                  "password": "strongpassword123"
                }
                """))
        .andExpect(status().isConflict());
  }

  @Test
  void signupRejectsInvalidPayload() throws Exception {
    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "",
                  "email": "not-an-email",
                  "username": "ab",
                  "password": "123"
                }
                """))
        .andExpect(status().isBadRequest());
  }

  private void signup(String payload) throws Exception {
    mockMvc.perform(post("/api/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isCreated());
  }
}
