import React, { useEffect, useState } from 'react';
import api from '../services/api'; // Seu axios instance
import '../styles/Veiculos.css';
import axios from 'axios'; // Para o upload de imagem, pode ser o axios direto

const Veiculos = () => {
  const [veiculos, setVeiculos] = useState([]);
  const [modelo, setModelo] = useState('');
  const [marca, setMarca] = useState('');
  const [ano, setAno] = useState('');
  const [cor, setCor] = useState('');
  const [descricao, setDescricao] = useState('');
  const [preco, setPreco] = useState('');
  const [dataCadastro, setDataCadastro] = useState(''); 
  const [imagem, setImagem] = useState(''); // Armazenará a URL da imagem
  const [imageFile, setImageFile] = useState(null); // Armazenará o objeto File para upload
  const [editId, setEditId] = useState(null);

  const listarVeiculos = async () => {
    try {
      const response = await api.get('/veiculos');
      setVeiculos(response.data);
    } catch (error) {
      console.error('Erro ao listar veículos:', error);
      alert('Erro ao carregar veículos.');
    }
  };

  useEffect(() => {
    listarVeiculos();
  }, []);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagem(reader.result);
      };
      reader.readAsDataURL(file);
    } else {
      setImageFile(null);
      setImagem('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    let imageUrl = imagem;

    try {
      if (imageFile) {
        const formData = new FormData();
        formData.append('image', imageFile);

        const uploadResponse = await axios.post('http://localhost:8080/api/upload/image', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        imageUrl = uploadResponse.data;
        console.log("DEBUG (Frontend): Imagem enviada para:", imageUrl);
      }

      const veiculoData = {
        modelo,
        marca,
        ano: parseInt(ano),
        cor,
        descricao,
        preco: parseFloat(preco),
        dataCadastro: new Date().toISOString().split('T')[0],
        imagem: imageUrl
      };

      if (editId) {
        await api.put(`/veiculos/${editId}`, veiculoData);
        setEditId(null);
      } else {
        await api.post('/veiculos', veiculoData);
      }

      setModelo('');
      setMarca('');
      setAno('');
      setCor('');
      setDescricao('');
      setPreco('');
      setDataCadastro('');
      setImagem('');
      setImageFile(null);
      listarVeiculos();
      alert(editId ? 'Veículo atualizado com sucesso!' : 'Veículo cadastrado com sucesso!');

    } catch (error) {
      console.error('Erro ao salvar veículo:', error.response ? error.response.data : error.message);
      alert(`Erro ao salvar veículo: ${error.response?.data?.message || error.message}`);
    }
  };

  const handleEdit = (veiculo) => {
    setEditId(veiculo.id);
    setModelo(veiculo.modelo);
    setMarca(veiculo.marca);
    setAno(veiculo.ano);
    setCor(veiculo.cor);
    setDescricao(veiculo.descricao);
    setPreco(veiculo.preco);
    setDataCadastro(veiculo.dataCadastro);
    setImagem(veiculo.imagem);
    setImageFile(null);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Deseja realmente excluir este veículo?')) {
      try {
        await api.delete(`/veiculos/${id}`);
        listarVeiculos();
        alert('Veículo excluído com sucesso!');
      } catch (error) {
        console.error('Erro ao excluir veículo:', error);
        alert('Erro ao excluir veículo.');
      }
    }
  };

  return (
    <div className="container">
      <h1>Gerenciar Veículos</h1>

      <form onSubmit={handleSubmit} className="veiculo-form">
        <div className="form-group">
          <div className="field-pair">
            <div className="field-item">
              <label>Marca *</label>
              <input
                type="text"
                placeholder="Marca"
                value={marca}
                onChange={(e) => setMarca(e.target.value)}
                required
                minLength="2"
                maxLength="50"
              />
            </div>
            <div className="field-item">
              <label>Modelo *</label>
              <input
                type="text"
                placeholder="Modelo"
                value={modelo}
                onChange={(e) => setModelo(e.target.value)}
                required
                minLength="2"
                maxLength="50"
              />
            </div>
          </div>
        </div>

        <div className="form-group">
          <div className="field-pair">
            <div className="field-item">
              <label>Cor *</label>
              <input
                type="text"
                placeholder="Cor"
                value={cor}
                onChange={(e) => setCor(e.target.value)}
                required
                minLength="2"
                maxLength="50"
              />
            </div>
            <div className="field-item">
              <label>Ano *</label>
              <input
                type="number"
                placeholder="Ano"
                value={ano}
                onChange={(e) => setAno(e.target.value)}
                required
                min="1900"
                max="2099"
              />
            </div>
          </div>
        </div>

        <div className="form-group">
          <label>Preço *</label>
          <input
            type="number"
            placeholder="Preço"
            value={preco}
            onChange={(e) => setPreco(e.target.value)}
            required
            min="0.01"
            step="0.01"
          />
        </div>


        <div className="form-group">
          <div className="field-item">
            <label>Descrição</label>
            <textarea
              placeholder="Descrição"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              maxLength="1000"
              rows="3"
            ></textarea>
          </div>
        </div>
        
        <div className="form-group">
          <label>Imagem</label>
          <div className="image-upload-container">
            {imagem && (
              <img
                src={imagem}
                alt="Preview"
                className="image-preview"
                style={{ maxWidth: '200px', maxHeight: '200px', objectFit: 'contain' }}
              />
            )}
            <input
              type="file"
              accept="image/*"
              onChange={(e) => handleImageChange(e)}
            />
            {imageFile && (
              <button onClick={() => { setImageFile(null); setImagem(''); }} className="remove-image-btn">
                Remover imagem
              </button>
            )}
             {!imageFile && imagem && (
                <button onClick={() => setImagem('')} className="remove-image-btn">
                  Remover imagem existente
                </button>
            )}
          </div>
        </div>

        <button type="submit" className="submit-btn">
          {editId ? 'Atualizar' : 'Cadastrar'}
        </button>
      </form>

      <h2>Veículos Cadastrados</h2>
      <table>
        <thead>
          <tr>
            <th>Marca</th>
            <th>Modelo</th>
            <th>Ano</th>
            <th>Cor</th>
            <th>Preço</th>
            <th>Imagem</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {veiculos.length > 0 ? (
            veiculos.map((veiculo) => (
              <tr key={veiculo.id}>
                <td>{veiculo.marca}</td>
                <td>{veiculo.modelo}</td>
                <td>{veiculo.ano}</td>
                <td>{veiculo.cor}</td>
                <td>R$ {veiculo.preco ? veiculo.preco.toFixed(2) : 'N/A'}</td>
                <td>
                  {veiculo.imagem && (
                    <img src={veiculo.imagem} alt={veiculo.modelo} style={{ width: '50px', height: 'auto' }} />
                  )}
                </td>
                <td>
                  <button onClick={() => handleEdit(veiculo)} className="edit-btn">Editar</button>
                  <button onClick={() => handleDelete(veiculo.id)} className="delete-btn">Excluir</button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="7">Nenhum veículo cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Veiculos;