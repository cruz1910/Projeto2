package com.projeto.service;

import com.projeto.dto.LoginRequestDTO;
import com.projeto.exception.AutenticacaoException;
import com.projeto.model.Usuario;
import com.projeto.repository.UsuarioRepository;
import com.projeto.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(LoginRequestDTO loginRequest) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getSenha() == null) {
            throw new AutenticacaoException("Dados de login inválidos");
        }

        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AutenticacaoException("Email não encontrado"));

        String senhaHash = HashUtil.gerarHashSHA256(loginRequest.getSenha());

        if (!senhaHash.equals(usuario.getSenha())) {
            throw new AutenticacaoException("Senha inválida");
        }

        return usuario;
    }
}
