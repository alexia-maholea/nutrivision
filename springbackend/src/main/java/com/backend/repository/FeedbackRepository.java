package com.backend.repository;

import com.backend.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findFirstByUser_IdAndCategoryOrderByIdDesc(Long userId, String category);

    @Query("""
            SELECT f.user.email
            FROM Feedback f
            WHERE f.category = 'NEWSLETTER'
              AND f.wantsNewsletter = true
              AND f.id = (
                    SELECT MAX(f2.id)
                    FROM Feedback f2
                    WHERE f2.user.id = f.user.id
                      AND f2.category = 'NEWSLETTER'
              )
            """)
    List<String> findSubscribedNewsletterEmails();

    @Query(value = """
            SELECT f
            FROM Feedback f
            JOIN f.user u
            WHERE f.category = 'GENERAL'
              AND (:q IS NULL
                OR COALESCE(f.message, '') ILIKE CONCAT('%', CAST(:q AS text), '%')
                OR u.email ILIKE CONCAT('%', CAST(:q AS text), '%')
                OR u.name ILIKE CONCAT('%', CAST(:q AS text), '%'))
            """,
            countQuery = """
            SELECT COUNT(f)
            FROM Feedback f
            JOIN f.user u
            WHERE f.category = 'GENERAL'
              AND (:q IS NULL
                OR COALESCE(f.message, '') ILIKE CONCAT('%', CAST(:q AS text), '%')
                OR u.email ILIKE CONCAT('%', CAST(:q AS text), '%')
                OR u.name ILIKE CONCAT('%', CAST(:q AS text), '%'))
            """)
    Page<Feedback> searchGeneralFeedback(@Param("q") String q, Pageable pageable);
}


