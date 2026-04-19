package com.backend.controller;

import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.repository.ProfileRepository;
import com.backend.repository.UserRepository;
import com.backend.service.RecipeRecommendationService;
import com.backend.service.RecipeService;
import com.backend.service.dto.RecipeCreateRequestDto;
import com.backend.service.dto.RecipeDetailDto;
import com.backend.service.dto.RecipeSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final RecipeService recipeService;

    public RecipeController(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            RecipeRecommendationService recipeRecommendationService,
            RecipeService recipeService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.recipeRecommendationService = recipeRecommendationService;
        this.recipeService = recipeService;
    }

    @GetMapping("/all")
    @Operation(summary = "All recipes", description = "Returns all recipes from the catalog, ordered by title.")
    public ResponseEntity<List<RecipeSummaryDto>> allRecipes() {
        return ResponseEntity.ok(recipeRecommendationService.allRecipes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recipe details", description = "Returns full details for a recipe: nutrition, dietary tags, ingredients, and steps.")
    public ResponseEntity<RecipeDetailDto> recipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @GetMapping("/my")
    @Operation(summary = "My recipes", description = "Returns recipes compatible with your dietary restrictions.")
    public ResponseEntity<List<RecipeSummaryDto>> myRecipes() {
        return ResponseEntity.ok(recipeRecommendationService.recommendedForProfile(getCurrentUserProfile()));
    }

    @GetMapping("/recommended")
    @Operation(summary = "Recommended recipes", description = "Alias for /my. Recipes that include all dietary restriction tags from your profile.")
    public ResponseEntity<List<RecipeSummaryDto>> recommended() {
        return myRecipes();
    }

    private Profile getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        return profileRepository.findByUser_Email(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add a new recipe", description = "Creates a new recipe. Admin role required.")
    public ResponseEntity<RecipeDetailDto> createRecipe(@RequestBody RecipeCreateRequestDto body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(body));
    }
}
