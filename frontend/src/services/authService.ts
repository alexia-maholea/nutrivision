import { api } from './api'

export interface LoginResponse {
  access_token: string
  token_type: string
  expires_in: number
}

export const authService = {
  login: (email: string, password: string) =>
    api.post<LoginResponse>('/auth/login', { email, password }),

  register: (name: string, email: string, password: string) =>
    api.post<void>('/auth/register', { name, email, password }),
}
