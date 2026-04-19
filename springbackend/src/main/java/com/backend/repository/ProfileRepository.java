package com.backend.repository;

import com.backend.entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = {"user", "dietaryRestrictions"})
    Optional<Profile> findByUser_Email(String email);

    @EntityGraph(attributePaths = {"user", "dietaryRestrictions"})
    @Query("SELECT p FROM Profile p ORDER BY p.id ASC")
    List<Profile> findAllForAdminListing();
}
