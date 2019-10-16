package com.epam.esm.mapper;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.entity.Orders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersModelMapper {

    @Autowired
    private ModelMapper mapper;

    public Orders toEntity(OrdersDto dto) {
        return (dto == null) ? null : mapper.map(dto, Orders.class);
    }

    public OrdersDto toDto(Orders entity) {
        return (entity == null)
                ? null
                : new OrdersDto(
                entity.getId(),
                entity.getToWhichUsersId(),
                entity.getGiftCertificateId(),
                entity.getCost(),
                entity.getTimestamp());
    }

    public List<Orders> listToEntity(List<OrdersDto> ordersDto) {
        return ordersDto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<OrdersDto> listToDto(List<Orders> orders) {
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }
}
