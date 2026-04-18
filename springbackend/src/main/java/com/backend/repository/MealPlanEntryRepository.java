package com.backend.repository;

import com.backend.entity.MealPlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealPlanEntryRepository extends JpaRepository<MealPlanEntry, Long> {

    List<MealPlanEntry> findByMealPlan_IdAndMealDate(Long mealPlanId, LocalDate mealDate);

    Optional<MealPlanEntry> findByMealPlan_IdAndMealDateAndMealSlotIndex(Long mealPlanId, LocalDate mealDate, Integer mealSlotIndex);
}
