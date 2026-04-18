package com.backend.repository;

import com.backend.entity.DietaryTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietaryTagRepository extends JpaRepository<DietaryTag, Long> {
}

