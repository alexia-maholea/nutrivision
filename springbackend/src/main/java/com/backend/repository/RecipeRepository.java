package com.backend.repository;

import com.backend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByTitleAsc();

    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(value = """
            SELECT r
            FROM Recipe r
            WHERE (:q IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (
                SELECT COUNT(DISTINCT dt.id)
                FROM Recipe r2
                JOIN r2.dietaryTags dt
                WHERE r2 = r
                  AND dt.id IN :requiredTagIds
              ) = :requiredTagCount
            """,
            countQuery = """
            SELECT COUNT(r)
            FROM Recipe r
            WHERE (:q IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (
                SELECT COUNT(DISTINCT dt.id)
                FROM Recipe r2
                JOIN r2.dietaryTags dt
                WHERE r2 = r
                  AND dt.id IN :requiredTagIds
              ) = :requiredTagCount
            """)
    Page<Recipe> findRecommendedByRequiredTagIds(@Param("requiredTagIds") Set<Long> requiredTagIds,
                                                  @Param("requiredTagCount") long requiredTagCount,
                                                  @Param("q") String q,
                                                  Pageable pageable);
}
