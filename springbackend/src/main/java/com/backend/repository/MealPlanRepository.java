package com.backend.repository;

import com.backend.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    Optional<MealPlan> findFirstByUser_IdOrderByIdAsc(Long userId);
}
