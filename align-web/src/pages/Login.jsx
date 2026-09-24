import { useState } from 'react'

function Login() {

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  async function handleLogin(event) {
    event.preventDefault()

    setError('')

    try {

      const response = await fetch(
        'http://localhost:8080/auth/login',
        {
          method: 'POST',

          headers: {
            'Content-Type': 'application/json'
          },

          body: JSON.stringify({
            email,
            password
          })
        }
      )

      if (!response.ok) {
        throw new Error('Email ou senha inválidos')
      }

      const data = await response.json()

      localStorage.setItem('token', data.token)

      console.log('Login realizado:', data)

    } catch (error) {
      setError(error.message)
    }
  }

  return (
    <div>

      <h1>ALIGN</h1>

      <h2>Entrar</h2>

      <form onSubmit={handleLogin}>

        <div>
          <label>Email</label>

          <input
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
        </div>

        <div>
          <label>Senha</label>

          <input
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        <button type="submit">
          Entrar
        </button>

      </form>

      {error && <p>{error}</p>}

    </div>
  )
}

export default Login