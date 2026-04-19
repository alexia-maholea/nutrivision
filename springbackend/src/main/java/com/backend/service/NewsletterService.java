package com.backend.service;

import com.backend.entity.Feedback;
import com.backend.entity.User;
import com.backend.exception.BadRequestException;
import com.backend.repository.FeedbackRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.NewsletterSubscriptionRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class NewsletterService {

	private static final String NEWSLETTER_CATEGORY = "NEWSLETTER";

	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;

	public NewsletterService(FeedbackRepository feedbackRepository, UserRepository userRepository) {
		this.feedbackRepository = feedbackRepository;
		this.userRepository = userRepository;
	}

	public void updateMySubscription(NewsletterSubscriptionRequestDto request) {
		if (request == null || request.getSubscribed() == null) {
			throw new BadRequestException("subscribed must be provided");
		}

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findUserByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

		Feedback preference = feedbackRepository.findFirstByUser_IdAndCategoryOrderByIdDesc(user.getId(), NEWSLETTER_CATEGORY)
				.orElseGet(() -> new Feedback().setUser(user).setCategory(NEWSLETTER_CATEGORY));

		preference.setWantsNewsletter(request.getSubscribed());
		preference.setMessage(request.getSubscribed()
				? "User subscribed to recipe newsletter"
				: "User unsubscribed from recipe newsletter");

		feedbackRepository.save(preference);
	}
}

