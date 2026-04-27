import { useEffect, useState } from 'react'
import {
  profileService,
  GOAL_TO_BACKEND,
  BACKEND_TO_GOAL,
  RESTRICTION_TO_TAG_ID,
  TAG_NAME_TO_RESTRICTION,
} from './services/profileService'
import './Profile.css'

const goals = [
  {
    id: 'slabire',
    title: 'Slăbire',
    desc: 'Deficit caloric sănătos',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <polyline points="23 6 13.5 15.5 8.5 10.5 1 18" stroke="#16a34a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        <polyline points="17 6 23 6 23 12" stroke="#16a34a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    id: 'masa',
    title: 'Masă musculară',
    desc: 'Proteine ridicate',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M6.5 6.5h1a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-1a1 1 0 0 1-1-1v-3a1 1 0 0 1 1-1z" stroke="#16a34a" strokeWidth="1.8"/>
        <path d="M16.5 6.5h1a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-1a1 1 0 0 1-1-1v-3a1 1 0 0 1 1-1z" stroke="#16a34a" strokeWidth="1.8"/>
        <path d="M7.5 9h9" stroke="#16a34a" strokeWidth="2" strokeLinecap="round"/>
        <path d="M5 8.5H3.5a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H5" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <path d="M19 8.5h1.5a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H19" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'mentinere',
    title: 'Menținere',
    desc: 'Echilibru caloric',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M12 3v1m0 16v1M4.22 4.22l.7.7m13.16 13.16.7.7M3 12h1m16 0h1M4.22 19.78l.7-.7M18.36 5.64l.7-.7" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <circle cx="12" cy="12" r="4" stroke="#16a34a" strokeWidth="1.8"/>
      </svg>
    ),
  },
  {
    id: 'energie',
    title: 'Energie',
    desc: 'Boost de energie',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    id: 'sanatate',
    title: 'Sănătate generală',
    desc: 'Nutrienți echilibrați',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    id: 'detox',
    title: 'Detoxifiere',
    desc: 'Curățarea organismului',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
        <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="#16a34a" strokeWidth="1.5" strokeLinecap="round"/>
      </svg>
    ),
  },
]

const restrictions = [
  {
    id: 'vegetarian',
    title: 'Vegetarian',
    desc: 'Fără carne',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
        <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="#16a34a" strokeWidth="1.5" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'vegan',
    title: 'Vegan',
    desc: 'Exclusiv produse vegetale',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M12 2a10 10 0 0 1 10 10c0 5.52-4.48 10-10 10S2 17.52 2 12 6.48 2 12 2z" stroke="#16a34a" strokeWidth="1.8"/>
        <path d="M12 6v6l4 2" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'gluten',
    title: 'Fără gluten',
    desc: 'Potrivit pentru celiaci',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <circle cx="12" cy="12" r="9" stroke="#16a34a" strokeWidth="1.8"/>
        <line x1="4.93" y1="4.93" x2="19.07" y2="19.07" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'lactoza',
    title: 'Fără lactoză',
    desc: 'Fără produse lactate',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M12 2C6 2 4 7 4 12s2 10 8 10 8-5 8-10S18 2 12 2z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <path d="M9 12h6" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'keto',
    title: 'Keto',
    desc: 'Grăsimi sănătoase, carbohidrați reduși',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    id: 'lowcarb',
    title: 'Low carb',
    desc: 'Carbohidrați limitați',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    id: 'nuci',
    title: 'Fără nuci',
    desc: 'Fără alergeni din nuci',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
        <line x1="12" y1="9" x2="12" y2="13" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <line x1="12" y1="17" x2="12.01" y2="17" stroke="#16a34a" strokeWidth="2" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    id: 'zahar',
    title: 'Fără zahăr',
    desc: 'Fără zahăr adăugat',
    icon: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
        <path d="M18 8h1a4 4 0 0 1 0 8h-1" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <line x1="6" y1="1" x2="6" y2="4" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <line x1="10" y1="1" x2="10" y2="4" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
        <line x1="14" y1="1" x2="14" y2="4" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
      </svg>
    ),
  },
]

function CheckIcon() {
  return (
    <svg width="9" height="9" viewBox="0 0 12 12" fill="none">
      <path d="M2 6l3 3 5-5" stroke="white" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  )
}

function SelectableCard({
  id, title, desc, icon, selected, onToggle,
}: {
  id: string; title: string; desc: string; icon: React.ReactNode; selected: boolean; onToggle: (id: string) => void
}) {
  return (
    <button
      className={`goal-card${selected ? ' selected' : ''}`}
      onClick={() => onToggle(id)}
    >
      {selected && <div className="check-badge"><CheckIcon /></div>}
      <div className="goal-icon-wrap">{icon}</div>
      <strong>{title}</strong>
      <span className="goal-desc">{desc}</span>
    </button>
  )
}

export default function Profile({ onRequireAuth }: { onRequireAuth: () => void }) {
  const [selectedGoal, setSelectedGoal] = useState<string | null>(null)
  const [selectedRestrictions, setSelectedRestrictions] = useState<string[]>([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)

  useEffect(() => {
    profileService.get()
      .then(data => {
        if (data.goal) setSelectedGoal(BACKEND_TO_GOAL[data.goal] ?? null)
        setSelectedRestrictions(
          data.dietaryRestrictions
            .map(tag => TAG_NAME_TO_RESTRICTION[tag.name])
            .filter(Boolean)
        )
      })
      .catch(err => {
        if (err instanceof Error && err.message.includes('403')) {
          onRequireAuth()
        } else {
          setError('Nu s-a putut încărca profilul.')
        }
      })
      .finally(() => setLoading(false))
  }, [onRequireAuth])

  function toggleGoal(id: string) {
    setSelectedGoal(prev => (prev === id ? null : id))
    setSuccess(false)
  }

  function toggleRestriction(id: string) {
    setSelectedRestrictions(prev =>
      prev.includes(id) ? prev.filter(r => r !== id) : [...prev, id]
    )
    setSuccess(false)
  }

  async function handleSave() {
    setSaving(true)
    setError('')
    setSuccess(false)
    try {
      const tagIds = selectedRestrictions
        .map(r => RESTRICTION_TO_TAG_ID[r])
        .filter((id): id is number => id !== undefined)

      await profileService.update({
        goal: selectedGoal ? GOAL_TO_BACKEND[selectedGoal] : undefined,
        dietaryRestrictionTagIds: tagIds,
      })
      setSuccess(true)
    } catch (err) {
      if (err instanceof Error && err.message.includes('403')) {
        onRequireAuth()
      } else {
        setError('Nu s-a putut salva profilul. Încearcă din nou.')
      }
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <div className="profile-page">
        <div className="profile-header">
          <p style={{ color: '#71717a' }}>Se încarcă profilul…</p>
        </div>
      </div>
    )
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <h1>Configurează-ți profilul</h1>
        <p>Selectează obiectivul și restricțiile tale pentru rețete personalizate</p>
      </div>

      <div className="profile-body">
        <section>
          <h2 className="profile-section-title">Obiectivul tău</h2>
          <div className="goals-grid">
            {goals.map(g => (
              <SelectableCard key={g.id} {...g} selected={selectedGoal === g.id} onToggle={toggleGoal} />
            ))}
          </div>
        </section>

        <section>
          <h2 className="profile-section-title">Restricții alimentare</h2>
          <div className="restrictions-grid">
            {restrictions.map(r => (
              <SelectableCard key={r.id} {...r} selected={selectedRestrictions.includes(r.id)} onToggle={toggleRestriction} />
            ))}
          </div>
        </section>

        {error && <p className="profile-error">{error}</p>}
        {success && <p className="profile-success">Profilul a fost salvat cu succes!</p>}

        <div className="profile-cta">
          <button className="profile-btn" onClick={handleSave} disabled={saving}>
            {saving ? 'Se salvează…' : 'Salvează profilul →'}
          </button>
        </div>
      </div>
    </div>
  )
}
