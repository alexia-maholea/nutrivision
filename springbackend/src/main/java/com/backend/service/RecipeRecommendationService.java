package com.backend.service;

import com.backend.entity.DietaryTag;
import com.backend.entity.Profile;
import com.backend.entity.Recipe;
import com.backend.repository.RecipeRepository;
import com.backend.service.dto.RecipeSummaryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecipeRecommendationService {

    private final RecipeRepository recipeRepository;

    public RecipeRecommendationService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Rețetele care respectă toate restricțiile din profil (tag-urile userului trebuie să apară pe rețetă).
     * Dacă userul nu are restricții, se întorc toate rețetele.
     */
    public List<RecipeSummaryDto> recommendedForProfile(Profile profile) {
        Set<Long> requiredTagIds = profile.getDietaryRestrictions().stream()
                .map(DietaryTag::getId)
                .collect(Collectors.toSet());

        return recipeRepository.findAllByOrderByTitleAsc().stream()
                .filter(r -> matchesRestrictions(r, requiredTagIds))
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    public boolean matchesRestrictions(Recipe recipe, Profile profile) {
        Set<Long> requiredTagIds = profile.getDietaryRestrictions().stream()
                .map(DietaryTag::getId)
                .collect(Collectors.toSet());
        return matchesRestrictions(recipe, requiredTagIds);
    }

    private boolean matchesRestrictions(Recipe recipe, Set<Long> requiredTagIds) {
        if (requiredTagIds.isEmpty()) {
            return true;
        }
        Set<Long> recipeTagIds = recipe.getDietaryTags().stream()
                .map(DietaryTag::getId)
                .collect(Collectors.toSet());
        return recipeTagIds.containsAll(requiredTagIds);
    }

    private RecipeSummaryDto toSummary(Recipe r) {
        return new RecipeSummaryDto()
                .setId(r.getId())
                .setTitle(r.getTitle())
                .setCalories(r.getCalories())
                .setCookingTime(r.getCookingTime())
                .setDifficulty(r.getDifficulty());
    }
}
