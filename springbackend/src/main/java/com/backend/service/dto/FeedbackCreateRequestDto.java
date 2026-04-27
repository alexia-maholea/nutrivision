package com.backend.service.dto;

public class FeedbackCreateRequestDto {

	private String category;
	private String satisfaction;
	private Boolean subscribe;
	private Integer rating;
	private String message;

	public String getCategory() {
		return category;
	}

	public FeedbackCreateRequestDto setCategory(String category) {
		this.category = category;
		return this;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public FeedbackCreateRequestDto setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
		return this;
	}

	public Boolean getSubscribe() {
		return subscribe;
	}

	public FeedbackCreateRequestDto setSubscribe(Boolean subscribe) {
		this.subscribe = subscribe;
		return this;
	}

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


