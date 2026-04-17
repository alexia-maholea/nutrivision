package com.backend.controller;

import com.backend.service.ProfileService;
import com.backend.service.dto.ProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile", description = "User nutrition profile (goals, restrictions, targets)")
public class ProfileController implements SecuredRestController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get my profile", description = "Returns goals, activity level, calorie target, anthropometrics, and dietary restriction tags.")
    public ResponseEntity<ProfileResponseDto> getMyProfile() {
        return ResponseEntity.ok(profileService.getCurrentUserProfile());
    }
}
