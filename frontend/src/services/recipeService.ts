import { api } from './api'

export interface RecipeSummary {
  id: number
  title: string
  calories: number | null
  cookingTime: number | null
  difficulty: 'EASY' | 'MEDIUM' | 'HARD' | null
}

export interface DietaryTag { id: number; name: string }

export interface RecipeIngredient {
  ingredientId: number
  name: string
  caloriesPer100g: number | null
  quantity: number | null
  unit: string | null
}

export interface RecipeStep {
  stepNo: number
  description: string
  durationMinutes: number | null
}

export interface RecipeDetail {
  id: number
  title: string
  description: string | null
  calories: number | null
  protein: number | null
  carbs: number | null
  fat: number | null
  cookingTime: number | null
  difficulty: 'EASY' | 'MEDIUM' | 'HARD' | null
  dietaryTags: DietaryTag[]
  ingredients: RecipeIngredient[]
  steps: RecipeStep[]
}

export interface PagedResponse<T> {
  items: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  hasNext: boolean
  hasPrevious: boolean
}

function buildParams(page: number, size: number, q?: string) {
  const p = new URLSearchParams({ page: String(page), size: String(size) })
  if (q) p.set('q', q)
  return p.toString()
}

export const recipeService = {
  all: (page: number, size: number, q?: string) =>
    api.get<PagedResponse<RecipeSummary>>(`/recipes/all?${buildParams(page, size, q)}`),

  my: (page: number, size: number, q?: string) =>
    api.get<PagedResponse<RecipeSummary>>(`/recipes/my?${buildParams(page, size, q)}`),

  getById: (id: number) =>
    api.get<RecipeDetail>(`/recipes/${id}`),
}
