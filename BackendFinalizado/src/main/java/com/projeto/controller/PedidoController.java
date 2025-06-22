package com.projeto.controller;

import com.projeto.dto.ItemPedidoDTO;
import com.projeto.dto.PedidoDTO;

import com.projeto.exception.PedidoException;
import com.projeto.mapper.PedidoMapper;
import com.projeto.model.ItemPedido;
import com.projeto.model.Pedido;
import com.projeto.model.Usuario;
import com.projeto.model.Veiculo;
import com.projeto.repository.UsuarioRepository;
import com.projeto.repository.VeiculoRepository;
import com.projeto.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private VeiculoRepository veiculoRepository;
// Testado no postman
    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            // Primeiro validamos o cliente
            Usuario cliente = usuarioRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new PedidoException("Cliente não encontrado com ID: " + pedidoDTO.getClienteId()));

            // Criamos o pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            pedido.setDataPedido(LocalDateTime.parse(pedidoDTO.getDataPedido().toString(), formatter));
            pedido.setStatus(pedidoDTO.getStatus().name());
            
            // Criamos os itens do pedido
            for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
                Veiculo veiculo = veiculoRepository.findById(itemDTO.getVeiculoId())
                    .orElseThrow(() -> new PedidoException("Veículo não encontrado com ID: " + itemDTO.getVeiculoId()));
                
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setVeiculo(veiculo);
                item.setPreco(itemDTO.getPreco());
                
                pedido.getItens().add(item);
            }

            // Calculamos o valor total
            pedido.setValorTotal(pedidoDTO.getValorTotal());

            // Salvamos o pedido
            Pedido pedidoSalvo = pedidoService.criarPedido(pedido);
            PedidoDTO pedidoDTOSalvo = PedidoMapper.INSTANCE.toPedidoDTO(pedidoSalvo);
            return ResponseEntity.status(201).body(pedidoDTOSalvo);
        } catch (PedidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
// Testado no postman
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            PedidoDTO pedidoDTO = PedidoMapper.INSTANCE.toPedidoDTO(pedido);
            return ResponseEntity.ok(pedidoDTO);
        } catch (PedidoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
// Testado no postman
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.listarPedidos();
            List<PedidoDTO> pedidosDTO = pedidos.stream()
                    .map(PedidoMapper.INSTANCE::toPedidoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pedidosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Testado no postman
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@PathVariable Long clienteId) {
        try {
            List<Pedido> pedidos = pedidoService.listarPedidosPorCliente(clienteId);
            List<PedidoDTO> pedidosDTO = pedidos.stream()
                    .map(PedidoMapper.INSTANCE::toPedidoDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(pedidosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // Testado no postman
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> atualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = PedidoMapper.INSTANCE.toPedido(pedidoDTO);
            pedido.setId(id);
            Pedido pedidoAtualizado = pedidoService.atualizarPedido(pedido);
            PedidoDTO pedidoDTOAtualizado = PedidoMapper.INSTANCE.toPedidoDTO(pedidoAtualizado);
            return ResponseEntity.ok(pedidoDTOAtualizado);
        } catch (PedidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
// Testado no postman
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        try {
            pedidoService.deletarPedido(id);
            return ResponseEntity.noContent().build();
        } catch (PedidoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
