package com.projeto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;
import com.projeto.enums.TipoUsuario;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome do usuário deve conter ao menos 2 caracteres")
    private String nome;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 64, message = "A senha deve ter ao menos 8 caracteres")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Set<Pedido> pedidos = new HashSet<>();

    // Getters e Setters
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
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }


}

