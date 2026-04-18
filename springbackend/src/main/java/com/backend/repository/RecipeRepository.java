package com.backend.repository;

import com.backend.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @EntityGraph(attributePaths = {"dietaryTags"})
    @Query("SELECT r FROM Recipe r ORDER BY r.title ASC")
    List<Recipe> findAllByOrderByTitleAsc();
}
