import { useState } from 'react'
import heroImg from './assets/hero.png'
import Profile from './Profile'
import Auth from './Auth'
import Recipes from './Recipes'
import './App.css'

type Page = 'home' | 'profile' | 'auth' | 'recipes'

const stats = [
  { value: '1.000+', label: 'Rețete Generate' },
  { value: '15 min', label: 'Timp Mediu de Gătit' },
  { value: '100%', label: 'Personalizate AI' },
  { value: '24/7', label: 'Disponibil Oricând' },
]

const features = [
  {
    title: 'Recomandări personalizate',
    description: 'Primești mese și rețete în funcție de stilul tău de viață, țintele zilnice și preferințele alimentare.',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
        <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" stroke="#16a34a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
      </svg>
    ),
  },
  {
    title: 'Planificare fără stres',
    description: 'NutriVision transformă mesele de zi cu zi într-un plan clar, ușor de urmat și rapid de pregătit.',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
        <rect x="3" y="4" width="18" height="18" rx="2" stroke="#16a34a" strokeWidth="2"/>
        <path d="M16 2v4M8 2v4M3 10h18" stroke="#16a34a" strokeWidth="2" strokeLinecap="round"/>
      </svg>
    ),
  },
  {
    title: 'Atenție la detalii',
    description: 'Calorii, porții și restricții alimentare sunt luate în calcul pentru rezultate mai bune, fără ghicit.',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
        <path d="M9.5 2A9.5 9.5 0 1 0 9.5 21A9.5 9.5 0 1 0 9.5 2Z" stroke="#16a34a" strokeWidth="2"/>
        <path d="M21 21l-4.35-4.35" stroke="#16a34a" strokeWidth="2" strokeLinecap="round"/>
      </svg>
    ),
  },
]

function LeafIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
      <path
        d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z"
        fill="white"
      />
      <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="white" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  )
}

function PlantIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <path
        d="M17 8C8 10 5.9 16.17 3.82 19.82A1 1 0 0 0 4.9 21C8.12 20.5 12.5 19.5 16 17c3-2 4-5 4-9-1 0-2 0-3 0z"
        fill="#16a34a"
      />
      <path d="M12 12c-2 2-3.5 5-4.5 8" stroke="#16a34a" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  )
}

function FlameIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
      <path
        d="M12 2c0 0-4 4-4 8 0 2.21 1.79 4 4 4s4-1.79 4-4c0-1-.4-1.9-1-2.6C14.5 8.6 14 10 12 10c0-2 2-4 2-6-1 .5-2 1-2 2z"
        fill="white"
      />
      <path
        d="M12 22c-3.31 0-6-2.69-6-6 0-4 3-7 4-9 .5 2 2 3.5 2 5 1.1 0 2-.9 2-2 1 1.5 2 3 2 5 0 3.87-1.79 7-4 7z"
        fill="white"
      />
    </svg>
  )
}

function App() {
  const [page, setPage] = useState<Page>('home')
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('token'))

  function goTo(p: Page) {
    if ((p === 'profile' || p === 'recipes') && !token) {
      setPage('auth')
    } else {
      setPage(p)
    }
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const nav = (p: Page) => (e: React.MouseEvent) => {
    e.preventDefault()
    goTo(p)
  }

  function handleAuthSuccess(newToken: string) {
    setToken(newToken)
    setPage('profile')
  }

  function handleLogout() {
    localStorage.removeItem('token')
    setToken(null)
    setPage('home')
  }

  if (page === 'auth') {
    return (
      <Auth
        onClose={() => setPage('home')}
        onAuthSuccess={handleAuthSuccess}
      />
    )
  }

  return (
    <div className="page-shell">
      <header className="site-header">
        <div className="container header-inner">
          <a className="brand" href="#" onClick={nav('home')} aria-label="NutriVision acasă">
            <span className="brand-mark">
              <LeafIcon />
            </span>
            <span className="brand-name">NutriVision</span>
          </a>

          <nav className="site-nav" aria-label="Navigație principală">
            <a href="#" onClick={nav('home')} className={page === 'home' ? 'nav-active' : ''}>Acasă</a>
            <a href="#" onClick={nav('profile')} className={page === 'profile' ? 'nav-active' : ''}>Profil</a>
            <a href="#" onClick={nav('recipes')} className={page === 'recipes' ? 'nav-active' : ''}>Rețete</a>
            {token ? (
              <button className="nav-login nav-logout" onClick={handleLogout}>
                Deconectează-te
              </button>
            ) : (
              <a href="#" onClick={nav('auth')} className="nav-login">Conectează-te</a>
            )}
          </nav>
        </div>
      </header>

      {page === 'profile' ? (
        <Profile onRequireAuth={() => setPage('auth')} />
      ) : page === 'recipes' ? (
        <Recipes token={token} onRequireAuth={() => setPage('auth')} />
      ) : (
        <main>
          <section id="home" className="hero-section">
            <div className="container hero-grid">
              <div className="hero-copy">
                <div className="eyebrow">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                    <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" fill="#16a34a"/>
                  </svg>
                  <span>Nutriție personalizată cu AI</span>
                </div>

                <h1>
                  Mănâncă <span className="accent">inteligent</span>,
                  <br />
                  trăiește mai bine
                </h1>

                <p className="hero-text">
                  Descoperă rețete personalizate în funcție de obiectivele tale
                  nutriționale și restricțiile alimentare. Simplu, sănătos, delicios.
                </p>

                <div className="hero-actions">
                  <a className="primary-btn" href="#features">
                    Începe acum <span aria-hidden="true">→</span>
                  </a>
                  <a className="secondary-btn" href="#" onClick={nav('recipes')}>
                    Vezi rețete
                  </a>
                </div>
              </div>

              <div className="hero-visual">
                <div className="hero-frame">
                  <img src={heroImg} alt="Boluri sănătoase cu ingrediente proaspete" />

                  <div className="floating-card floating-card--top">
                    <div className="floating-icon floating-icon--green">
                      <PlantIcon />
                    </div>
                    <div>
                      <strong>Plant-based</strong>
                      <span>Opțiuni nelimitate</span>
                    </div>
                  </div>

                  <div className="floating-card floating-card--bottom">
                    <div className="floating-icon floating-icon--orange">
                      <FlameIcon />
                    </div>
                    <div>
                      <strong>420 kcal</strong>
                      <span>Per porție</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section id="stats" className="stats-section">
            <div className="container stats-grid">
              {stats.map((stat) => (
                <div className="stat-card" key={stat.label}>
                  <strong>{stat.value}</strong>
                  <span>{stat.label}</span>
                </div>
              ))}
            </div>
          </section>

          <section id="features" className="content-section">
            <div className="container">
              <div className="section-heading">
                <span className="label">Ce obții</span>
                <h2>Tot ce ai nevoie pentru mese mai bune, fără să pierzi timp</h2>
                <p>
                  NutriVision adună ideile, calculele și recomandările într-un singur loc, ca să găsești
                  rapid ce ți se potrivește.
                </p>
              </div>

              <div className="feature-grid">
                {features.map((feature) => (
                  <article className="feature-card" key={feature.title}>
                    <div className="feature-icon">{feature.icon}</div>
                    <h3>{feature.title}</h3>
                    <p>{feature.description}</p>
                  </article>
                ))}
              </div>
            </div>
          </section>

          {/* ── Inspirație pentru fiecare masă ── */}
          <section className="meals-section">
            <div className="container">
              <h2 className="meals-title">Inspirație pentru fiecare masă</h2>
              <p className="meals-sub">De la mic dejun până la cină, găsești rețete potrivite pentru orice moment al zilei.</p>
              <div className="meals-grid">
                <div className="meal-card meal-card--breakfast">
                  <div className="meal-overlay">
                    <span className="meal-label">ENERGIE PENTRU TOATĂ ZIUA</span>
                    <strong className="meal-name">Mic dejun</strong>
                  </div>
                </div>
                <div className="meal-card meal-card--lunch">
                  <div className="meal-overlay">
                    <span className="meal-label">ECHILIBRU PE FARFURIE</span>
                    <strong className="meal-name">Prânz</strong>
                  </div>
                </div>
                <div className="meal-card meal-card--dinner">
                  <div className="meal-overlay">
                    <span className="meal-label">UȘOR ȘI SĂȚIOS</span>
                    <strong className="meal-name">Cină</strong>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* ── Indiferent de stilul tău alimentar ── */}
          <section className="diets-section">
            <div className="container diets-grid">
              <div className="diets-img-wrap">
                <img src={heroImg} alt="Ingrediente proaspete" className="diets-img" />
              </div>
              <div className="diets-content">
                <h2>Indiferent de stilul tău alimentar</h2>
                <p>AI-ul nostru se adaptează la orice tip de dietă și obiectiv. Spune-i ce vrei și primești rețete pe măsură.</p>
                <div className="diet-cards">
                  <div className="diet-card">
                    <div className="diet-icon">
                      <PlantIcon />
                    </div>
                    <strong>Vegan &amp; Vegetarian</strong>
                    <span>Mese pe bază de plante, bogate în nutrienți.</span>
                  </div>
                  <div className="diet-card">
                    <div className="diet-icon">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                        <path d="M12 2c0 0-4 4-4 8 0 2.21 1.79 4 4 4s4-1.79 4-4c0-1-.4-1.9-1-2.6C14.5 8.6 14 10 12 10c0-2 2-4 2-6-1 .5-2 1-2 2z" fill="#16a34a"/>
                        <path d="M12 22c-3.31 0-6-2.69-6-6 0-4 3-7 4-9 .5 2 2 3.5 2 5 1.1 0 2-.9 2-2 1 1.5 2 3 2 5 0 3.87-1.79 7-4 7z" fill="#16a34a"/>
                      </svg>
                    </div>
                    <strong>High-protein</strong>
                    <span>Pentru construirea masei musculare.</span>
                  </div>
                  <div className="diet-card">
                    <div className="diet-icon">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                        <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
                      </svg>
                    </div>
                    <strong>Low-carb &amp; Keto</strong>
                    <span>Pentru gestionarea greutății și energie stabilă.</span>
                  </div>
                  <div className="diet-card">
                    <div className="diet-icon">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                        <path d="M6 13.87A4 4 0 0 1 7.41 6a5.11 5.11 0 0 1 1.05-1.54 5 5 0 0 1 7.08 0A5.11 5.11 0 0 1 16.59 6 4 4 0 0 1 18 13.87V21H6V13.87z" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
                        <line x1="6" y1="17" x2="18" y2="17" stroke="#16a34a" strokeWidth="1.8" strokeLinecap="round"/>
                      </svg>
                    </div>
                    <strong>Mediteraneană</strong>
                    <span>Echilibru, gust și beneficii dovedite.</span>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* ── Testimonial ── */}
          <section className="testimonial-section">
            <div className="container testimonial-inner">
              <blockquote>
                „Am descoperit zeci de rețete pe care le pot găti rapid și care se potrivesc perfect cu obiectivele mele. NutriVision mi-a schimbat modul în care mă raportez la mâncare."
              </blockquote>
              <div className="testimonial-author">
                <div className="author-avatar">A</div>
                <div>
                  <strong>Andreea M.</strong>
                  <span>Utilizator NutriVision</span>
                </div>
              </div>
            </div>
          </section>

          {/* ── CTA banner ── */}
          <section className="cta-section">
            <div className="container">
              <div className="cta-banner">
                <h2>Gata de schimbare?</h2>
                <p>Configurează-ți profilul și primește rețete personalizate generate de AI în câteva secunde.</p>
                <a href="#" className="cta-btn" onClick={nav('profile')}>Configurează profilul →</a>
              </div>
            </div>
          </section>
        </main>
      )}
    </div>
  )
}

export default App
