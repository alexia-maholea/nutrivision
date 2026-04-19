package com.backend.service.dto;

public class NewsletterSubscriptionRequestDto {

    private Boolean subscribed;

    public Boolean getSubscribed() {
        return subscribed;
    }

    public NewsletterSubscriptionRequestDto setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
        return this;
    }
}

