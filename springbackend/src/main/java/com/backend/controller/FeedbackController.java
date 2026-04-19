package com.backend.controller;

import com.backend.service.FeedbackService;
import com.backend.service.dto.FeedbackCreateRequestDto;
import com.backend.service.dto.FeedbackResponseDto;
import com.backend.service.dto.PagedResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feedback")
@Tag(name = "Feedback", description = "Submit and review application feedback")
public class FeedbackController implements SecuredRestController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @Operation(summary = "Submit feedback", description = "Stores user feedback message and optional rating (1-5).")
    public ResponseEntity<FeedbackResponseDto> submitFeedback(@RequestBody FeedbackCreateRequestDto body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.submitFeedback(body));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "List feedback", description = "Returns paginated GENERAL feedback entries with q search over message/name/email. Admin role required.")
    public ResponseEntity<PagedResponseDto<FeedbackResponseDto>> getGeneralFeedbackForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(PagedResponseDto.from(feedbackService.getGeneralFeedbackForAdmin(
                q,
                PageRequest.of(page, size, Sort.by("id").descending()))));
    }
}

