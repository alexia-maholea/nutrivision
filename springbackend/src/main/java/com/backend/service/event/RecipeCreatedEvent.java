package com.backend.service.event;

public class RecipeCreatedEvent {

	private final Long recipeId;
	private final String recipeTitle;

	public RecipeCreatedEvent(Long recipeId, String recipeTitle) {
		this.recipeId = recipeId;
		this.recipeTitle = recipeTitle;
	}

	public Long getRecipeId() {
		return recipeId;
	}

	public String getRecipeTitle() {
		return recipeTitle;
	}
}

