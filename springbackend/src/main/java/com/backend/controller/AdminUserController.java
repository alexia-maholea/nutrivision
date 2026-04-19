package com.backend.controller;

import com.backend.service.ProfileService;
import com.backend.service.dto.ProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin Users", description = "Admin endpoints for viewing users and profile data")
public class AdminUserController implements SecuredRestController {

    private final ProfileService profileService;

    public AdminUserController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "List users", description = "Returns all users with role and profile details. Admin role required.")
    public ResponseEntity<List<ProfileResponseDto>> getAllUsers() {
        return ResponseEntity.ok(profileService.getAllProfilesForAdmin());
    }
}


