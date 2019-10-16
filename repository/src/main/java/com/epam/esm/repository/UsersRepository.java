package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Orders;
import com.epam.esm.entity.Users;

import java.math.BigDecimal;
import java.util.List;

/** Implementation of {@link IRepository} for working with {@link Users} */
public interface UsersRepository extends IRepository<Users> {

  /**
   * Add gift certificate to users
   *
   * @param orders - new order
   * @param newUserBalance - new userBalance
   * @return  users orders
   */
  Orders addOrders(
      Orders orders, BigDecimal newUserBalance);

  /**
   * Get all users's orders
   *
   * @param usersId - users id
   * @param pageNumber - number or page
   * @param maxResults - how much elements should be shown on page
   * @return all users's orders
   */
  List<Orders> getAllOrdersByUsersId(Long usersId, int pageNumber, int maxResults);
}
