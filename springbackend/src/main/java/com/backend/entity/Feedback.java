package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback", schema = "project")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "wants_newsletter")
    private Boolean wantsNewsletter;

    @Column(name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public Feedback setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRating() {
        return rating;
    }

    public Feedback setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Feedback setMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean getWantsNewsletter() {
        return wantsNewsletter;
    }

    public Feedback setWantsNewsletter(Boolean wantsNewsletter) {
        this.wantsNewsletter = wantsNewsletter;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Feedback setCategory(String category) {
        this.category = category;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Feedback setUser(User user) {
        this.user = user;
        return this;
    }
}
