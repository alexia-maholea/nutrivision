package com.backend.controller;

import com.backend.service.ProfileService;
import com.backend.service.dto.PagedResponseDto;
import com.backend.service.dto.ProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "List users", description = "Returns paginated users with role and profile details. Supports q search by name/email. Admin role required.")
    public ResponseEntity<PagedResponseDto<ProfileResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(PagedResponseDto.from(profileService.getProfilesForAdmin(
                q,
                PageRequest.of(page, size, Sort.by("id").ascending()))));
    }
}


