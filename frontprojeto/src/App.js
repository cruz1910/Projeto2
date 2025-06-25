import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './pages/Login.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Veiculos from './pages/Veiculos.jsx';
import Funcionarios from './pages/Funcionarios.jsx';

import Cart from './pages/Cart.jsx';
import Register from './pages/Register.jsx';
import NavbarSwitcher from './components/NavbarSwitcher.jsx';
import PrivateRoute from './routes/PrivateRoute.jsx';
import PrivateRouteAdmin from './routes/PrivateRouteAdmin.jsx';
import { testConnection } from './services/testApi';
import './styles/ConnectionError.css';

function App() {
  const [isConnected, setIsConnected] = useState(false);
  const [connectionError, setConnectionError] = useState('');

  useEffect(() => {
    const checkConnection = async () => {
      try {
        const isConnected = await testConnection();
        setIsConnected(isConnected);
      } catch (error) {
        setConnectionError('Não foi possível conectar ao servidor. Por favor, verifique se o backend está rodando.');
      }
    };
    checkConnection();
  }, []);

  if (!isConnected && connectionError) {
    return (
      <div className="connection-error">
        <h2>{connectionError}</h2>
        <p>Por favor, verifique se:</p>
        <ul>
          <li>O backend está rodando na porta 8080</li>
          <li>O servidor não está retornando erros</li>
        </ul>
      </div>
    );
  }

  return (
    <>
      <NavbarSwitcher />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route
          path="/painel"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/veiculos"
          element={
            <PrivateRoute>
              <Veiculos />
            </PrivateRoute>
          }
        />
        <Route
          path="/funcionarios"
          element={
            <PrivateRouteAdmin>
              <Funcionarios />
            </PrivateRouteAdmin>
          }
        />

        <Route path="/carrinho" element={
          <PrivateRoute>
            <Cart />
          </PrivateRoute>
        } />
        <Route path="/login" element={<Login />} />
        <Route path="/cadastro" element={<Register />} />
      </Routes>
    </>
  );
}

export default App;
