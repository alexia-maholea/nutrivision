import { useState } from 'react'
import { authService } from './services/authService'
import { userService } from './services/userService'
import './Auth.css'

type Mode = 'login' | 'register'

function LeafIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
      <path d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z" fill="white"/>
      <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="white" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  )
}

function EyeIcon({ visible }: { visible: boolean }) {
  return visible ? (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke="#9ca3af" strokeWidth="1.8"/>
      <circle cx="12" cy="12" r="3" stroke="#9ca3af" strokeWidth="1.8"/>
    </svg>
  ) : (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24" stroke="#9ca3af" strokeWidth="1.8" strokeLinecap="round"/>
      <line x1="1" y1="1" x2="23" y2="23" stroke="#9ca3af" strokeWidth="1.8" strokeLinecap="round"/>
    </svg>
  )
}

export default function Auth({
  onClose,
  onAuthSuccess,
}: {
  onClose: () => void
  onAuthSuccess: (token: string) => void
}) {
  const [mode, setMode] = useState<Mode>('login')
  const [showPass, setShowPass] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  function switchMode(next: Mode) {
    setMode(next)
    setError('')
    setPassword('')
    setConfirm('')
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')

    if (mode === 'register' && password !== confirm) {
      setError('Parolele nu coincid.')
      return
    }

    setLoading(true)
    try {
      if (mode === 'register') {
        await authService.register(name, email, password)
      }
      const res = await authService.login(email, password)
      localStorage.setItem('token', res.access_token)

      // Fetch and cache user role
      try {
        const user = await userService.getCurrentUser()
        console.log('[Auth] User fetched:', user)
        localStorage.setItem('userRole', user.role)
        console.log('[Auth] Stored role:', user.role)
      } catch (err) {
        console.error('[Auth] Failed to fetch user:', err)
      }

      onAuthSuccess(res.access_token)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'A apărut o eroare. Încearcă din nou.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        {/* Logo */}
        <div className="auth-logo" onClick={onClose} title="Înapoi acasă">
          <span className="auth-logo-mark"><LeafIcon /></span>
          <span className="auth-logo-name">NutriVision</span>
        </div>

        {/* Heading */}
        <div className="auth-heading">
          <h1>{mode === 'login' ? 'Bine ai revenit' : 'Creează cont'}</h1>
          <p>{mode === 'login' ? 'Conectează-te pentru a-ți accesa rețetele personalizate.' : 'Începe gratuit și descoperă rețete adaptate ție.'}</p>
        </div>

        {/* Toggle */}
        <div className="auth-toggle">
          <button
            className={mode === 'login' ? 'active' : ''}
            onClick={() => switchMode('login')}
          >
            Autentificare
          </button>
          <button
            className={mode === 'register' ? 'active' : ''}
            onClick={() => switchMode('register')}
          >
            Cont nou
          </button>
        </div>

        {/* Form */}
        <form className="auth-form" onSubmit={handleSubmit}>
          {mode === 'register' && (
            <div className="field">
              <label htmlFor="name">Nume complet</label>
              <input
                id="name"
                type="text"
                placeholder="Ana Ionescu"
                autoComplete="name"
                value={name}
                onChange={e => setName(e.target.value)}
                required
              />
            </div>
          )}

          <div className="field">
            <label htmlFor="email">Adresă de email</label>
            <input
              id="email"
              type="email"
              placeholder="ana@email.com"
              autoComplete="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="field">
            <label htmlFor="password">Parolă</label>
            <div className="input-wrap">
              <input
                id="password"
                type={showPass ? 'text' : 'password'}
                placeholder="Minim 8 caractere"
                autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
              />
              <button type="button" className="eye-btn" onClick={() => setShowPass(v => !v)}>
                <EyeIcon visible={showPass} />
              </button>
            </div>
          </div>

          {mode === 'register' && (
            <div className="field">
              <label htmlFor="confirm">Confirmă parola</label>
              <div className="input-wrap">
                <input
                  id="confirm"
                  type={showConfirm ? 'text' : 'password'}
                  placeholder="Repetă parola"
                  autoComplete="new-password"
                  value={confirm}
                  onChange={e => setConfirm(e.target.value)}
                  required
                />
                <button type="button" className="eye-btn" onClick={() => setShowConfirm(v => !v)}>
                  <EyeIcon visible={showConfirm} />
                </button>
              </div>
            </div>
          )}

          {mode === 'login' && (
            <div className="forgot-wrap">
              <a href="#" className="forgot-link">Ai uitat parola?</a>
            </div>
          )}

          {error && <p className="auth-error">{error}</p>}

          <button type="submit" className="auth-submit" disabled={loading}>
            {loading ? 'Se încarcă…' : mode === 'login' ? 'Conectează-te' : 'Creează cont'} {!loading && '→'}
          </button>
        </form>

        <p className="auth-switch">
          {mode === 'login' ? 'Nu ai cont?' : 'Ai deja cont?'}{' '}
          <button onClick={() => switchMode(mode === 'login' ? 'register' : 'login')}>
            {mode === 'login' ? 'Înregistrează-te' : 'Autentifică-te'}
          </button>
        </p>
      </div>
    </div>
  )
}
