package com.projeto.controller;

import com.projeto.dto.VeiculoDTO; // Importa o DTO
import com.projeto.service.VeiculoService;
import jakarta.validation.Valid; // Para usar @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<VeiculoDTO> criarVeiculo(@Valid @RequestBody VeiculoDTO veiculoDTO) {
        VeiculoDTO novoVeiculo = veiculoService.criarVeiculo(veiculoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoDTO veiculoDTO) {
        VeiculoDTO veiculoAtualizado = veiculoService.atualizarVeiculo(id, veiculoDTO);
        return ResponseEntity.ok(veiculoAtualizado);
    }

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listarVeiculos() {
        List<VeiculoDTO> veiculos = veiculoService.listarVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarPorId(@PathVariable Long id) {
        VeiculoDTO veiculo = veiculoService.buscarPorId(id);
        return ResponseEntity.ok(veiculo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}