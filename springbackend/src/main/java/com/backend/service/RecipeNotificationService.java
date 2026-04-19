package com.backend.service;

import com.backend.repository.FeedbackRepository;
import com.backend.service.event.RecipeCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
public class RecipeNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(RecipeNotificationService.class);

    private final FeedbackRepository feedbackRepository;
    private final JavaMailSender mailSender;

    @Value("${mail.from:no-reply@nutrivision.local}")
    private String from;

    public RecipeNotificationService(FeedbackRepository feedbackRepository, JavaMailSender mailSender) {
        this.feedbackRepository = feedbackRepository;
        this.mailSender = mailSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRecipeCreated(RecipeCreatedEvent event) {
        List<String> subscribedEmails = feedbackRepository.findSubscribedNewsletterEmails();
        if (subscribedEmails.isEmpty()) {
            return;
        }

        for (String email : subscribedEmails) {
            try {
                sendRecipeCreatedEmail(email, event);
            } catch (Exception ex) {
                logger.warn("Failed to send recipe notification to {} for recipe {}", email, event.getRecipeId(), ex);
            }
        }
    }

    private void sendRecipeCreatedEmail(String to, RecipeCreatedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("New recipe available: " + event.getRecipeTitle());
        message.setText("Hello,\n\nA new recipe was added: " + event.getRecipeTitle()
                + " (ID: " + event.getRecipeId() + ").\n"
                + "Open NutriVision to check it out.\n\n"
                + "You receive this because you subscribed to newsletter updates.");
        mailSender.send(message);
    }
}


