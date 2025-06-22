package com.projeto.dto;

import com.projeto.model.Veiculo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class VeiculoDTO {
    private Long id;

    @NotBlank(message = "Marca é obrigatória")
    @Size(min = 2, max = 50, message = "Marca deve ter entre 2 e 50 caracteres")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(min = 2, max = 50, message = "Modelo deve ter entre 2 e 50 caracteres")
    private String modelo;

    @NotBlank(message = "Cor é obrigatória")
    @Size(min = 2, max = 50, message = "Cor deve ter entre 2 e 50 caracteres")
    private String cor;

    @NotNull(message = "Ano é obrigatório")
    private Integer ano;

    @Size(max = 1000, message = "Descrição muito longa")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que 0")
    private BigDecimal preco;

    private String dataCadastro;

    @Size(max = 1000, message = "URL da imagem muito longa")
    private String imagem;

    public VeiculoDTO() {

    }

    public VeiculoDTO(Veiculo veiculo) {
        this.id = veiculo.getId();
        this.marca = veiculo.getMarca();
        this.modelo = veiculo.getModelo();
        this.cor = veiculo.getCor();
        this.ano = veiculo.getAno();
        this.descricao = veiculo.getDescricao();
        this.preco = veiculo.getPreco();
        this.dataCadastro = veiculo.getDataCadastro();
        this.imagem = veiculo.getImagem();
    }

    public Veiculo toVeiculo() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(this.id);
        veiculo.setMarca(this.marca);
        veiculo.setModelo(this.modelo);
        veiculo.setCor(this.cor);
        veiculo.setAno(this.ano);
        veiculo.setDescricao(this.descricao);
        veiculo.setPreco(this.preco);
        veiculo.setDataCadastro(this.dataCadastro);
        veiculo.setImagem(this.imagem);
        return veiculo;
    }

    // Getters e Setters
    public Long getId() { 
        return id; }
    public void setId(Long id) { 
        this.id = id; }
    public String getMarca() { 
        return marca; }
    public void setMarca(String marca) { 
        this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { 
        this.modelo = modelo; }
    public String getCor() {
         return cor; }
    public void setCor(String cor) { 
        this.cor = cor; }
    public Integer getAno() {
         return ano; }
    public void setAno(Integer ano) {
         this.ano = ano; }
    public String getDescricao() { 
        return descricao; }
    public void setDescricao(String descricao) {
         this.descricao = descricao; }
    public BigDecimal getPreco() { 
        return preco; }
    public void setPreco(BigDecimal preco) { 
        this.preco = preco; }
    public String getDataCadastro() {
         return dataCadastro; }
    public void setDataCadastro(String dataCadastro) {
         this.dataCadastro = dataCadastro; }
    public String getImagem() {
         return imagem; }
    public void setImagem(String imagem) { 
        this.imagem = imagem; }
}
