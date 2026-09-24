const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

export function getToken() { return localStorage.getItem('align_token') }
export function setSession(data) {
  localStorage.setItem('align_token', data.token)
  localStorage.setItem('align_user', JSON.stringify({ name: data.name || 'Você', email: data.email || '' }))
}
export function clearSession() { localStorage.removeItem('align_token'); localStorage.removeItem('align_user') }
export function getUser() { try { return JSON.parse(localStorage.getItem('align_user')) || { name: 'Você' } } catch { return { name: 'Você' } } }

async function request(path, options = {}) {
  const headers = { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...options.headers }
  const token = getToken()
  if (token) headers.Authorization = `Bearer ${token}`
  const response = await fetch(`${API_URL}${path}`, { ...options, headers })
  if (response.status === 401 || response.status === 403) {
    if (token) window.dispatchEvent(new Event('align:unauthorized'))
  }
  if (!response.ok) {
    let message = 'Não foi possível concluir a ação.'
    try { const body = await response.json(); message = body.message || body.error || message } catch { /* sem corpo */ }
    throw new Error(message)
  }
  if (response.status === 204) return null
  const text = await response.text()
  return text ? JSON.parse(text) : null
}

export const api = {
  login: (body) => request('/auth/login', { method: 'POST', body: JSON.stringify(body) }),
  register: (body) => request('/auth/register', { method: 'POST', body: JSON.stringify(body) }),
  dashboard: () => request('/dashboard'),
  goals: () => request('/goals'),
  createGoal: (body) => request('/goals', { method: 'POST', body: JSON.stringify(body) }),
  updateGoal: (id, body) => request(`/goals/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteGoal: (id) => request(`/goals/${id}`, { method: 'DELETE' }),
  tasks: (goalId) => request(`/goals/${goalId}/tasks`),
  createTask: (goalId, body) => request(`/goals/${goalId}/tasks`, { method: 'POST', body: JSON.stringify(body) }),
  updateTask: (goalId, taskId, body) => request(`/goals/${goalId}/tasks/${taskId}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteTask: (goalId, taskId) => request(`/goals/${goalId}/tasks/${taskId}`, { method: 'DELETE' }),
  habits: () => request('/habits'),
  createHabit: (body) => request('/habits', { method: 'POST', body: JSON.stringify(body) }),
  updateHabit: (id, body) => request(`/habits/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteHabit: (id) => request(`/habits/${id}`, { method: 'DELETE' }),
  completeHabit: (id) => request(`/habits/${id}/completions`, { method: 'POST' }),
  habitStats: (id) => request(`/habits/${id}/stats`),
}
