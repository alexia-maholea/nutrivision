import { useState } from 'react'
import { feedbackService } from './services/feedbackService'
import './Feedback.css'

function StarIcon({ filled }: { filled: boolean }) {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill={filled ? '#16a34a' : 'none'} stroke={filled ? '#16a34a' : '#e4e6e1'} strokeWidth="2">
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  )
}

export default function Feedback({
  token,
  onRequireAuth,
}: {
  token: string | null
  onRequireAuth: () => void
}) {
  const [category, setCategory] = useState('bug')
  const [satisfaction, setSatisfaction] = useState('good')
  const [subscribe, setSubscribe] = useState(false)
  const [rating, setRating] = useState(0)
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')
    setSuccess(false)

    if (!message.trim()) {
      setError('Mesajul feedback-ului este obligatoriu')
      return
    }

    setLoading(true)
    try {
      await feedbackService.submit({
        category,
        satisfaction,
        subscribe,
        rating: rating > 0 ? rating : undefined,
        message,
      })
      setSuccess(true)
      setCategory('bug')
      setSatisfaction('good')
      setSubscribe(false)
      setRating(0)
      setMessage('')
      setTimeout(() => setSuccess(false), 5000)
    } catch (err) {
      const msg = err instanceof Error ? err.message : 'Nu s-a putut trimite feedback-ul'
      console.error('[Feedback Error]', msg)
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  if (!token) {
    return (
      <div className="feedback-page">
        <div className="feedback-auth-prompt">
          <h2>Conectează-te pentru a trimite feedback</h2>
          <p>Părerea ta ne ajută să îmbunătățim NutriVision.</p>
          <button className="feedback-auth-btn" onClick={onRequireAuth}>Conectează-te →</button>
        </div>
      </div>
    )
  }

  return (
    <div className="feedback-page">
      <div className="container feedback-container">
        <div className="feedback-header">
          <h1>Trimite-ne feedback</h1>
          <p>Ajută-ne să îmbunătățim NutriVision cu sugestiile și observațiile tale.</p>
        </div>

        <form className="feedback-form" onSubmit={handleSubmit}>
          {/* Category Select */}
          <div className="form-group">
            <label htmlFor="category">Tip de feedback *</label>
            <select
              id="category"
              value={category}
              onChange={e => setCategory(e.target.value)}
              className="form-select"
            >
              <option value="bug">🐛 Raport de eroare</option>
              <option value="feature">✨ Idee nouă</option>
              <option value="improvement">⚡ Îmbunătățire</option>
              <option value="other">💭 Altul</option>
            </select>
          </div>

          {/* Satisfaction Radio Buttons */}
          <div className="form-group">
            <label>Cât de mulțumit ești? *</label>
            <div className="radio-group">
              <label className="radio-option">
                <input
                  type="radio"
                  name="satisfaction"
                  value="very_unsatisfied"
                  checked={satisfaction === 'very_unsatisfied'}
                  onChange={e => setSatisfaction(e.target.value)}
                />
                <span>😢 Foarte nemulțumit</span>
              </label>
              <label className="radio-option">
                <input
                  type="radio"
                  name="satisfaction"
                  value="unsatisfied"
                  checked={satisfaction === 'unsatisfied'}
                  onChange={e => setSatisfaction(e.target.value)}
                />
                <span>😕 Nemulțumit</span>
              </label>
              <label className="radio-option">
                <input
                  type="radio"
                  name="satisfaction"
                  value="neutral"
                  checked={satisfaction === 'neutral'}
                  onChange={e => setSatisfaction(e.target.value)}
                />
                <span>😐 Neutru</span>
              </label>
              <label className="radio-option">
                <input
                  type="radio"
                  name="satisfaction"
                  value="good"
                  checked={satisfaction === 'good'}
                  onChange={e => setSatisfaction(e.target.value)}
                />
                <span>🙂 Mulțumit</span>
              </label>
              <label className="radio-option">
                <input
                  type="radio"
                  name="satisfaction"
                  value="very_satisfied"
                  checked={satisfaction === 'very_satisfied'}
                  onChange={e => setSatisfaction(e.target.value)}
                />
                <span>😄 Foarte mulțumit</span>
              </label>
            </div>
          </div>

          {/* Star Rating */}
          <div className="form-group">
            <label>Evaluare (opțional)</label>
            <div className="stars-container">
              {[1, 2, 3, 4, 5].map(star => (
                <button
                  key={star}
                  type="button"
                  className="star-btn"
                  onClick={() => setRating(rating === star ? 0 : star)}
                  title={`${star} stele`}
                >
                  <StarIcon filled={star <= rating} />
                </button>
              ))}
              {rating > 0 && <span className="rating-text">{rating}/5 stele</span>}
            </div>
          </div>

          {/* Checkbox Subscribe */}
          <div className="form-group">
            <label className="checkbox-option">
              <input
                type="checkbox"
                checked={subscribe}
                onChange={e => setSubscribe(e.target.checked)}
              />
              <span>Vreau să primesc actualizări despre schimbările din NutriVision</span>
            </label>
          </div>

          {/* Message Textarea */}
          <div className="form-group">
            <label htmlFor="message">Mesajul tău *</label>
            <textarea
              id="message"
              value={message}
              onChange={e => setMessage(e.target.value)}
              placeholder="Spune-ne mai mult despre feedback-ul tău..."
              rows={6}
              className="form-textarea"
            />
            <span className="char-count">{message.length} caractere</span>
          </div>

          {/* Messages */}
          {error && <div className="feedback-error">{error}</div>}
          {success && <div className="feedback-success">✓ Feedback-ul a fost trimis cu succes! Mulțumim!</div>}

          {/* Submit Button */}
          <div className="form-actions">
            <button type="submit" className="btn-submit" disabled={loading}>
              {loading ? 'Se trimite...' : 'Trimite feedback →'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
