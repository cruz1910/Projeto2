package com.projeto.mapper;

import com.projeto.dto.ItemPedidoDTO;
import com.projeto.dto.PedidoDTO;
import com.projeto.model.ItemPedido;
import com.projeto.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "dataPedido", source = "dataPedido")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "valorTotal", source = "valorTotal")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "toItemPedidoList")
    Pedido toPedido(PedidoDTO pedidoDTO);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "dataPedido", source = "dataPedido")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "valorTotal", source = "valorTotal")
    @Mapping(target = "itens", source = "itens", qualifiedByName = "toItemPedidoDTOList")
    PedidoDTO toPedidoDTO(Pedido pedido);

    @Mapping(target = "veiculo", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    ItemPedido toItemPedido(ItemPedidoDTO itemDTO);

    @Mapping(target = "veiculoId", source = "veiculo.id")
    ItemPedidoDTO toItemPedidoDTO(ItemPedido item);

    @Named("toItemPedidoList")
    default Set<ItemPedido> toItemPedidoList(Set<ItemPedidoDTO> itemDTOs) {
        if (itemDTOs == null) {
            return null;
        }
        return itemDTOs.stream()
                .map(this::toItemPedido)
                .collect(Collectors.toSet());
    }

    @Named("toItemPedidoDTOList")
    default Set<ItemPedidoDTO> toItemPedidoDTOList(Set<ItemPedido> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(this::toItemPedidoDTO)
                .collect(Collectors.toSet());
    }
}
