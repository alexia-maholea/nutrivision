import { useEffect, useState } from 'react'
import { adminUserService } from './services/adminUserService'
import type { UserProfile } from './services/adminUserService'
import './Users.css'

function SearchIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <circle cx="11" cy="11" r="8" stroke="#9ca3af" strokeWidth="2"/>
      <path d="M21 21l-4.35-4.35" stroke="#9ca3af" strokeWidth="2" strokeLinecap="round"/>
    </svg>
  )
}

const GOAL_LABEL: Record<string, string> = {
  WEIGHT_LOSS: 'Pierdere de greutate',
  MUSCLE_GAIN: 'Câștig muscular',
  MAINTENANCE: 'Menținere',
}

export default function Users({
  token,
  onRequireAuth,
}: {
  token: string | null
  onRequireAuth: () => void
}) {
  const [pageSize, setPageSize] = useState(10)
  const [page, setPage] = useState(0)
  const [searchInput, setSearchInput] = useState('')
  const [query, setQuery] = useState('')
  const [users, setUsers] = useState<UserProfile[]>([])
  const [meta, setMeta] = useState({ totalPages: 1, hasNext: false, hasPrevious: false, totalElements: 0 })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  /* Debounce search */
  useEffect(() => {
    const id = setTimeout(() => {
      setQuery(searchInput)
      setPage(0)
    }, 400)
    return () => clearTimeout(id)
  }, [searchInput])

  /* Fetch users */
  useEffect(() => {
    if (!token) return
    setLoading(true)
    setError('')
    adminUserService.all(page, pageSize, query || undefined)
      .then(data => {
        setUsers(data.items)
        setMeta({
          totalPages: data.totalPages,
          hasNext: data.hasNext,
          hasPrevious: data.hasPrevious,
          totalElements: data.totalElements,
        })
      })
      .catch(err => {
        const msg = err instanceof Error ? err.message : String(err)
        console.error('[Users API Error]', msg)
        if (msg.includes('401') || msg.includes('403')) {
          onRequireAuth()
        } else {
          setError(`Eroare: ${msg}`)
        }
      })
      .finally(() => setLoading(false))
  }, [page, query, token, pageSize, onRequireAuth])

  if (!token) {
    return (
      <div className="users-page">
        <div className="users-auth-prompt">
          <h2>Conectează-te pentru a vedea utilizatorii</h2>
          <button className="users-auth-btn" onClick={onRequireAuth}>Conectează-te →</button>
        </div>
      </div>
    )
  }

  return (
    <div className="users-page">
      <div className="container">
        {/* Page header */}
        <div className="users-header">
          <h1>Utilizatori</h1>
          <p>Gestionează profilurile utilizatorilor</p>
        </div>

        {/* Toolbar */}
        <div className="users-toolbar">
          <div className="users-search">
            <SearchIcon />
            <input
              type="text"
              placeholder="Caută utilizatori..."
              value={searchInput}
              onChange={e => setSearchInput(e.target.value)}
              aria-label="Caută utilizatori"
            />
            {searchInput && (
              <button className="search-clear" onClick={() => setSearchInput('')} aria-label="Șterge căutarea">×</button>
            )}
          </div>

          <div className="users-page-size">
            <label htmlFor="user-page-size">Pe pagină:</label>
            <input
              id="user-page-size"
              type="number"
              min="1"
              max="100"
              value={pageSize}
              onChange={e => {
                const val = Math.max(1, Math.min(100, Number(e.target.value)))
                setPageSize(val)
                setPage(0)
              }}
            />
          </div>
        </div>

        {/* Result count */}
        {!loading && !error && (
          <p className="users-count">
            {meta.totalElements === 0
              ? 'Nicio utilizator găsit'
              : `${meta.totalElements} ${meta.totalElements === 1 ? 'utilizator' : 'utilizatori'}`}
            {query && <> pentru <em>„{query}"</em></>}
          </p>
        )}

        {/* Table */}
        {loading ? (
          <div className="users-table-wrapper">
            <p style={{ textAlign: 'center', padding: '2rem', color: '#71717a' }}>Se încarcă...</p>
          </div>
        ) : error ? (
          <div className="users-error-wrap">
            <p>{error}</p>
            <button onClick={() => setError('')} className="users-retry">Reîncearcă</button>
          </div>
        ) : users.length === 0 ? (
          <div className="users-empty">
            <p>Nicio utilizator nu corespunde căutării tale.</p>
          </div>
        ) : (
          <div className="users-table-wrapper">
            <table className="users-table">
              <thead>
                <tr>
                  <th>Nume</th>
                  <th>Email</th>
                  <th>Rol</th>
                  <th>Vârstă</th>
                  <th>Greutate</th>
                  <th>Obiectiv</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => (
                  <tr key={user.email}>
                    <td className="col-name">{user.name}</td>
                    <td className="col-email">{user.email}</td>
                    <td className="col-role">
                      <span className={`role-badge role-${user.role.toLowerCase()}`}>
                        {user.role}
                      </span>
                    </td>
                    <td className="col-age">{user.age ?? '—'}</td>
                    <td className="col-weight">{user.weight ? `${user.weight} kg` : '—'}</td>
                    <td className="col-goal">{user.goal ? GOAL_LABEL[user.goal] ?? user.goal : '—'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination */}
        {meta.totalPages > 1 && !loading && !error && (
          <div className="users-pagination">
            <button
              className="page-btn"
              disabled={!meta.hasPrevious}
              onClick={() => { setPage(p => p - 1); window.scrollTo({ top: 0, behavior: 'smooth' }) }}
            >
              ← Anterioară
            </button>
            <div className="page-indicator">
              {Array.from({ length: meta.totalPages }, (_, i) => (
                <button
                  key={i}
                  className={`page-dot ${i === page ? 'active' : ''}`}
                  onClick={() => { setPage(i); window.scrollTo({ top: 0, behavior: 'smooth' }) }}
                  aria-label={`Pagina ${i + 1}`}
                >
                  {i + 1}
                </button>
              ))}
            </div>
            <button
              className="page-btn"
              disabled={!meta.hasNext}
              onClick={() => { setPage(p => p + 1); window.scrollTo({ top: 0, behavior: 'smooth' }) }}
            >
              Următoare →
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
