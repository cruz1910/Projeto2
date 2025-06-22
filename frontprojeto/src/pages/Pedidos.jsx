import React, { useEffect, useState } from 'react';
import api from '../services/api';
import '../styles/Pedidos.css';

const Pedidos = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const [veiculos, setVeiculos] = useState([]);
  const [pedidos, setPedidos] = useState([]);
  const [carrinho, setCarrinho] = useState([]);

  const listarVeiculos = async () => {
    try {
      const response = await api.get('/veiculos');
      setVeiculos(response.data);
    } catch (error) {
      console.error('Erro ao listar veículos:', error);
    }
  };

  const listarPedidos = async () => {
    try {
      let response;
      if (user.tipo === 'CLIENTE') {
        response = await api.get(`/pedidos/cliente/${user.id}`);
      } else {
        response = await api.get('/pedidos');
      }

      // Filtrar apenas os pedidos pendentes
      const pedidosPendentes = response.data.filter((p) => p.status === 'PENDENTE');
      setPedidos(pedidosPendentes);
    } catch (error) {
      console.error('Erro ao listar pedidos:', error);
    }
  };

  useEffect(() => {
    listarVeiculos();
    listarPedidos();
  }, []);

  const adicionarAoCarrinho = (veiculo) => {
    if (!carrinho.find((item) => item.id === veiculo.id)) {
      setCarrinho([...carrinho, veiculo]);
    }
  };

  const removerDoCarrinho = (id) => {
    setCarrinho(carrinho.filter((item) => item.id !== id));
  };

  const finalizarPedido = async () => {
    if (carrinho.length === 0) {
      alert('Carrinho vazio');
      return;
    }

    const itens = carrinho.map((v) => ({
      veiculoId: v.id,
      preco: v.preco,
    }));

    const valorTotal = carrinho.reduce((acc, v) => acc + v.preco, 0);

    const data = {
      clienteId: user.id,
      dataPedido: new Date().toISOString(),
      status: 'PENDENTE',
      valorTotal,
      itens,
    };

    try {
      await api.post('/pedidos', data);
      setCarrinho([]);
      listarPedidos();
    } catch (error) {
      console.error('Erro ao finalizar pedido:', error);
    }
  };

  const deletarPedido = async (id) => {
    if (window.confirm('Deseja realmente excluir este pedido?')) {
      try {
        await api.delete(`/pedidos/${id}`);
        listarPedidos();
      } catch (error) {
        console.error('Erro ao excluir pedido:', error);
      }
    }
  };

  return (
    <div className="container">
      {/* Se for cliente, mostrar o carrinho */}
      {user.tipo === 'CLIENTE' && (
        <div className="carrinho">
          <h2>Veículos Disponíveis</h2>
          <ul>
            {veiculos.map((v) => (
              <li key={v.id}>
                {v.modelo} - R$ {v.preco.toFixed(2)}{' '}
                <button onClick={() => adicionarAoCarrinho(v)}>Adicionar</button>
              </li>
            ))}
          </ul>

          <h2>Carrinho</h2>
          <ul>
            {carrinho.map((v) => (
              <li key={v.id}>
                {v.modelo} - R$ {v.preco.toFixed(2)}{' '}
                <button onClick={() => removerDoCarrinho(v.id)}>Remover</button>
              </li>
            ))}
          </ul>

          <button onClick={finalizarPedido}>Finalizar Pedido</button>
        </div>
      )}

      <h2>Pedidos Pendentes</h2>
      <table>
        <thead>
          <tr>
            <th>Numero do Pedido</th>
            <th>Data</th>
            <th>Status</th>
            <th>Valor Total</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {pedidos.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{new Date(p.dataPedido).toLocaleDateString()} {new Date(p.dataPedido).toLocaleTimeString()}</td>
              <td>{p.status}</td>
              <td>R$ {p.valorTotal.toFixed(2)}</td>
              <td>
                {/* alterar função, não deletarPedido */}
                <button onClick={() => deletarPedido(p.id)}>Processar</button> 
                <button onClick={() => deletarPedido(p.id)}>Finalizar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Pedidos;
