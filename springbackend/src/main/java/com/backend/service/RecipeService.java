package com.backend.service;

import com.backend.entity.DietaryTag;
import com.backend.entity.Ingredient;
import com.backend.entity.Recipe;
import com.backend.entity.RecipeIngredient;
import com.backend.entity.RecipeStep;
import com.backend.exception.BadRequestException;
import com.backend.exception.ConflictException;
import com.backend.repository.DietaryTagRepository;
import com.backend.repository.IngredientRepository;
import com.backend.repository.RecipeRepository;
import com.backend.service.event.RecipeCreatedEvent;
import com.backend.service.dto.RecipeCreateRequestDto;
import com.backend.service.dto.DietaryTagDto;
import com.backend.service.dto.RecipeDetailDto;
import com.backend.service.dto.RecipeIngredientDto;
import com.backend.service.dto.RecipeIngredientInputDto;
import com.backend.service.dto.RecipeStepDto;
import com.backend.service.dto.RecipeStepInputDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final DietaryTagRepository dietaryTagRepository;
    private final IngredientRepository ingredientRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RecipeService(RecipeRepository recipeRepository,
                         DietaryTagRepository dietaryTagRepository,
                         IngredientRepository ingredientRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.recipeRepository = recipeRepository;
        this.dietaryTagRepository = dietaryTagRepository;
        this.ingredientRepository = ingredientRepository;
        this.eventPublisher = eventPublisher;
    }

    public RecipeDetailDto createRecipe(RecipeCreateRequestDto request) {
        validateRequest(request);
        String title = request.getTitle().trim();

        if (recipeRepository.existsByTitleIgnoreCase(title)) {
            throw new ConflictException("Recipe already exists with title: " + title);
        }

        Recipe recipe = new Recipe()
                .setTitle(title)
                .setDescription(request.getDescription())
                .setCalories(request.getCalories())
                .setProtein(request.getProtein())
                .setCarbs(request.getCarbs())
                .setFat(request.getFat())
                .setCookingTime(request.getCookingTime())
                .setDifficulty(request.getDifficulty());

        applyDietaryTags(recipe, request.getDietaryTagIds());
        applyIngredients(recipe, request.getIngredients());
        applySteps(recipe, request.getSteps());

        Recipe saved = recipeRepository.save(recipe);
        eventPublisher.publishEvent(new RecipeCreatedEvent(saved.getId(), saved.getTitle()));
        return toDetail(saved);
    }

    public RecipeDetailDto updateRecipe(Long id, RecipeCreateRequestDto request) {
        validateRequest(request);
        String title = request.getTitle().trim();

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (recipeRepository.existsByTitleIgnoreCaseAndIdNot(title, id)) {
            throw new ConflictException("Recipe already exists with title: " + title);
        }

        recipe.setTitle(title)
                .setDescription(request.getDescription())
                .setCalories(request.getCalories())
                .setProtein(request.getProtein())
                .setCarbs(request.getCarbs())
                .setFat(request.getFat())
                .setCookingTime(request.getCookingTime())
                .setDifficulty(request.getDifficulty());

        if (request.getDietaryTagIds() != null) {
            applyDietaryTags(recipe, request.getDietaryTagIds());
        }

        if (request.getIngredients() != null) {
            applyIngredientsForUpdate(recipe, request.getIngredients());
        }

        if (request.getSteps() != null) {
            applyStepsForUpdate(recipe, request.getSteps());
        }

        return toDetail(recipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        try {
            recipeRepository.delete(recipe);
            recipeRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Recipe is used in meal plans and cannot be deleted", ex);
        }
    }

    @Transactional(readOnly = true)
    public RecipeDetailDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        return toDetail(recipe);
    }

    private void applyDietaryTags(Recipe recipe, List<Long> dietaryTagIds) {
        if (dietaryTagIds == null) {
            return;
        }

        Set<Long> requestedIds = dietaryTagIds.stream()
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (requestedIds.isEmpty()) {
            recipe.setDietaryTags(new LinkedHashSet<>());
            return;
        }

        List<DietaryTag> tags = dietaryTagRepository.findAllById(requestedIds);
        if (tags.size() != requestedIds.size()) {
            Set<Long> foundIds = tags.stream().map(DietaryTag::getId).collect(Collectors.toSet());
            Set<Long> missingIds = requestedIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            throw new BadRequestException("Unknown dietary tag ids: " + missingIds);
        }
        recipe.setDietaryTags(tags.stream().collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private void applyIngredients(Recipe recipe, List<RecipeIngredientInputDto> ingredientInputs) {
        if (ingredientInputs == null) {
            return;
        }

        Set<Long> usedIngredientIds = new LinkedHashSet<>();
        for (int i = 0; i < ingredientInputs.size(); i++) {
            RecipeIngredientInputDto input = ingredientInputs.get(i);
            int row = i + 1;
            if (input == null) {
                throw new BadRequestException("ingredients[" + row + "] is required");
            }
            if (input.getQuantity() == null || input.getQuantity() <= 0) {
                throw new BadRequestException("ingredients[" + row + "].quantity must be > 0");
            }

            String unit = input.getUnit() == null ? null : input.getUnit().trim();
            if (unit == null || unit.isEmpty()) {
                throw new BadRequestException("ingredients[" + row + "].unit is required");
            }

            Ingredient ingredient = resolveIngredient(input, row);
            if (!usedIngredientIds.add(ingredient.getId())) {
                throw new BadRequestException("Duplicate ingredient in request: " + ingredient.getId());
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient()
                    .setRecipe(recipe)
                    .setIngredient(ingredient)
                    .setQuantity(input.getQuantity())
                    .setUnit(unit);
            recipe.getRecipeIngredients().add(recipeIngredient);
        }
    }

    private void applyIngredientsForUpdate(Recipe recipe, List<RecipeIngredientInputDto> ingredientInputs) {
        Map<Long, RecipeIngredient> existingByIngredientId = recipe.getRecipeIngredients().stream()
                .filter(ri -> ri.getIngredient() != null && ri.getIngredient().getId() != null)
                .collect(Collectors.toMap(
                        ri -> ri.getIngredient().getId(),
                        ri -> ri,
                        (left, right) -> left));

        List<RecipeIngredient> updatedIngredients = new ArrayList<>();
        Set<Long> usedIngredientIds = new LinkedHashSet<>();

        for (int i = 0; i < ingredientInputs.size(); i++) {
            RecipeIngredientInputDto input = ingredientInputs.get(i);
            int row = i + 1;
            if (input == null) {
                throw new BadRequestException("ingredients[" + row + "] is required");
            }
            if (input.getQuantity() == null || input.getQuantity() <= 0) {
                throw new BadRequestException("ingredients[" + row + "].quantity must be > 0");
            }

            String unit = input.getUnit() == null ? null : input.getUnit().trim();
            if (unit == null || unit.isEmpty()) {
                throw new BadRequestException("ingredients[" + row + "].unit is required");
            }

            Ingredient ingredient = resolveIngredient(input, row);
            if (!usedIngredientIds.add(ingredient.getId())) {
                throw new BadRequestException("Duplicate ingredient in request: " + ingredient.getId());
            }

            RecipeIngredient recipeIngredient = existingByIngredientId.get(ingredient.getId());
            if (recipeIngredient == null) {
                recipeIngredient = new RecipeIngredient()
                        .setRecipe(recipe)
                        .setIngredient(ingredient);
            }

            recipeIngredient.setQuantity(input.getQuantity())
                    .setUnit(unit);
            updatedIngredients.add(recipeIngredient);
        }

        recipe.getRecipeIngredients().clear();
        recipe.getRecipeIngredients().addAll(updatedIngredients);
    }

    private Ingredient resolveIngredient(RecipeIngredientInputDto input, int row) {
        if (input.getIngredientId() != null) {
            return ingredientRepository.findById(input.getIngredientId())
                    .orElseThrow(() -> new BadRequestException("Unknown ingredient id: " + input.getIngredientId()));
        }

        String ingredientName = input.getName() == null ? null : input.getName().trim();
        if (ingredientName == null || ingredientName.isEmpty()) {
            throw new BadRequestException("ingredients[" + row + "].name is required when ingredientId is missing");
        }
        if (input.getCaloriesPer100g() != null && input.getCaloriesPer100g() < 0) {
            throw new BadRequestException("ingredients[" + row + "].caloriesPer100g must be >= 0");
        }

        return ingredientRepository.findFirstByNameIgnoreCase(ingredientName)
                .orElseGet(() -> ingredientRepository.save(new Ingredient()
                        .setName(ingredientName)
                        .setCaloriesPer100g(input.getCaloriesPer100g())));
    }

    private void applySteps(Recipe recipe, List<RecipeStepInputDto> stepInputs) {
        if (stepInputs == null) {
            return;
        }

        Set<Integer> usedStepNos = new LinkedHashSet<>();
        for (int i = 0; i < stepInputs.size(); i++) {
            RecipeStepInputDto input = stepInputs.get(i);
            int row = i + 1;
            if (input == null) {
                throw new BadRequestException("steps[" + row + "] is required");
            }

            Integer stepNo = input.getStepNo() != null ? input.getStepNo() : row;
            if (stepNo <= 0) {
                throw new BadRequestException("steps[" + row + "].stepNo must be > 0");
            }
            if (!usedStepNos.add(stepNo)) {
                throw new BadRequestException("Duplicate stepNo in request: " + stepNo);
            }

            String description = input.getDescription() == null ? null : input.getDescription().trim();
            if (description == null || description.isEmpty()) {
                throw new BadRequestException("steps[" + row + "].description is required");
            }
            if (input.getDurationMinutes() != null && input.getDurationMinutes() < 0) {
                throw new BadRequestException("steps[" + row + "].durationMinutes must be >= 0");
            }

            RecipeStep step = new RecipeStep()
                    .setRecipe(recipe)
                    .setStepNo(stepNo)
                    .setDescription(description)
                    .setDurationMinutes(input.getDurationMinutes());
            recipe.getSteps().add(step);
        }
    }

    private void applyStepsForUpdate(Recipe recipe, List<RecipeStepInputDto> stepInputs) {
        Map<Integer, RecipeStep> existingByStepNo = recipe.getSteps().stream()
                .filter(step -> step.getStepNo() != null)
                .collect(Collectors.toMap(RecipeStep::getStepNo, step -> step, (left, right) -> left));

        List<RecipeStep> updatedSteps = new ArrayList<>();
        Set<Integer> usedStepNos = new LinkedHashSet<>();

        for (int i = 0; i < stepInputs.size(); i++) {
            RecipeStepInputDto input = stepInputs.get(i);
            int row = i + 1;
            if (input == null) {
                throw new BadRequestException("steps[" + row + "] is required");
            }

            Integer stepNo = input.getStepNo() != null ? input.getStepNo() : row;
            if (stepNo <= 0) {
                throw new BadRequestException("steps[" + row + "].stepNo must be > 0");
            }
            if (!usedStepNos.add(stepNo)) {
                throw new BadRequestException("Duplicate stepNo in request: " + stepNo);
            }

            String description = input.getDescription() == null ? null : input.getDescription().trim();
            if (description == null || description.isEmpty()) {
                throw new BadRequestException("steps[" + row + "].description is required");
            }
            if (input.getDurationMinutes() != null && input.getDurationMinutes() < 0) {
                throw new BadRequestException("steps[" + row + "].durationMinutes must be >= 0");
            }

            RecipeStep step = existingByStepNo.get(stepNo);
            if (step == null) {
                step = new RecipeStep()
                        .setRecipe(recipe)
                        .setStepNo(stepNo);
            }

            step.setDescription(description)
                    .setDurationMinutes(input.getDurationMinutes());
            updatedSteps.add(step);
        }

        recipe.getSteps().clear();
        recipe.getSteps().addAll(updatedSteps);
    }

    private RecipeDetailDto toDetail(Recipe saved) {
        RecipeDetailDto detail = new RecipeDetailDto()
                .setId(saved.getId())
                .setTitle(saved.getTitle())
                .setDescription(saved.getDescription())
                .setCalories(saved.getCalories())
                .setProtein(saved.getProtein())
                .setCarbs(saved.getCarbs())
                .setFat(saved.getFat())
                .setCookingTime(saved.getCookingTime())
                .setDifficulty(saved.getDifficulty());

        detail.setDietaryTags(saved.getDietaryTags().stream()
                .sorted(Comparator.comparing(DietaryTag::getName))
                .map(tag -> new DietaryTagDto().setId(tag.getId()).setName(tag.getName()))
                .collect(Collectors.toList()));

        detail.setIngredients(saved.getRecipeIngredients().stream()
                .sorted(Comparator.comparing(ri -> ri.getIngredient().getName(), String.CASE_INSENSITIVE_ORDER))
                .map(ri -> new RecipeIngredientDto()
                        .setIngredientId(ri.getIngredient().getId())
                        .setName(ri.getIngredient().getName())
                        .setCaloriesPer100g(ri.getIngredient().getCaloriesPer100g())
                        .setQuantity(ri.getQuantity())
                        .setUnit(ri.getUnit()))
                .collect(Collectors.toList()));

        detail.setSteps(saved.getSteps().stream()
                .sorted(Comparator.comparing(RecipeStep::getStepNo))
                .map(step -> new RecipeStepDto()
                        .setStepNo(step.getStepNo())
                        .setDescription(step.getDescription())
                        .setDurationMinutes(step.getDurationMinutes()))
                .collect(Collectors.toList()));

        return detail;
    }

    private void validateRequest(RecipeCreateRequestDto request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        String title = request.getTitle() == null ? null : request.getTitle().trim();
        if (title == null || title.isEmpty()) {
            throw new BadRequestException("title is required");
        }
        if (request.getCalories() != null && request.getCalories() < 0) {
            throw new BadRequestException("calories must be >= 0");
        }
        if (request.getCookingTime() != null && request.getCookingTime() < 0) {
            throw new BadRequestException("cookingTime must be >= 0");
        }
        if (request.getProtein() != null && request.getProtein() < 0) {
            throw new BadRequestException("protein must be >= 0");
        }
        if (request.getCarbs() != null && request.getCarbs() < 0) {
            throw new BadRequestException("carbs must be >= 0");
        }
        if (request.getFat() != null && request.getFat() < 0) {
            throw new BadRequestException("fat must be >= 0");
        }
    }
}

