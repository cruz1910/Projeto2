package com.projeto.service;

import com.projeto.dto.UsuarioDTO;
import com.projeto.enums.TipoUsuario;
import com.projeto.exception.UsuarioException;
import com.projeto.mapper.UsuarioMapper;
import com.projeto.model.Usuario;
import com.projeto.repository.UsuarioRepository;
import com.projeto.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getTipo() == null) {
            throw new UsuarioException("Tipo de usuário é obrigatório");
        }

        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new UsuarioException("Email já cadastrado");
        }

        // Validar nome
        if (usuarioDTO.getNome() == null || usuarioDTO.getNome().length() < 2) {
            throw new UsuarioException("O nome do usuário deve conter ao menos 2 caracteres");
        }

        // Somente CLIENTE exige confirmação de senha
        if (usuarioDTO.getTipo() == TipoUsuario.CLIENTE) {
            if (usuarioDTO.getSenha() == null || usuarioDTO.getConfirmacaoSenha() == null) {
                throw new UsuarioException("Senha e confirmação são obrigatórias");
            }
            if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
                throw new UsuarioException("As senhas não coincidem");
            }
            if (usuarioDTO.getSenha().length() < 8) {
                throw new UsuarioException("A senha deve ter ao menos 8 caracteres");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTipo(usuarioDTO.getTipo());

        if (usuarioDTO.getSenha() != null) {
            usuario.setSenha(HashUtil.gerarHashSHA256(usuarioDTO.getSenha()));
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toUsuarioDTO(savedUsuario);
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTipo(usuarioDTO.getTipo());

        // Atualizar senha se fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            if (usuarioDTO.getTipo() == TipoUsuario.CLIENTE) {
                if (usuarioDTO.getConfirmacaoSenha() == null || usuarioDTO.getConfirmacaoSenha().isEmpty()) {
                    throw new UsuarioException("Confirmação de senha é obrigatória para alteração");
                }
                if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
                    throw new UsuarioException("As senhas não coincidem");
                }
            }
            usuario.setSenha(HashUtil.gerarHashSHA256(usuarioDTO.getSenha()));
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toUsuarioDTO(updatedUsuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));
        return UsuarioMapper.INSTANCE.toUsuarioDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado"));
        return UsuarioMapper.INSTANCE.toUsuarioDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper.INSTANCE::toUsuarioDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarFuncionarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Funcionário não encontrado"));
        
        if (usuario.getTipo() != TipoUsuario.FUNCIONARIO) {
            throw new UsuarioException("Usuário não é um funcionário");
        }
        
        return UsuarioMapper.INSTANCE.toUsuarioDTO(usuario);
    }

    @Transactional
    public UsuarioDTO atualizarFuncionario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Funcionário não encontrado"));
        
        if (usuario.getTipo() != TipoUsuario.FUNCIONARIO) {
            throw new UsuarioException("Usuário não é um funcionário");
        }

        // Atualiza apenas os campos que foram fornecidos
        if (usuarioDTO.getNome() != null) {
            usuario.setNome(usuarioDTO.getNome());
        }
        if (usuarioDTO.getEmail() != null) {
            usuario.setEmail(usuarioDTO.getEmail());
        }
        usuario.setTipo(usuarioDTO.getTipo());

        // Atualizar senha se fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            if (usuarioDTO.getTipo() == TipoUsuario.CLIENTE) {
                if (usuarioDTO.getConfirmacaoSenha() == null || usuarioDTO.getConfirmacaoSenha().isEmpty()) {
                    throw new UsuarioException("Confirmação de senha é obrigatória para alteração");
                }
                if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
                    throw new UsuarioException("As senhas não coincidem");
                }
            }
            usuario.setSenha(HashUtil.gerarHashSHA256(usuarioDTO.getSenha()));
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toUsuarioDTO(updatedUsuario);
    }

    @Transactional
    public void deletarFuncionario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Funcionário não encontrado"));
        
        if (usuario.getTipo() != TipoUsuario.FUNCIONARIO) {
            throw new UsuarioException("Usuário não é um funcionário");
        }
        
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarFuncionarios() {
        return usuarioRepository.findByTipo(TipoUsuario.FUNCIONARIO)
                .stream()
                .map(UsuarioMapper.INSTANCE::toUsuarioDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioException("Usuário não encontrado para exclusão");
        }

        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new UsuarioException(
                    "Não foi possível excluir o usuário. Verifique se ele está associado a outros dados.");
        }

    }
}
