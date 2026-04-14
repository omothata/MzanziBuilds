package com.mzanzibuilds.backend.onboarding;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OnboardingControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private Long primaryUserId;
  private Long suggestedUserId;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    primaryUserId = createUser("Ofentse", "ofentse@example.com", "ofentse");
    suggestedUserId = createUser("Alex", "alex@example.com", "alex");
  }

  @Test
  void saveProfileStoresProfileFieldsAndSkills() throws Exception {
    mockMvc.perform(post("/api/onboarding/profile")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "userId": %d,
                  "bio": "Building useful tools",
                  "location": "Johannesburg",
                  "profilePictureUrl": "https://example.com/avatar.jpg",
                  "websiteUrl": "https://mzanzibuilds.dev",
                  "githubUrl": "https://github.com/ofentse",
                  "linkedinUrl": "https://linkedin.com/in/ofentse",
                  "xUrl": "https://x.com/ofentse",
                  "skills": ["Java", "Spring Boot", "PostgreSQL"]
                }
                """.formatted(primaryUserId)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(primaryUserId))
        .andExpect(jsonPath("$.skills[0]").value("Java"))
        .andExpect(jsonPath("$.onboardingCompleted").value(false));
  }

  @Test
  void saveFollowedSkillsStoresInterestList() throws Exception {
    mockMvc.perform(post("/api/onboarding/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "userId": %d,
                  "followedSkills": ["React", "UI Design", "Spring Boot"]
                }
                """.formatted(primaryUserId)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.followedSkills[0]").value("React"))
        .andExpect(jsonPath("$.followedSkills[2]").value("Spring Boot"));
  }

  @Test
  void suggestionsReturnOtherAccounts() throws Exception {
    mockMvc.perform(get("/api/onboarding/suggestions").param("userId", primaryUserId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userId").value(suggestedUserId))
        .andExpect(jsonPath("$[0].username").value("alex"));
  }

  @Test
  void followingSuggestionsMarksOnboardingComplete() throws Exception {
    mockMvc.perform(post("/api/onboarding/follow-suggestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "userId": %d,
                  "accountIds": [%d]
                }
                """.formatted(primaryUserId, suggestedUserId)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.onboardingCompleted").value(true))
        .andExpect(jsonPath("$.followingIds[0]").value(suggestedUserId));
  }

  private Long createUser(String name, String email, String username) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setUsername(username);
    user.setPasswordHash(passwordEncoder.encode("strongpassword123"));
    return userRepository.save(user).getId();
  }
}
