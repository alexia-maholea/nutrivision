package com.backend.controller;

import com.backend.service.ProfileService;
import com.backend.service.dto.MeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
@Tag(name = "Me", description = "Current authenticated user identity")
public class MeController implements SecuredRestController {

    private final ProfileService profileService;

    public MeController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Current user", description = "Returns the authenticated user email, name and role.")
    public ResponseEntity<MeResponseDto> getCurrentUser() {
        return ResponseEntity.ok(profileService.getCurrentUserIdentity());
    }
}

