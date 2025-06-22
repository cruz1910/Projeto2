import React, { useEffect, useState } from 'react';
import api from '../services/api';
import '../styles/Funcionarios.css';

const Funcionarios = () => {
  const [funcionarios, setFuncionarios] = useState([]);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmacaoSenha, setConfirmacaoSenha] = useState('');
  const [editId, setEditId] = useState(null);
  const [loading, setLoading] = useState(false);

  // Buscar lista de funcionários
  const listarFuncionarios = async () => {
    try {
      setLoading(true);
      const response = await api.get('/usuarios/funcionarios');
      setFuncionarios(response.data);
    } catch (error) {
      console.error('Erro ao listar funcionários:', error);
      alert('Erro ao carregar a lista de funcionários');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    listarFuncionarios();
  }, []);

  // Submissão de formulário: cadastrar ou atualizar
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = {
        nome,
        email,
        senha: senha || null,
        confirmacaoSenha: confirmacaoSenha || null,
        tipo: 'FUNCIONARIO'
      };

      if (editId) {
        // Atualizar funcionário existente
        await api.put(`/usuarios/funcionarios/${editId}`, data);
        setEditId(null);
      } else {
        // Cadastrar novo funcionário
        await api.post('/usuarios', data);
      }

      // Limpar campos e recarregar lista
      setNome('');
      setEmail('');
      setSenha('');
      setConfirmacaoSenha('');
      listarFuncionarios();
    } catch (error) {
      console.error(error);
      alert(error.response?.data?.message || 'Erro ao salvar funcionário');
    } finally {
      setLoading(false);
    }
  };

  // Preencher formulário para edição
  const handleEdit = (func) => {
    setEditId(func.id);
    setNome(func.nome);
    setEmail(func.email);
    setSenha(''); // Limpa a senha ao editar
    setConfirmacaoSenha('');
  };

  // Excluir funcionário
  const handleDelete = async (id) => {
    if (window.confirm('Deseja realmente excluir este funcionário?')) {
      setLoading(true);
      try {
        await api.delete(`/usuarios/funcionarios/${id}`);
        // Atualiza a lista removendo o funcionário excluído
        setFuncionarios(funcionarios.filter(func => func.id !== id));
      } catch (error) {
        console.error(error);
        alert(error.response?.data?.message || 'Erro ao excluir funcionário');
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div className="container">
      <h1>Gerenciar Funcionários</h1>

      <form onSubmit={handleSubmit} className="form">
        <input
          type="text"
          placeholder="Nome"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          required
        />
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
          required={!editId}
        />
        <input
          type="password"
          placeholder="Confirme a senha"
          value={confirmacaoSenha}
          onChange={(e) => setConfirmacaoSenha(e.target.value)}
          required={!editId}
        />
        <button
          type="submit"
          disabled={loading}
          style={{
            backgroundColor: editId ? '#ff9800' : '#3f51b5',
            color: 'white',
            padding: '10px 20px',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            opacity: loading ? 0.5 : 1
          }}
        >
          {loading ? 'Carregando...' : editId ? 'Atualizar' : 'Cadastrar'}
        </button>
      </form>

      <table>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Email</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {funcionarios.map((f) => (
            <tr key={f.id}>
              <td>{f.nome}</td>
              <td>{f.email}</td>
              <td>
                <button onClick={() => handleEdit(f)}>Editar</button>
                <button onClick={() => handleDelete(f.id)}>Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Funcionarios;