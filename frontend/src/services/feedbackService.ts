import { api } from './api'

export interface FeedbackResponse {
  id: number
  email: string
  name: string
  rating: number | null
  message: string
}

export const feedbackService = {
  submit: (data: {
    category?: string
    satisfaction?: string
    subscribe?: boolean
    rating?: number
    message: string
  }) => api.post<FeedbackResponse>('/feedback', data),
}
