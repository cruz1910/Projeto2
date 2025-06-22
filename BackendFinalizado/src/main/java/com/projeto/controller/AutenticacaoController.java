package com.projeto.controller;

import com.projeto.dto.LoginRequestDTO;
import com.projeto.dto.UsuarioDTO;
import com.projeto.exception.AutenticacaoException;
import com.projeto.model.Usuario;
import com.projeto.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Testado no postman
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            Usuario usuario = autenticacaoService.autenticar(loginRequest);
            UsuarioDTO dto = new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                "", "", // senha omitida
                usuario.getTipo()
            );
            return ResponseEntity.ok(dto);
        } catch (AutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
