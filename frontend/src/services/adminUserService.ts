import { api } from './api'

export interface DietaryTag {
  id: number
  name: string
}

export interface UserProfile {
  email: string
  name: string
  role: string
  age: number | null
  height: number | null
  weight: number | null
  gender: string | null
  activityLevel: string | null
  goal: string | null
  dailyCaloriesTarget: number | null
  mealsPerDay: number | null
  dietaryRestrictions: DietaryTag[]
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

export const adminUserService = {
  all: (page: number, size: number, q?: string) =>
    api.get<PagedResponse<UserProfile>>(`/admin/users?${buildParams(page, size, q)}`),
}
