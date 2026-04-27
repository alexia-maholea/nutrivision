package com.backend.service;

import com.backend.entity.DietaryTag;
import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.repository.UserRepository;
import com.backend.service.dto.AdminUserProfileDto;
import com.backend.service.dto.DietaryTagDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AdminUserProfileDto> getAllUsersWithProfiles() {
        return userRepository.findAllWithProfileAndRestrictions().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AdminUserProfileDto toDto(User user) {
        AdminUserProfileDto dto = new AdminUserProfileDto()
                .setUserId(user.getId())
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setRole(user.getRole());

        Profile profile = user.getProfile();
        if (profile == null) {
            return dto;
        }

        dto.setAge(profile.getAge())
                .setHeight(profile.getHeight())
                .setWeight(profile.getWeight())
                .setGender(profile.getGender())
                .setActivityLevel(profile.getActivityLevel())
                .setGoal(profile.getGoal())
                .setDailyCaloriesTarget(profile.getDailyCaloriesTarget())
                .setMealsPerDay(profile.getMealsPerDay());

        dto.setDietaryRestrictions(profile.getDietaryRestrictions().stream()
                .sorted(Comparator.comparing(DietaryTag::getName))
                .map(tag -> new DietaryTagDto().setId(tag.getId()).setName(tag.getName()))
                .collect(Collectors.toList()));

        return dto;
    }
}

