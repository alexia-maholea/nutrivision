package com.backend.service;

import com.backend.entity.DietaryTag;
import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.exception.BadRequestException;
import com.backend.repository.DietaryTagRepository;
import com.backend.repository.ProfileRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.DietaryTagDto;
import com.backend.service.dto.MeResponseDto;
import com.backend.service.dto.MealsPerDayRequestDto;
import com.backend.service.dto.ProfileResponseDto;
import com.backend.service.dto.ProfileUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final DietaryTagRepository dietaryTagRepository;

    public ProfileService(ProfileRepository profileRepository,
                          UserRepository userRepository,
                          DietaryTagRepository dietaryTagRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.dietaryTagRepository = dietaryTagRepository;
    }

    public ProfileResponseDto getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Profile profile = profileRepository.findByUser_Email(email)
                .orElseGet(() -> createProfileForUser(user));

        return mapProfile(profile);
    }

    @Transactional(readOnly = true)
    public MeResponseDto getCurrentUserIdentity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        return new MeResponseDto()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setRole(user.getRole().name());
    }

    public List<ProfileResponseDto> getAllProfilesForAdmin() {
        return profileRepository.findAllForAdminListing().stream()
                .map(this::mapProfile)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponseDto> getProfilesForAdmin(String q, Pageable pageable) {
        String normalizedQ = normalizeQuery(q);
        return (normalizedQ == null
                ? profileRepository.findAllForAdminListing(pageable)
                : profileRepository.searchForAdminListing(normalizedQ, pageable))
                .map(this::mapProfile);
    }

    private ProfileResponseDto mapProfile(Profile profile) {

        ProfileResponseDto dto = new ProfileResponseDto()
                .setEmail(profile.getUser().getEmail())
                .setName(profile.getUser().getName())
                .setRole(profile.getUser().getRole().name())
                .setAge(profile.getAge())
                .setHeight(profile.getHeight())
                .setWeight(profile.getWeight())
                .setGender(profile.getGender())
                .setActivityLevel(profile.getActivityLevel())
                .setGoal(profile.getGoal())
                .setDailyCaloriesTarget(profile.getDailyCaloriesTarget())
                .setMealsPerDay(profile.getMealsPerDay());

        dto.setDietaryRestrictions(
                profile.getDietaryRestrictions().stream()
                        .sorted(Comparator.comparing(DietaryTag::getName))
                        .map(this::toTagDto)
                        .collect(Collectors.toList()));

        return dto;
    }

    public ProfileResponseDto updateMealsPerDay(MealsPerDayRequestDto request) {
        Integer n = request.getMealsPerDay();
        if (n == null || n < 2 || n > 10) {
            throw new BadRequestException("mealsPerDay must be between 2 and 10");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        Profile profile = profileRepository.findByUser_Email(email)
                .orElseGet(() -> createProfileForUser(user));
        profile.setMealsPerDay(n);
        profileRepository.save(profile);
        return getCurrentUserProfile();
    }

    public ProfileResponseDto updateCurrentUserProfile(ProfileUpdateRequestDto request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        Profile profile = profileRepository.findByUser_Email(email)
                .orElseGet(() -> createProfileForUser(user));

        if (request.getAge() != null) {
            if (request.getAge() <= 0 || request.getAge() > 130) {
                throw new BadRequestException("age must be between 1 and 130");
            }
            profile.setAge(request.getAge());
        }
        if (request.getHeight() != null) {
            if (request.getHeight() <= 0) {
                throw new BadRequestException("height must be > 0");
            }
            profile.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            if (request.getWeight() <= 0) {
                throw new BadRequestException("weight must be > 0");
            }
            profile.setWeight(request.getWeight());
        }
        if (request.getDailyCaloriesTarget() != null) {
            if (request.getDailyCaloriesTarget() <= 0) {
                throw new BadRequestException("dailyCaloriesTarget must be > 0");
            }
            profile.setDailyCaloriesTarget(request.getDailyCaloriesTarget());
        }

        if (request.getGender() != null) {
            profile.setGender(request.getGender());
        }
        if (request.getActivityLevel() != null) {
            profile.setActivityLevel(request.getActivityLevel());
        }
        if (request.getGoal() != null) {
            profile.setGoal(request.getGoal());
        }

        if (request.getDietaryRestrictionTagIds() != null) {
            Set<Long> requestedIds = request.getDietaryRestrictionTagIds().stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (requestedIds.isEmpty()) {
                profile.setDietaryRestrictions(new LinkedHashSet<>());
            } else {
                List<DietaryTag> tags = dietaryTagRepository.findAllById(requestedIds);
                if (tags.size() != requestedIds.size()) {
                    Set<Long> foundIds = tags.stream().map(DietaryTag::getId).collect(Collectors.toSet());
                    Set<Long> missingIds = requestedIds.stream()
                            .filter(id -> !foundIds.contains(id))
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    throw new BadRequestException("Unknown dietary tag ids: " + missingIds);
                }
                profile.setDietaryRestrictions(tags.stream().collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        }

        profileRepository.save(profile);
        return getCurrentUserProfile();
    }

    /**
     * Conturi vechi fără rând în {@code profile}: îl creăm (relație 1–1 cu User).
     */
    protected Profile createProfileForUser(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setMealsPerDay(3);
        user.setProfile(profile);
        return profileRepository.save(profile);
    }

    private DietaryTagDto toTagDto(DietaryTag tag) {
        return new DietaryTagDto()
                .setId(tag.getId())
                .setName(tag.getName());
    }

    private String normalizeQuery(String q) {
        if (q == null) {
            return null;
        }
        String trimmed = q.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
