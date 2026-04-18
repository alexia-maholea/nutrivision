package com.backend.controller;

import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.repository.ProfileRepository;
import com.backend.repository.UserRepository;
import com.backend.service.RecipeRecommendationService;
import com.backend.service.dto.RecipeSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipes", description = "Recommended recipes based on profile dietary tags")
public class RecipeController implements SecuredRestController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RecipeRecommendationService recipeRecommendationService;

    public RecipeController(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            RecipeRecommendationService recipeRecommendationService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.recipeRecommendationService = recipeRecommendationService;
    }

    @GetMapping("/recommended")
    @Operation(summary = "Recommended recipes", description = "Recipes that include all dietary restriction tags from your profile. If you have no restrictions, all recipes are returned.")
    public ResponseEntity<List<RecipeSummaryDto>> recommended() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        Profile profile = profileRepository.findByUser_Email(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        return ResponseEntity.ok(recipeRecommendationService.recommendedForProfile(profile));
    }
}
