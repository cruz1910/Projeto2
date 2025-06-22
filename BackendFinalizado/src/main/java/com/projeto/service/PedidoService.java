package com.projeto.service;

import com.projeto.exception.PedidoException;
import com.projeto.model.ItemPedido;
import com.projeto.model.Pedido;
import com.projeto.model.Veiculo;
import com.projeto.repository.ItemPedidoRepository;
import com.projeto.repository.PedidoRepository;
import com.projeto.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        validarPedido(pedido);
        verificarDisponibilidadeVeiculos(pedido);

        calcularValorTotal(pedido);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        atualizarDisponibilidadeVeiculos(pedidoSalvo, false);

        return pedidoSalvo;
    }

    @Transactional
    public Pedido atualizarPedido(Pedido pedido) {
        Pedido pedidoExistente = pedidoRepository.findById(pedido.getId())
                .orElseThrow(() -> new PedidoException("Pedido não encontrado com ID: " + pedido.getId()));

        // Mantém o cliente existente
        pedido.setCliente(pedidoExistente.getCliente());
        
        // Primeiro salvamos o pedido sem os itens
        pedido.setItens(null);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Depois salvamos os itens
        pedidoSalvo.setItens(pedido.getItens());
        return pedidoRepository.save(pedidoSalvo);
    }

    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException("Pedido não encontrado com ID: " + id));

        atualizarDisponibilidadeVeiculos(pedido, true);

        itemPedidoRepository.deleteAll(pedido.getItens());
        pedidoRepository.delete(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoException("Pedido não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    // Métodos privados para organização

    private void validarPedido(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new PedidoException("Cliente não informado");
        }

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new PedidoException("Nenhum item foi adicionado ao pedido");
        }
    }

    private void verificarDisponibilidadeVeiculos(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            Veiculo veiculo = veiculoRepository.findById(item.getVeiculo().getId())
                    .orElseThrow(() -> new PedidoException("Veículo não encontrado com ID: " + item.getVeiculo().getId()));

            if (!Boolean.TRUE.equals(veiculo.getDisponivel())) {
                throw new PedidoException("Veículo com ID " + veiculo.getId() + " não está disponível");
            }

            item.setVeiculo(veiculo);
            item.setPreco(veiculo.getPreco());
            item.setPedido(pedido);
        }
    }

    private void calcularValorTotal(Pedido pedido) {
        BigDecimal total = pedido.getItens().stream()
                .map(ItemPedido::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);
    }

    private void atualizarDisponibilidadeVeiculos(Pedido pedido, boolean disponivel) {
        for (ItemPedido item : pedido.getItens()) {
            Veiculo veiculo = item.getVeiculo();
            veiculo.setDisponivel(disponivel);
            veiculoRepository.save(veiculo);
        }
    }
}
