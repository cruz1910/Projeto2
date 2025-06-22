import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { FaShoppingCart } from 'react-icons/fa';
import './NavbarSwitcher.css';

const NavbarSwitcher = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem('user');
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        {/* Redireciona para o painel ao clicar no CompreCar */}
        <Link to="/painel" className="navbar-title">CompreCar</Link>
      </div>
      <div className="navbar-right">
        {user && (
          <>
            <Link className="navbar-link" to="/painel">Início</Link>

            {['FUNCIONARIO', 'ADMIN'].includes(user.tipo) && (
              <>
                <Link className="navbar-link" to="/veiculos">Veículos</Link>
                <Link  className="navbar-link" to="/pedidos">Pedidos</Link>
              </>
            )}

            {user.tipo === 'ADMIN' && <Link to="/funcionarios">Funcionários</Link>}

            <span className="welcome-message">Bem-vindo, {user.nome || user.email}</span>

            <button onClick={logout} className="logout-btn">Sair</button>

            {user.tipo === 'CLIENTE' && (
              <Link to="/carrinho" className="cart-icon">
                <FaShoppingCart size={18} />
              </Link>
            )}
          </>
        )}
      </div>
    </nav>
  );
};

export default NavbarSwitcher;
