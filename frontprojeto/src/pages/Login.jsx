import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';
import api from '../services/api';
import '../styles/Login.css';

const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', { email, senha });
      localStorage.setItem('user', JSON.stringify(response.data));
      navigate('/dashboard');
    } catch (error) {
      console.error('Erro no login:', error.response?.data || error.message);
      setError(error.response?.data?.message || 'Erro ao fazer login. Por favor, tente novamente.');
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleLogin}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
          required
        />
        <button type="submit" className="login-button">Entrar</button>
      </form>
      <p className="register-link">
        Não tem uma conta? <Link to="/cadastro">Cadastre-se aqui</Link>
      </p>
    </div>
  );
};

export default Login;
