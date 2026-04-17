package com.backend.repository;

import com.backend.entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /** Încarcă user-ul; tag-urile se încarcă lazy (al doilea SELECT), ca să evităm un singur SQL uriaș. */
    @EntityGraph(attributePaths = {"user"})
    Optional<Profile> findByUser_Email(String email);
}
