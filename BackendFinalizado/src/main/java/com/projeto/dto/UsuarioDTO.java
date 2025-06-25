package com.projeto.dto;

import com.projeto.enums.TipoUsuario;
import com.projeto.exception.UsuarioException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDTO {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDTO.class);
    private static final int MIN_SENHA_LENGTH = 64;
    private static final int MAX_NOME_LENGTH = 100;
    private static final int MAX_EMAIL_LENGTH = 255;

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do usuário deve conter ao menos 2 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 64, message = "A senha deve ter ao menos 8 caracteres")
    private String senha;
    private String confirmacaoSenha;
    private TipoUsuario tipo;

    public UsuarioDTO() {
        logger.debug("Criado novo DTO vazio");
    }

    public UsuarioDTO(Long id, String nome, String email, String senha, String confirmacaoSenha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.confirmacaoSenha = confirmacaoSenha;
        this.tipo = tipo;
        logger.debug("Criado novo DTO com dados: {}", this);
    }

    public UsuarioDTO(String nome, String email, String senha, String confirmacaoSenha, TipoUsuario tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.confirmacaoSenha = confirmacaoSenha;
        this.tipo = tipo;
        logger.debug("Criado DTO com dados: nome={}, email={}, tipo={}", nome, email, tipo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            logger.error("Nome inválido");
            throw new UsuarioException("Nome é obrigatório");
        }

        if (nome.length() > MAX_NOME_LENGTH) {
            logger.error("Nome muito longo");
            throw new UsuarioException("Nome deve ter no máximo " + MAX_NOME_LENGTH + " caracteres");
        }

        this.nome = nome;
        logger.debug("Nome definido: {}", nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.error("Email inválido");
            throw new UsuarioException("Email é obrigatório");
        }

        if (email.length() > MAX_EMAIL_LENGTH) {
            logger.error("Email muito longo");
            throw new UsuarioException("Email deve ter no máximo " + MAX_EMAIL_LENGTH + " caracteres");
        }

        this.email = email;
        logger.debug("Email definido: {}", email);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (this.tipo == TipoUsuario.CLIENTE) {
            if (senha == null || senha.trim().isEmpty()) {
                logger.error("Senha obrigatória para cliente");
                throw new UsuarioException("Senha é obrigatória para cliente");
            }

            if (senha.length() < MIN_SENHA_LENGTH) {
                logger.error("Senha muito curta");
                throw new UsuarioException("A senha deve ter pelo menos " + MIN_SENHA_LENGTH + " caracteres");
            }
        }

        this.senha = senha;
        logger.debug("Senha definida");
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        if (this.tipo == TipoUsuario.CLIENTE) {
            if (confirmacaoSenha == null || confirmacaoSenha.trim().isEmpty()) {
                logger.error("Confirmação obrigatória para cliente");
                throw new UsuarioException("Confirmação de senha é obrigatória para cliente");
            }

            if (confirmacaoSenha.length() < MIN_SENHA_LENGTH) {
                logger.error("Confirmação muito curta");
                throw new UsuarioException("Confirmação deve ter pelo menos " + MIN_SENHA_LENGTH + " caracteres");
            }
        }

        this.confirmacaoSenha = confirmacaoSenha;
        logger.debug("Confirmação definida");
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        if (tipo == null) {
            logger.error("Tipo de usuário nulo");
            throw new UsuarioException("Tipo de usuário é obrigatório");
        }

        this.tipo = tipo;
        logger.debug("Tipo definido: {}", tipo);
    }
}
