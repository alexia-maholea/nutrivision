import { useEffect, useState } from 'react'
import { recipeService } from './services/recipeService'
import type { RecipeSummary, RecipeDetail } from './services/recipeService'
import { AdminRecipeForm, DeleteConfirmDialog } from './AdminRecipeModals'
import './Recipes.css'

type Tab = 'all' | 'my'

const DIFF_LABEL: Record<string, string> = { EASY: 'Ușor', MEDIUM: 'Mediu', HARD: 'Dificil' }
const DIFF_CLASS: Record<string, string> = { EASY: 'diff-easy', MEDIUM: 'diff-medium', HARD: 'diff-hard' }
const TAG_LABEL: Record<string, string> = {
  VEGAN: 'Vegan',
  VEGETARIAN: 'Vegetarian',
  GLUTEN_FREE: 'Fără gluten',
  KETO: 'Keto',
  LACTOSE_FREE: 'Fără lactoză',
}

function fmt(v: number | null): string {
  if (v === null) return '—'
  return Number.isInteger(v) ? String(v) : v.toFixed(1)
}

/* ── Icons ── */
function SearchIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <circle cx="11" cy="11" r="8" stroke="#9ca3af" strokeWidth="2"/>
      <path d="M21 21l-4.35-4.35" stroke="#9ca3af" strokeWidth="2" strokeLinecap="round"/>
    </svg>
  )
}

function ClockIcon() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
      <circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="1.8"/>
      <path d="M12 7v5l3 3" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"/>
    </svg>
  )
}

function FlameIcon() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
      <path d="M12 2c0 0-4 4-4 8 0 2.21 1.79 4 4 4s4-1.79 4-4c0-1-.4-1.9-1-2.6C14.5 8.6 14 10 12 10c0-2 2-4 2-6-1 .5-2 1-2 2z" fill="currentColor"/>
      <path d="M12 22c-3.31 0-6-2.69-6-6 0-4 3-7 4-9 .5 2 2 3.5 2 5 1.1 0 2-.9 2-2 1 1.5 2 3 2 5 0 3.87-1.79 7-4 7z" fill="currentColor"/>
    </svg>
  )
}

function ArrowIcon() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
      <path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  )
}

/* ── Skeleton card ── */
function SkeletonCard() {
  return <div className="recipe-card-skeleton" />
}

/* ── Recipe card ── */
function RecipeCard({ recipe, onClick, onDelete, isAdmin }: {
  recipe: RecipeSummary
  onClick: () => void
  onDelete?: (id: number) => void
  isAdmin?: boolean
}) {
  return (
    <article className="recipe-card" onClick={onClick} role="button" tabIndex={0} onKeyDown={e => e.key === 'Enter' && onClick()}>
      <div className="recipe-card-top">
        {recipe.difficulty && (
          <span className={`recipe-diff ${DIFF_CLASS[recipe.difficulty]}`}>{DIFF_LABEL[recipe.difficulty]}</span>
        )}
        {isAdmin && (
          <div className="recipe-card-admin">
            <button
              className="admin-btn admin-edit"
              onClick={e => {
                e.stopPropagation()
                onClick()
              }}
              title="Editează"
            >
              ✏️
            </button>
            <button
              className="admin-btn admin-delete"
              onClick={e => {
                e.stopPropagation()
                onDelete?.(recipe.id)
              }}
              title="Șterge"
            >
              🗑️
            </button>
          </div>
        )}
      </div>
      <h3 className="recipe-title">{recipe.title}</h3>
      <div className="recipe-meta">
        {recipe.cookingTime != null && (
          <span className="recipe-meta-item">
            <ClockIcon /> {recipe.cookingTime} min
          </span>
        )}
        {recipe.calories != null && (
          <span className="recipe-meta-item">
            <FlameIcon /> {recipe.calories} kcal
          </span>
        )}
      </div>
      <div className="recipe-card-footer">
        <span className="recipe-link">
          Vezi rețeta <ArrowIcon />
        </span>
      </div>
    </article>
  )
}

/* ── Macro pill ── */
function MacroCard({ label, value, unit }: { label: string; value: number | null; unit: string }) {
  return (
    <div className="macro-card">
      <strong>{fmt(value)}<span className="macro-unit"> {unit}</span></strong>
      <span>{label}</span>
    </div>
  )
}

/* ── Detail modal content ── */
function RecipeDetailView({ detail, isAdmin, onEdit, onDelete }: { detail: RecipeDetail; isAdmin?: boolean; onEdit?: (recipe: RecipeDetail) => void; onDelete?: (id: number) => void }) {
  return (
    <div className="modal-content">
      <div className="modal-header">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
          <div className="modal-badges">
            {detail.difficulty && (
              <span className={`recipe-diff ${DIFF_CLASS[detail.difficulty]}`}>{DIFF_LABEL[detail.difficulty]}</span>
            )}
            {detail.dietaryTags.map(t => (
              <span key={t.id} className="modal-tag">{TAG_LABEL[t.name] ?? t.name}</span>
            ))}
          </div>
          {isAdmin && (
            <div className="detail-admin-btns">
              <button className="detail-btn edit" onClick={() => onEdit?.(detail)} title="Editează">✏️</button>
              <button className="detail-btn delete" onClick={() => onDelete?.(detail.id)} title="Șterge">🗑️</button>
            </div>
          )}
        </div>
        <h2>{detail.title}</h2>
        {detail.description && <p className="modal-desc">{detail.description}</p>}
      </div>

      <div className="modal-macros">
        <MacroCard label="Calorii" value={detail.calories} unit="kcal" />
        <MacroCard label="Proteine" value={detail.protein} unit="g" />
        <MacroCard label="Glucide" value={detail.carbs} unit="g" />
        <MacroCard label="Grăsimi" value={detail.fat} unit="g" />
      </div>

      {detail.cookingTime != null && (
        <div className="modal-timing">
          <ClockIcon /> <span>{detail.cookingTime} minute de preparare</span>
        </div>
      )}

      {detail.ingredients.length > 0 && (
        <div className="modal-section">
          <h3>Ingrediente</h3>
          <ul className="ingredient-list">
            {detail.ingredients.map(ing => (
              <li key={ing.ingredientId}>
                <span className="ing-name">{ing.name}</span>
                {ing.quantity != null && ing.unit && (
                  <span className="ing-qty">{fmt(ing.quantity)} {ing.unit}</span>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}

      {detail.steps.length > 0 && (
        <div className="modal-section">
          <h3>Mod de preparare</h3>
          <ol className="steps-list">
            {detail.steps.map(step => (
              <li key={step.stepNo}>
                <span className="step-desc">{step.description}</span>
                {step.durationMinutes != null && (
                  <span className="step-dur">{step.durationMinutes} min</span>
                )}
              </li>
            ))}
          </ol>
        </div>
      )}
    </div>
  )
}

/* ── Main component ── */
export default function Recipes({
  token,
  onRequireAuth,
}: {
  token: string | null
  onRequireAuth: () => void
}) {
  const [tab, setTab] = useState<Tab>('all')
  const [view, setView] = useState<'grid' | 'table'>('grid')
  const [pageSize, setPageSize] = useState(9)
  const [page, setPage] = useState(0)
  const [searchInput, setSearchInput] = useState('')
  const [query, setQuery] = useState('')
  const [recipes, setRecipes] = useState<RecipeSummary[]>([])
  const [meta, setMeta] = useState({ totalPages: 1, hasNext: false, hasPrevious: false, totalElements: 0 })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [detail, setDetail] = useState<RecipeDetail | null>(null)
  const [detailLoading, setDetailLoading] = useState(false)

  // Admin features
  const [isAdmin, setIsAdmin] = useState(false)
  const [showAddModal, setShowAddModal] = useState(false)
  const [editingId, setEditingId] = useState<number | null>(null)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState<number | null>(null)
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    calories: '',
    protein: '',
    carbs: '',
    fat: '',
    cooking_time: '',
    difficulty: 'EASY',
  })
  const [formLoading, setFormLoading] = useState(false)

  /* Debounce search input */
  useEffect(() => {
    const id = setTimeout(() => {
      setQuery(searchInput)
      setPage(0)
    }, 400)
    return () => clearTimeout(id)
  }, [searchInput])

  /* Check if admin */
  useEffect(() => {
    if (!token) return
    // Try to get from localStorage (cached from profile fetch)
    const cached = localStorage.getItem('userRole')
    if (cached) {
      setIsAdmin(cached === 'ADMIN')
    }
  }, [token])

  /* Fetch recipe list */
  useEffect(() => {
    if (!token) return
    setLoading(true)
    setError('')
    const fetcher = tab === 'all'
      ? recipeService.all(page, pageSize, query || undefined)
      : recipeService.my(page, pageSize, query || undefined)
    fetcher
      .then(data => {
        setRecipes(data.items)
        setMeta({
          totalPages: data.totalPages,
          hasNext: data.hasNext,
          hasPrevious: data.hasPrevious,
          totalElements: data.totalElements,
        })
      })
      .catch(err => {
        const msg = err instanceof Error ? err.message : String(err)
        console.error('[Recipes API Error]', msg)
        if (msg.includes('401') || msg.includes('403')) {
          onRequireAuth()
        } else {
          setError(`Eroare: ${msg}`)
        }
      })
      .finally(() => setLoading(false))
  }, [tab, page, query, token, pageSize, onRequireAuth])

  /* Fetch recipe detail */
  useEffect(() => {
    if (selectedId === null) { setDetail(null); return }
    setDetailLoading(true)
    recipeService.getById(selectedId)
      .then(setDetail)
      .catch(() => setSelectedId(null))
      .finally(() => setDetailLoading(false))
  }, [selectedId])

  /* Lock body scroll when modal is open */
  useEffect(() => {
    document.body.style.overflow = selectedId !== null ? 'hidden' : ''
    return () => { document.body.style.overflow = '' }
  }, [selectedId])

  /* Close modal on Escape */
  useEffect(() => {
    if (selectedId === null) return
    const onKey = (e: KeyboardEvent) => { if (e.key === 'Escape') setSelectedId(null) }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [selectedId])

  function switchTab(t: Tab) {
    setTab(t)
    setPage(0)
    setSearchInput('')
  }

  function openAddModal() {
    setFormData({ title: '', description: '', calories: '', protein: '', carbs: '', fat: '', cooking_time: '', difficulty: 'EASY' })
    setEditingId(null)
    setShowAddModal(true)
  }

  function openEditModal(recipe: RecipeDetail) {
    setFormData({
      title: recipe.title,
      description: recipe.description || '',
      calories: recipe.calories ? String(recipe.calories) : '',
      protein: recipe.protein ? String(recipe.protein) : '',
      carbs: recipe.carbs ? String(recipe.carbs) : '',
      fat: recipe.fat ? String(recipe.fat) : '',
      cooking_time: recipe.cookingTime ? String(recipe.cookingTime) : '',
      difficulty: recipe.difficulty || 'EASY',
    })
    setEditingId(recipe.id)
    setShowAddModal(true)
  }

  async function handleSaveRecipe() {
    if (!formData.title.trim()) {
      alert('Titlul rețetei este obligatoriu')
      return
    }
    setFormLoading(true)
    try {
      const payload = {
        title: formData.title,
        description: formData.description || undefined,
        calories: formData.calories ? Number(formData.calories) : undefined,
        protein: formData.protein ? Number(formData.protein) : undefined,
        carbs: formData.carbs ? Number(formData.carbs) : undefined,
        fat: formData.fat ? Number(formData.fat) : undefined,
        cooking_time: formData.cooking_time ? Number(formData.cooking_time) : undefined,
        difficulty: formData.difficulty || undefined,
      }
      console.log('[Recipes] Saving recipe:', { editingId, payload })
      console.log('[Recipes] formData:', formData)
      console.log('[Recipes] JSON payload:', JSON.stringify(payload, null, 2))

      if (editingId) {
        console.log('[Recipes] Calling update for id:', editingId)
        await recipeService.update(editingId, payload)
        console.log('[Recipes] Update completed')
      } else {
        console.log('[Recipes] Calling create')
        await recipeService.create(payload)
        console.log('[Recipes] Create completed')
      }

      console.log('[Recipes] Recipe saved successfully')
      setShowAddModal(false)
      setEditingId(null)
      setPage(0)
      setQuery('')
      setSearchInput('')
    } catch (err) {
      const msg = err instanceof Error ? err.message : 'Nu s-a putut salva rețeta'
      console.error('[Recipes] Save error:', msg, err)
      alert(`Eroare: ${msg}`)
    } finally {
      setFormLoading(false)
    }
  }

  async function handleDeleteRecipe(id: number) {
    setFormLoading(true)
    try {
      await recipeService.delete(id)
      setShowDeleteConfirm(null)
      setSelectedId(null)
      setPage(0)
    } catch (err) {
      alert(`Eroare: ${err instanceof Error ? err.message : 'Nu s-a putut șterge rețeta'}`)
    } finally {
      setFormLoading(false)
    }
  }

  /* Not logged in */
  if (!token) {
    return (
      <div className="recipes-page">
        <div className="recipes-auth-prompt">
          <div className="auth-prompt-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
              <path d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z" fill="#16a34a"/>
              <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="#16a34a" strokeWidth="1.5" strokeLinecap="round"/>
            </svg>
          </div>
          <h2>Conectează-te pentru a vedea rețetele</h2>
          <p>Ai nevoie de un cont pentru a accesa catalogul de rețete personalizate NutriVision.</p>
          <button className="recipes-auth-btn" onClick={onRequireAuth}>Conectează-te →</button>
        </div>
      </div>
    )
  }

  return (
    <div className="recipes-page">
      <div className="container">

        {/* Page header */}
        <div className="recipes-header">
          <h1>Rețete</h1>
          <p>Descoperă preparate delicioase adaptate stilului tău de viață.</p>
        </div>

        {/* Toolbar: tabs + search */}
        <div className="recipes-toolbar">
          <div className="recipes-tabs">
            <button className={tab === 'all' ? 'active' : ''} onClick={() => switchTab('all')}>
              Toate rețetele
            </button>
            <button className={tab === 'my' ? 'active' : ''} onClick={() => switchTab('my')}>
              Rețetele mele
            </button>
          </div>

          <div className="recipes-search">
            <SearchIcon />
            <input
              type="text"
              placeholder="Caută rețete..."
              value={searchInput}
              onChange={e => setSearchInput(e.target.value)}
              aria-label="Caută rețete"
            />
            {searchInput && (
              <button className="search-clear" onClick={() => setSearchInput('')} aria-label="Șterge căutarea">×</button>
            )}
          </div>

          <div className="recipes-view-toggle">
            <button
              className={view === 'grid' ? 'active' : ''}
              onClick={() => setView('grid')}
              title="Vizualizare grilă"
              aria-label="Grilă"
            >
              ⊞
            </button>
            <button
              className={view === 'table' ? 'active' : ''}
              onClick={() => setView('table')}
              title="Vizualizare tabel"
              aria-label="Tabel"
            >
              ≡
            </button>
          </div>

          <div className="recipes-page-size">
            <label htmlFor="page-size">Pe pagină:</label>
            <input
              id="page-size"
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

          {isAdmin && (
            <button className="btn-add-recipe" onClick={() => openAddModal()}>
              + Adauga rețetă
            </button>
          )}
        </div>

        {/* Result count */}
        {!loading && !error && (
          <p className="recipes-count">
            {meta.totalElements === 0
              ? 'Nicio rețetă găsită'
              : `${meta.totalElements} ${meta.totalElements === 1 ? 'rețetă' : 'rețete'}`}
            {query && <> pentru <em>„{query}"</em></>}
          </p>
        )}

        {/* Grid or Table */}
        {loading ? (
          <div className="recipes-grid">
            {Array.from({ length: pageSize }).map((_, i) => <SkeletonCard key={i} />)}
          </div>
        ) : error ? (
          <div className="recipes-error-wrap">
            <p>{error}</p>
            <button onClick={() => setError('')} className="recipes-retry">Reîncearcă</button>
          </div>
        ) : recipes.length === 0 ? (
          <div className="recipes-empty">
            <p>
              {tab === 'my'
                ? 'Nu ai rețete compatibile cu restricțiile tale. Actualizează-ți profilul pentru a vedea sugestii.'
                : 'Nicio rețetă nu corespunde căutării tale.'}
            </p>
          </div>
        ) : view === 'grid' ? (
          <div className="recipes-grid">
            {recipes.map(r => (
              <RecipeCard
                key={r.id}
                recipe={r}
                onClick={() => setSelectedId(r.id)}
                isAdmin={isAdmin}
                onDelete={id => setShowDeleteConfirm(id)}
              />
            ))}
          </div>
        ) : (
          <div className="recipes-table-wrapper">
            <table className="recipes-table">
              <thead>
                <tr>
                  <th>Rețetă</th>
                  <th>Timp</th>
                  <th>Calorii</th>
                  <th>Dificultate</th>
                  {isAdmin && <th>Acțiuni</th>}
                </tr>
              </thead>
              <tbody>
                {recipes.map(r => (
                  <tr key={r.id} className="table-row">
                    <td className="col-title" onClick={() => setSelectedId(r.id)} style={{ cursor: 'pointer' }}>
                      {r.title}
                    </td>
                    <td className="col-time">{r.cookingTime} min</td>
                    <td className="col-calories">{r.calories} kcal</td>
                    <td className="col-difficulty">
                      {r.difficulty && (
                        <span className={`recipe-diff ${DIFF_CLASS[r.difficulty]}`}>
                          {DIFF_LABEL[r.difficulty]}
                        </span>
                      )}
                    </td>
                    {isAdmin && (
                      <td className="col-actions">
                        <button className="table-action edit" onClick={() => setSelectedId(r.id)} title="Editează">
                          ✏️
                        </button>
                        <button className="table-action delete" onClick={() => setShowDeleteConfirm(r.id)} title="Șterge">
                          🗑️
                        </button>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination */}
        {meta.totalPages > 1 && !loading && !error && (
          <div className="recipes-pagination">
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

      {/* Detail modal */}
      {selectedId !== null && (
        <div
          className="recipe-modal-overlay"
          onClick={e => { if (e.target === e.currentTarget) setSelectedId(null) }}
          role="dialog"
          aria-modal="true"
        >
          <div className="recipe-modal">
            <button className="modal-close" onClick={() => setSelectedId(null)} aria-label="Închide">×</button>
            {detailLoading || !detail ? (
              <div className="modal-loading">
                <div className="modal-loading-spinner" />
                <span>Se încarcă rețeta…</span>
              </div>
            ) : (
              <RecipeDetailView
                detail={detail}
                isAdmin={isAdmin}
                onEdit={recipe => {
                  openEditModal(recipe)
                  setSelectedId(null)
                }}
                onDelete={id => {
                  setShowDeleteConfirm(id)
                  setSelectedId(null)
                }}
              />
            )}
          </div>
        </div>
      )}

      {/* Admin Modals */}
      <AdminRecipeForm
        isOpen={showAddModal}
        isLoading={formLoading}
        formData={formData}
        setFormData={setFormData}
        onSave={handleSaveRecipe}
        onClose={() => setShowAddModal(false)}
      />

      <DeleteConfirmDialog
        isOpen={showDeleteConfirm !== null}
        isLoading={formLoading}
        onConfirm={() => showDeleteConfirm !== null && handleDeleteRecipe(showDeleteConfirm)}
        onCancel={() => setShowDeleteConfirm(null)}
      />
    </div>
  )
}
