import { api } from './api'

export interface MeResponse {
  email: string
  name: string
  role: string
}

export const userService = {
  getCurrentUser: () =>
    api.get<MeResponse>('/me'),
}
