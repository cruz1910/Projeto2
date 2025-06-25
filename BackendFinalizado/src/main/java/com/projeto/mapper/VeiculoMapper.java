package com.projeto.mapper;

import com.projeto.dto.VeiculoDTO;
import com.projeto.model.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface VeiculoMapper {

    @Mappings({
        @Mapping(target = "marca", source = "marca"),
        @Mapping(target = "modelo", source = "modelo"),
        @Mapping(target = "cor", source = "cor"),
        @Mapping(target = "ano", source = "ano"),
        @Mapping(target = "descricao", source = "descricao"),
        @Mapping(target = "preco", source = "preco"),
        @Mapping(target = "imagem", source = "imagem"),
        @Mapping(target = "disponivel", constant = "true"),
        @Mapping(target = "dataCadastro", expression = "java(java.time.LocalDateTime.now().toString())")
    })
    Veiculo toVeiculo(VeiculoDTO veiculoDTO);

    @Mappings({
        @Mapping(target = "marca", source = "marca"),
        @Mapping(target = "modelo", source = "modelo"),
        @Mapping(target = "cor", source = "cor"),
        @Mapping(target = "ano", source = "ano"),
        @Mapping(target = "descricao", source = "descricao"),
        @Mapping(target = "preco", source = "preco"),
        @Mapping(target = "imagem", source = "imagem")
    })
    VeiculoDTO toVeiculoDTO(Veiculo veiculo);

    default String getNullSafeString(String value) {
        return value != null ? value : "";
    }

    default Integer getNullSafeInteger(Integer value) {
        return value != null ? value : 0;
    }

    default BigDecimal getNullSafeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}