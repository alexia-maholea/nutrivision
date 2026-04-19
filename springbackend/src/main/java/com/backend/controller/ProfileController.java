package com.backend.controller;

import com.backend.service.ProfileService;
import com.backend.service.NewsletterService;
import com.backend.service.dto.ProfileResponseDto;
import com.backend.service.dto.ProfileUpdateRequestDto;
import com.backend.service.dto.NewsletterSubscriptionRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.backend.service.dto.MealsPerDayRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile", description = "User nutrition profile (goals, restrictions, targets)")
public class ProfileController implements SecuredRestController {

    private final ProfileService profileService;
    private final NewsletterService newsletterService;

    public ProfileController(ProfileService profileService, NewsletterService newsletterService) {
        this.profileService = profileService;
        this.newsletterService = newsletterService;
    }

    @GetMapping
    @Operation(summary = "Get my profile", description = "Returns goals, activity level, calorie target, anthropometrics, dietary restriction tags, and mealsPerDay for the calendar.")
    public ResponseEntity<ProfileResponseDto> getMyProfile() {
        return ResponseEntity.ok(profileService.getCurrentUserProfile());
    }

    @PatchMapping
    @Operation(summary = "Update my profile", description = "Partial update for age/height/weight/gender/activityLevel/goal/dailyCaloriesTarget and dietaryRestrictionTagIds.")
    public ResponseEntity<ProfileResponseDto> patchMyProfile(@RequestBody ProfileUpdateRequestDto body) {
        return ResponseEntity.ok(profileService.updateCurrentUserProfile(body));
    }

    @PatchMapping("/meals-per-day")
    @Operation(summary = "Set meals per day", description = "How many meal slots you want each day in the calendar (2–10).")
    public ResponseEntity<ProfileResponseDto> patchMealsPerDay(@RequestBody MealsPerDayRequestDto body) {
        return ResponseEntity.ok(profileService.updateMealsPerDay(body));
    }

    @PatchMapping("/newsletter")
    @Operation(summary = "Subscribe/unsubscribe newsletter", description = "Set true to receive email when new recipes are added.")
    public ResponseEntity<Void> patchNewsletter(@RequestBody NewsletterSubscriptionRequestDto body) {
        newsletterService.updateMySubscription(body);
        return ResponseEntity.noContent().build();
    }
}
