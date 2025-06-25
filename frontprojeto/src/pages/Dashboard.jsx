import React, { useEffect, useState } from 'react';
import api from '../services/api';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const [veiculos, setVeiculos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    listarVeiculos();
  }, []);

  const listarVeiculos = async () => {
    try {
      const response = await api.get('/veiculos');
      setVeiculos(response.data);
    } catch (error) {
      console.error('Erro ao listar veículos:', error);
      alert('Erro ao carregar veículos.');
    } finally {
      setLoading(false);
    }
  };

  const adicionarAoCarrinho = (veiculo) => {
    let carrinhoAtual = JSON.parse(localStorage.getItem('cart')) || [];

    // Verificar se o veículo já está no carrinho
    const jaExiste = carrinhoAtual.find((item) => item.id === veiculo.id);

    if (jaExiste) {
      alert('Este veículo já está no carrinho.');
      return;
    }

    // Adicionar o veículo com quantidade 1
    const veiculoComQuantidade = { ...veiculo, quantidade: 1 };

    carrinhoAtual.push(veiculoComQuantidade);
    localStorage.setItem('cart', JSON.stringify(carrinhoAtual));

    alert('Veículo adicionado ao carrinho com sucesso!');
  };

  if (!user) {
    return null;
  }

  return (
    <>
      <div className="dashboard-container">
        <div className="vehicles-section">
          {loading ? (
            <div className="loading">Carregando veículos...</div>
          ) : (
            <div className="vehicles-grid">
              {veiculos.map((veiculo) => (
                <div key={veiculo.id} className="vehicle-card">
                  <div className="vehicle-image">
                    {veiculo.imagem && (
                      <img
                        src={veiculo.imagem}
                        alt={veiculo.modelo}
                        className="vehicle-img"
                      />
                    )}
                  </div>
                  <div className="vehicle-info">
                    <h3>{veiculo.marca} {veiculo.modelo}</h3>
                    <p>Ano: {veiculo.ano}</p>
                    <p>Cor: {veiculo.cor}</p>
                    <p>Preço: R$ {veiculo.preco?.toFixed(2)}</p>
                    {veiculo.descricao && (
                      <p className="description">{veiculo.descricao}</p>
                    )}

                    {/* Mostrar botão apenas se o usuário for CLIENTE */}
                    {user.tipo === 'CLIENTE' && (
                      <button
                        className="add-to-cart-btn"
                        onClick={() => adicionarAoCarrinho(veiculo)}
                      >
                        Adicionar ao Carrinho
                      </button>
                    )}
                  </div>
                </div>
              ))}
              {veiculos.length === 0 && !loading && (
                <p className="no-vehicles">Nenhum veículo disponível no momento.</p>
              )}
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default Dashboard;
