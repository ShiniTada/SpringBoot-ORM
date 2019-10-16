package com.epam.esm.dto.list;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.entity.Orders;

import java.util.List;

public class OrdersListDto {
	
	private List<OrdersDto> orders;

	public OrdersListDto(List<OrdersDto> orders) {
		this.orders = orders;
	}

	public List<OrdersDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersDto> orders) {
		this.orders = orders;
	}

}
