package com.backend.service.dto;

public class FeedbackResponseDto {

	private Long id;
	private String email;
	private String name;
	private Integer rating;
	private String message;

	public Long getId() {
		return id;
	}

	public FeedbackResponseDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public FeedbackResponseDto setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getName() {
		return name;
	}

	public FeedbackResponseDto setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getRating() {
		return rating;
	}

	public FeedbackResponseDto setRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public FeedbackResponseDto setMessage(String message) {
		this.message = message;
		return this;
	}
}

