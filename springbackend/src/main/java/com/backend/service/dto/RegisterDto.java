package com.backend.service.dto;

import org.springframework.stereotype.Component;

@Component
public class RegisterDto {

    private String email;
    private String name;
    private String password;

    public String getEmail() {
        return email;
    }

    public RegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public RegisterDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
