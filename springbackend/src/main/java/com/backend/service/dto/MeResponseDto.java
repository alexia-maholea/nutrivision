package com.backend.service.dto;

public class MeResponseDto {

    private String email;
    private String name;
    private String role;

    public String getEmail() {
        return email;
    }

    public MeResponseDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public MeResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getRole() {
        return role;
    }

    public MeResponseDto setRole(String role) {
        this.role = role;
        return this;
    }
}

