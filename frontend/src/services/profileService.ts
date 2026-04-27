import { api } from './api'

export interface DietaryTag { id: number; name: string }

export interface ProfileData {
  email: string
  name: string
  role: string
  age: number | null
  height: number | null
  weight: number | null
  gender: 'MALE' | 'FEMALE' | 'OTHER' | null
  activityLevel: string | null
  goal: 'LOSE_WEIGHT' | 'MAINTAIN' | 'GAIN_WEIGHT' | null
  dailyCaloriesTarget: number | null
  mealsPerDay: number
  dietaryRestrictions: DietaryTag[]
}

// Frontend goal ID → backend enum
export const GOAL_TO_BACKEND: Record<string, string> = {
  slabire: 'LOSE_WEIGHT',
  masa: 'GAIN_WEIGHT',
  mentinere: 'MAINTAIN',
  energie: 'MAINTAIN',
  sanatate: 'MAINTAIN',
  detox: 'MAINTAIN',
}

// Backend enum → frontend goal ID (prefer the canonical label)
export const BACKEND_TO_GOAL: Record<string, string> = {
  LOSE_WEIGHT: 'slabire',
  GAIN_WEIGHT: 'masa',
  MAINTAIN: 'mentinere',
}

// Frontend restriction ID → backend dietary_tag ID (from V1 seed)
export const RESTRICTION_TO_TAG_ID: Record<string, number> = {
  vegan: 1,
  vegetarian: 2,
  gluten: 3,
  keto: 4,
  lactoza: 5,
}

// Backend tag name → frontend restriction ID
export const TAG_NAME_TO_RESTRICTION: Record<string, string> = {
  VEGAN: 'vegan',
  VEGETARIAN: 'vegetarian',
  GLUTEN_FREE: 'gluten',
  KETO: 'keto',
  LACTOSE_FREE: 'lactoza',
}

export const profileService = {
  get: () => api.get<ProfileData>('/profile'),

  update: (data: { goal?: string; dietaryRestrictionTagIds?: number[] }) =>
    api.patch<ProfileData>('/profile', data),
}
