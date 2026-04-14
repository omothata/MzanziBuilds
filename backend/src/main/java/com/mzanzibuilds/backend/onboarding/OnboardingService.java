package com.mzanzibuilds.backend.onboarding;

import com.mzanzibuilds.backend.onboarding.dto.FollowAccountsRequest;
import com.mzanzibuilds.backend.onboarding.dto.FollowedSkillsRequest;
import com.mzanzibuilds.backend.onboarding.dto.OnboardingResponse;
import com.mzanzibuilds.backend.onboarding.dto.ProfileSetupRequest;
import com.mzanzibuilds.backend.onboarding.dto.UserSummaryResponse;
import com.mzanzibuilds.backend.user.User;
import com.mzanzibuilds.backend.user.UserRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OnboardingService {

  private final UserRepository userRepository;

  public OnboardingService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public OnboardingResponse saveProfile(ProfileSetupRequest request) {
    User user = getUser(request.getUserId());

    user.setBio(normalizeText(request.getBio()));
    user.setLocation(normalizeText(request.getLocation()));
    user.setProfilePictureUrl(normalizeText(request.getProfilePictureUrl()));
    user.setWebsiteUrl(normalizeText(request.getWebsiteUrl()));
    user.setGithubUrl(normalizeText(request.getGithubUrl()));
    user.setLinkedinUrl(normalizeText(request.getLinkedinUrl()));
    user.setXUrl(normalizeText(request.getXUrl()));
    user.setSkills(normalizeList(request.getSkills()));

    return toOnboardingResponse(userRepository.save(user));
  }

  @Transactional
  public OnboardingResponse saveFollowedSkills(FollowedSkillsRequest request) {
    User user = getUser(request.getUserId());
    user.setFollowedSkills(normalizeList(request.getFollowedSkills()));
    return toOnboardingResponse(userRepository.save(user));
  }

  @Transactional(readOnly = true)
  public List<UserSummaryResponse> getSuggestions(Long userId) {
    User user = getUser(userId);
    Set<Long> excludedIds = user.getFollowing().stream()
        .map(User::getId)
        .collect(Collectors.toCollection(LinkedHashSet::new));
    excludedIds.add(user.getId());

    return userRepository.findTop5ByIdNotOrderByCreatedAtDesc(user.getId()).stream()
        .filter(candidate -> !excludedIds.contains(candidate.getId()))
        .map(this::toUserSummaryResponse)
        .toList();
  }

  @Transactional
  public OnboardingResponse followSuggestions(FollowAccountsRequest request) {
    User user = getUser(request.getUserId());
    Set<User> following = new LinkedHashSet<>(user.getFollowing());

    if (request.getAccountIds() != null && !request.getAccountIds().isEmpty()) {
      List<User> accounts = userRepository.findAllById(request.getAccountIds()).stream()
          .filter(account -> !account.getId().equals(user.getId()))
          .toList();
      following.addAll(accounts);
    }

    user.setFollowing(following);
    user.setOnboardingCompleted(true);

    return toOnboardingResponse(userRepository.save(user));
  }

  @Transactional(readOnly = true)
  public OnboardingResponse getOnboarding(Long userId) {
    return toOnboardingResponse(getUser(userId));
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private OnboardingResponse toOnboardingResponse(User user) {
    return new OnboardingResponse(
        user.getId(),
        user.isOnboardingCompleted(),
        List.copyOf(user.getSkills()),
        List.copyOf(user.getFollowedSkills()),
        user.getFollowing().stream().map(User::getId).toList()
    );
  }

  private UserSummaryResponse toUserSummaryResponse(User user) {
    return new UserSummaryResponse(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getBio(),
        user.getLocation(),
        user.getProfilePictureUrl(),
        List.copyOf(user.getSkills())
    );
  }

  private String normalizeText(String value) {
    if (value == null) {
      return null;
    }

    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private List<String> normalizeList(List<String> values) {
    if (values == null) {
      return List.of();
    }

    return values.stream()
        .map(this::normalizeText)
        .filter(value -> value != null)
        .distinct()
        .toList();
  }
}
