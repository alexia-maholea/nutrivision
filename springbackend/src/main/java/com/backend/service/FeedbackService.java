package com.backend.service;

import com.backend.entity.Feedback;
import com.backend.entity.User;
import com.backend.exception.BadRequestException;
import com.backend.repository.FeedbackRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.FeedbackCreateRequestDto;
import com.backend.service.dto.FeedbackResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class FeedbackService {

    private static final String GENERAL_CATEGORY = "GENERAL";

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    public FeedbackResponseDto submitFeedback(FeedbackCreateRequestDto request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        String message = request.getMessage() == null ? null : request.getMessage().trim();
        if (message == null || message.isEmpty()) {
            throw new BadRequestException("message is required");
        }
        if (request.getRating() != null && (request.getRating() < 1 || request.getRating() > 5)) {
            throw new BadRequestException("rating must be between 1 and 5");
        }

        User user = getAuthenticatedUser();

        Feedback feedback = new Feedback()
                .setUser(user)
                .setCategory(GENERAL_CATEGORY)
                .setRating(request.getRating())
                .setMessage(message)
                .setWantsNewsletter(null);

        return toDto(feedbackRepository.save(feedback));
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponseDto> getGeneralFeedbackForAdmin(String q, Pageable pageable) {
        String normalizedQ = normalizeQuery(q);
        return feedbackRepository.searchGeneralFeedback(normalizedQ, pageable).map(this::toDto);
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private FeedbackResponseDto toDto(Feedback feedback) {
        return new FeedbackResponseDto()
                .setId(feedback.getId())
                .setEmail(feedback.getUser().getEmail())
                .setName(feedback.getUser().getName())
                .setRating(feedback.getRating())
                .setMessage(feedback.getMessage());
    }

    private String normalizeQuery(String q) {
        if (q == null) {
            return null;
        }
        String trimmed = q.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

