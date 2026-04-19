package com.backend.controller;

import com.backend.entity.Profile;
import com.backend.entity.User;
import com.backend.repository.ProfileRepository;
import com.backend.repository.UserRepository;
import com.backend.service.RecipeRecommendationService;
import com.backend.service.RecipeService;
import com.backend.service.dto.PagedResponseDto;
import com.backend.service.dto.RecipeCreateRequestDto;
import com.backend.service.dto.RecipeDetailDto;
import com.backend.service.dto.RecipeSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    @Operation(summary = "All recipes", description = "Returns paginated recipes from the catalog, with optional title search.")
    public ResponseEntity<PagedResponseDto<RecipeSummaryDto>> allRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(PagedResponseDto.from(
                recipeRecommendationService.allRecipes(q, PageRequest.of(page, size, Sort.by("title").ascending()))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recipe details", description = "Returns full details for a recipe: nutrition, dietary tags, ingredients, and steps.")
    public ResponseEntity<RecipeDetailDto> recipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @GetMapping("/my")
    @Operation(summary = "My recipes", description = "Returns paginated recipes compatible with your dietary restrictions, with optional title search.")
    public ResponseEntity<PagedResponseDto<RecipeSummaryDto>> myRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(PagedResponseDto.from(recipeRecommendationService.recommendedForProfile(
                getCurrentUserProfile(), q, PageRequest.of(page, size, Sort.by("title").ascending()))));
    }

    @GetMapping("/recommended")
    @Operation(summary = "Recommended recipes", description = "Alias for /my. Recipes that include all dietary restriction tags from your profile.")
    public ResponseEntity<PagedResponseDto<RecipeSummaryDto>> recommended(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q) {
        return myRecipes(page, size, q);
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update recipe", description = "Updates an existing recipe. Admin role required.")
    public ResponseEntity<RecipeDetailDto> updateRecipe(@PathVariable Long id, @RequestBody RecipeCreateRequestDto body) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete recipe", description = "Deletes a recipe. Returns 204 on success, 404 if recipe is missing, 409 if recipe is referenced by meal plans.")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
