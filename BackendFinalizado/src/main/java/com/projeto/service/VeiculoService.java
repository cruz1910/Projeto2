package com.projeto.service;

import com.projeto.dto.VeiculoDTO;
import com.projeto.mapper.VeiculoMapper; // Importa o mapper
import com.projeto.model.Veiculo;
import com.projeto.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoMapper veiculoMapper; // Injeta o mapper

    @Transactional
    public VeiculoDTO criarVeiculo(VeiculoDTO veiculoDTO) {
        // Converte o DTO para a entidade Veiculo usando o mapper
        Veiculo veiculo = veiculoMapper.toVeiculo(veiculoDTO);

        // Define a data de cadastro se não foi fornecida (ou force a data atual)
        if (veiculo.getDataCadastro() == null || veiculo.getDataCadastro().isEmpty()) {
            veiculo.setDataCadastro(LocalDate.now().toString()); // Define a data atual
        }
        // Garante que 'disponivel' seja true ao criar
        if (veiculo.getDisponivel() == null) {
            veiculo.setDisponivel(true);
        }

        System.out.println("DEBUG (VeiculoService): Tentando salvar veículo: " +
                "Marca: " + veiculo.getMarca() +
                ", Modelo: " + veiculo.getModelo() +
                ", Cor: " + veiculo.getCor() +
                ", Imagem URL: " + veiculo.getImagem());

        Veiculo savedVeiculo = veiculoRepository.save(veiculo);
        // Retorna o DTO do veículo salvo, convertendo de volta
        return veiculoMapper.toVeiculoDTO(savedVeiculo);
    }

    @Transactional
    public VeiculoDTO atualizarVeiculo(Long id, VeiculoDTO veiculoDTO) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + id));

        // Atualiza os campos do veículo existente com os dados do DTO usando o mapper
        // Isso é mais seguro para evitar sobrescrever campos não intencionais.
        // O MapStruct pode ter @Mapping para ignorar campos ou copiar de forma específica.
        // Aqui, fazemos manualmente para ter controle explícito dos campos atualizados.
        veiculoExistente.setMarca(veiculoDTO.getMarca());
        veiculoExistente.setModelo(veiculoDTO.getModelo());
        veiculoExistente.setCor(veiculoDTO.getCor());
        veiculoExistente.setAno(veiculoDTO.getAno());
        veiculoExistente.setDescricao(veiculoDTO.getDescricao());
        veiculoExistente.setPreco(veiculoDTO.getPreco());
        veiculoExistente.setImagem(veiculoDTO.getImagem()); // Atualiza a URL da imagem

        Veiculo updatedVeiculo = veiculoRepository.save(veiculoExistente);
        return veiculoMapper.toVeiculoDTO(updatedVeiculo);
    }

    @Transactional(readOnly = true)
    public List<VeiculoDTO> listarVeiculos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        // Converte a lista de Veiculo para lista de VeiculoDTO usando o mapper
        return veiculos.stream()
                .map(veiculoMapper::toVeiculoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VeiculoDTO buscarPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + id));
        return veiculoMapper.toVeiculoDTO(veiculo);
    }

    @Transactional
    public void deletarVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }
}