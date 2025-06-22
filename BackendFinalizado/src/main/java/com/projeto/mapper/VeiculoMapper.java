
package com.projeto.mapper;

import com.projeto.dto.VeiculoDTO;
import com.projeto.model.Veiculo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring") 
public interface VeiculoMapper {
    
    

    Veiculo toVeiculo(VeiculoDTO veiculoDTO);

    VeiculoDTO toVeiculoDTO(Veiculo veiculo);
}