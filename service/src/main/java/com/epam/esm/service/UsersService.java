package com.epam.esm.service;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.entity.Orders;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends Service<UsersDto>, UserDetailsService {

  /**
   * @param name - Users name
   * @return {@link UsersDto} entity
   */
  UsersDto getByName(String name);

  /**
   * Add gift certificate to users
   *
   * @param giftCertificateId - gift certificate id
   * @param toUsersId - users id
   * @param fromUsersId - users id
   * @return {@link OrdersDto} entity
   */
  OrdersDto addOrders(Long giftCertificateId, Long toUsersId, Long fromUsersId);

  /**
   * Get all users's orders
   *
   * @param usersId - users id
   * @param pageNumber - number or page
   * @param maxResults - how much elements should be shown on page
   * @return users orders
   */
  List<OrdersDto> getAllOrdersByUsersId(Long usersId, int pageNumber, int maxResults);
}
