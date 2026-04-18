package com.backend.service;

import com.backend.entity.MealPlan;
import com.backend.entity.MealPlanEntry;
import com.backend.entity.Profile;
import com.backend.entity.Recipe;
import com.backend.entity.User;
import com.backend.entity.enums.MealType;
import com.backend.exception.BadRequestException;
import com.backend.repository.MealPlanEntryRepository;
import com.backend.repository.MealPlanRepository;
import com.backend.repository.ProfileRepository;
import com.backend.repository.RecipeRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.AssignRecipeToSlotRequestDto;
import com.backend.service.dto.CalendarDayResponseDto;
import com.backend.service.dto.MealSlotStateDto;
import com.backend.service.dto.RecipeSummaryDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@Transactional
public class CalendarService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final MealPlanRepository mealPlanRepository;
    private final MealPlanEntryRepository mealPlanEntryRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeRecommendationService recipeRecommendationService;

    public CalendarService(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            MealPlanRepository mealPlanRepository,
            MealPlanEntryRepository mealPlanEntryRepository,
            RecipeRepository recipeRepository,
            RecipeRecommendationService recipeRecommendationService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.mealPlanEntryRepository = mealPlanEntryRepository;
        this.recipeRepository = recipeRepository;
        this.recipeRecommendationService = recipeRecommendationService;
    }

    public CalendarDayResponseDto getDay(LocalDate date) {
        User user = currentUser();
        Profile profile = loadProfile(user);
        MealPlan plan = resolveCalendarPlan(user);
        int mealsPerDay = profile.getMealsPerDay();

        Map<Integer, Recipe> bySlot = new HashMap<>();
        for (MealPlanEntry e : mealPlanEntryRepository.findByMealPlan_IdAndMealDate(plan.getId(), date)) {
            bySlot.put(e.getMealSlotIndex(), e.getRecipe());
        }

        CalendarDayResponseDto dto = new CalendarDayResponseDto()
                .setDate(date)
                .setMealsPerDay(mealsPerDay);

        IntStream.range(0, mealsPerDay).forEach(i -> {
            MealSlotStateDto slot = new MealSlotStateDto().setSlotIndex(i);
            Recipe r = bySlot.get(i);
            if (r != null) {
                slot.setRecipe(toSummary(r));
            }
            dto.getSlots().add(slot);
        });

        return dto;
    }

    public CalendarDayResponseDto assignRecipe(AssignRecipeToSlotRequestDto body) {
        if (body.getDate() == null || body.getSlotIndex() == null || body.getRecipeId() == null) {
            throw new BadRequestException("date, slotIndex and recipeId are required");
        }

        User user = currentUser();
        Profile profile = loadProfile(user);
        int mealsPerDay = profile.getMealsPerDay();
        if (body.getSlotIndex() < 0 || body.getSlotIndex() >= mealsPerDay) {
            throw new BadRequestException("slotIndex must be between 0 and " + (mealsPerDay - 1));
        }

        Recipe recipe = recipeRepository.findById(body.getRecipeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipeRecommendationService.matchesRestrictions(recipe, profile)) {
            throw new BadRequestException("Recipe does not match your dietary restrictions");
        }

        MealPlan plan = resolveCalendarPlan(user);

        MealPlanEntry entry = mealPlanEntryRepository
                .findByMealPlan_IdAndMealDateAndMealSlotIndex(plan.getId(), body.getDate(), body.getSlotIndex())
                .orElseGet(MealPlanEntry::new);

        entry.setMealPlan(plan);
        entry.setMealDate(body.getDate());
        entry.setMealSlotIndex(body.getSlotIndex());
        entry.setRecipe(recipe);
        entry.setMealType(mealTypeForSlot(body.getSlotIndex()));
        mealPlanEntryRepository.save(entry);

        return getDay(body.getDate());
    }

    private MealType mealTypeForSlot(int slot) {
        return switch (slot) {
            case 0 -> MealType.BREAKFAST;
            case 1 -> MealType.LUNCH;
            case 2 -> MealType.DINNER;
            default -> MealType.SNACK;
        };
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Profile loadProfile(User user) {
        return profileRepository.findByUser_Email(user.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    private MealPlan resolveCalendarPlan(User user) {
        return mealPlanRepository.findFirstByUser_IdOrderByIdAsc(user.getId())
                .orElseGet(() -> mealPlanRepository.save(
                        new MealPlan()
                                .setUser(user)
                                .setStartDate(LocalDate.now().minusYears(5))
                                .setEndDate(LocalDate.now().plusYears(5))
                ));
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
