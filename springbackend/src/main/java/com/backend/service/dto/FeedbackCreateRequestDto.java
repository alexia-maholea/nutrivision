package com.backend.service.dto;

public class FeedbackCreateRequestDto {

	private Integer rating;
	private String message;

	public Integer getRating() {
		return rating;
	}

	public FeedbackCreateRequestDto setRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public FeedbackCreateRequestDto setMessage(String message) {
		this.message = message;
		return this;
	}
}

