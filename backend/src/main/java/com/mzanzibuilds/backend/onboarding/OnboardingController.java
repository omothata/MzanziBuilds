package com.mzanzibuilds.backend.onboarding;

import com.mzanzibuilds.backend.onboarding.dto.FollowAccountsRequest;
import com.mzanzibuilds.backend.onboarding.dto.FollowedSkillsRequest;
import com.mzanzibuilds.backend.onboarding.dto.OnboardingResponse;
import com.mzanzibuilds.backend.onboarding.dto.ProfileSetupRequest;
import com.mzanzibuilds.backend.onboarding.dto.UserSummaryResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

  private final OnboardingService onboardingService;

  public OnboardingController(OnboardingService onboardingService) {
    this.onboardingService = onboardingService;
  }

  @PostMapping("/profile")
  public OnboardingResponse saveProfile(@Valid @RequestBody ProfileSetupRequest request) {
    return onboardingService.saveProfile(request);
  }

  @PostMapping("/skills")
  public OnboardingResponse saveFollowedSkills(@Valid @RequestBody FollowedSkillsRequest request) {
    return onboardingService.saveFollowedSkills(request);
  }

  @GetMapping("/suggestions")
  public List<UserSummaryResponse> getSuggestions(@RequestParam Long userId) {
    return onboardingService.getSuggestions(userId);
  }

  @PostMapping("/follow-suggestions")
  public OnboardingResponse followSuggestions(@Valid @RequestBody FollowAccountsRequest request) {
    return onboardingService.followSuggestions(request);
  }

  @GetMapping
  public OnboardingResponse getOnboarding(@RequestParam Long userId) {
    return onboardingService.getOnboarding(userId);
  }
}
