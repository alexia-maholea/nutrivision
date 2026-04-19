package com.backend.repository;

import com.backend.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = {"user", "dietaryRestrictions"})
    Optional<Profile> findByUser_Email(String email);

    @EntityGraph(attributePaths = {"user", "dietaryRestrictions"})
    @Query("SELECT p FROM Profile p ORDER BY p.id ASC")
    List<Profile> findAllForAdminListing();

    @EntityGraph(attributePaths = {"user", "dietaryRestrictions"})
    @Query(value = """
            SELECT p
            FROM Profile p
            JOIN p.user u
            WHERE (:q IS NULL
               OR LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))
            """,
            countQuery = """
            SELECT COUNT(p)
            FROM Profile p
            JOIN p.user u
            WHERE (:q IS NULL
               OR LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<Profile> searchForAdminListing(@Param("q") String q, Pageable pageable);
}
