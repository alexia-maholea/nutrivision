package com.backend.service;

import com.backend.entity.DietaryTag;
import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.repository.ProfileRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.DietaryTagDto;
import com.backend.service.dto.ProfileResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileResponseDto getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Profile profile = profileRepository.findByUser_Email(email)
                .orElseGet(() -> createProfileForUser(user));

        ProfileResponseDto dto = new ProfileResponseDto()
                .setEmail(profile.getUser().getEmail())
                .setName(profile.getUser().getName())
                .setAge(profile.getAge())
                .setHeight(profile.getHeight())
                .setWeight(profile.getWeight())
                .setGender(profile.getGender())
                .setActivityLevel(profile.getActivityLevel())
                .setGoal(profile.getGoal())
                .setDailyCaloriesTarget(profile.getDailyCaloriesTarget());

        dto.setDietaryRestrictions(
                profile.getDietaryRestrictions().stream()
                        .sorted(Comparator.comparing(DietaryTag::getName))
                        .map(this::toTagDto)
                        .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Conturi vechi fără rând în {@code profile}: îl creăm (relație 1–1 cu User).
     */
    protected Profile createProfileForUser(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        user.setProfile(profile);
        return profileRepository.save(profile);
    }

    private DietaryTagDto toTagDto(DietaryTag tag) {
        return new DietaryTagDto()
                .setId(tag.getId())
                .setName(tag.getName());
    }
}
