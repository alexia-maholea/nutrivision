export function AdminRecipeForm({
  isOpen,
  isLoading,
  formData,
  setFormData,
  onSave,
  onClose,
}: {
  isOpen: boolean
  isLoading: boolean
  formData: {
    title: string
    description: string
    calories: string
    protein: string
    carbs: string
    fat: string
    cooking_time: string
    difficulty: string
  }
  setFormData: (data: any) => void
  onSave: () => void
  onClose: () => void
}) {
  if (!isOpen) return null

  return (
    <div className="recipe-modal-overlay" onClick={e => e.target === e.currentTarget && onClose()}>
      <div className="recipe-modal">
        <button className="modal-close" onClick={onClose}>×</button>
        <div className="modal-content">
          <h2>{formData.title === '' ? 'Adauga rețetă' : 'Editează rețetă'}</h2>

          <div className="form-group">
            <label>Titlu *</label>
            <input
              type="text"
              value={formData.title}
              onChange={e => setFormData({ ...formData, title: e.target.value })}
              placeholder="Titlul rețetei"
            />
          </div>

          <div className="form-group">
            <label>Descriere</label>
            <textarea
              value={formData.description}
              onChange={e => setFormData({ ...formData, description: e.target.value })}
              placeholder="Descrierea rețetei"
              rows={3}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Calorii</label>
              <input
                type="number"
                value={formData.calories}
                onChange={e => setFormData({ ...formData, calories: e.target.value })}
                placeholder="0"
              />
            </div>
            <div className="form-group">
              <label>Proteine (g)</label>
              <input
                type="number"
                value={formData.protein}
                onChange={e => setFormData({ ...formData, protein: e.target.value })}
                placeholder="0"
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label>Glucide (g)</label>
              <input
                type="number"
                value={formData.carbs}
                onChange={e => setFormData({ ...formData, carbs: e.target.value })}
                placeholder="0"
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label>Grăsimi (g)</label>
              <input
                type="number"
                value={formData.fat}
                onChange={e => setFormData({ ...formData, fat: e.target.value })}
                placeholder="0"
                step="0.1"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Timp gătit (min)</label>
              <input
                type="number"
                value={formData.cooking_time}
                onChange={e => setFormData({ ...formData, cooking_time: e.target.value })}
                placeholder="0"
              />
            </div>
            <div className="form-group">
              <label>Dificultate</label>
              <select value={formData.difficulty} onChange={e => setFormData({ ...formData, difficulty: e.target.value })}>
                <option value="EASY">Ușor</option>
                <option value="MEDIUM">Mediu</option>
                <option value="HARD">Dificil</option>
              </select>
            </div>
          </div>

          <div className="form-actions">
            <button className="btn-cancel" onClick={onClose} disabled={isLoading}>Anulează</button>
            <button className="btn-save" onClick={onSave} disabled={isLoading}>
              {isLoading ? 'Se salvează...' : 'Salvează'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export function DeleteConfirmDialog({
  isOpen,
  isLoading,
  onConfirm,
  onCancel,
}: {
  isOpen: boolean
  isLoading: boolean
  onConfirm: () => void
  onCancel: () => void
}) {
  if (!isOpen) return null

  return (
    <div className="recipe-modal-overlay" onClick={e => e.target === e.currentTarget && onCancel()}>
      <div className="recipe-modal delete-confirm">
        <div className="modal-content">
          <h2>Confirmi ștergerea?</h2>
          <p>Această acțiune nu poate fi anulată.</p>

          <div className="form-actions">
            <button className="btn-cancel" onClick={onCancel} disabled={isLoading}>Anulează</button>
            <button className="btn-delete" onClick={onConfirm} disabled={isLoading}>
              {isLoading ? 'Se șterge...' : 'Șterge'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
