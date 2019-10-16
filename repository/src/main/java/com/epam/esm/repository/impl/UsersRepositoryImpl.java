package com.epam.esm.repository.impl;

import com.epam.esm.entity.Orders;
import com.epam.esm.entity.Users;
import com.epam.esm.repository.UsersRepository;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

  private static final String DELETE_ORDERS = "delete from Orders WHERE usersId = :id";

  @PersistenceContext private EntityManager em;

  @Override
  public Users create(Users users) {
    try {
      em.persist(users);
      return users;
    } catch (EntityExistsException e) {
      return null;
    }
  }

  @Override
  public Users update(Users users) {
    Users oldUsers = em.find(Users.class, new Long(users.getId()));
    if (oldUsers == null) {
      return null;
    }
    em.detach(oldUsers);
    oldUsers.setName(users.getName());
    oldUsers.setPassword(users.getPassword());
    oldUsers.setUsersBalance(users.getUsersBalance());
    em.merge(oldUsers);
    return oldUsers;
  }

  @Override
  public void delete(Users users) {

    em.createQuery(DELETE_ORDERS).setParameter("id", users.getId()).executeUpdate();
    Users oldUsers = em.find(Users.class, new Long(users.getId()));
    em.remove(oldUsers);
  }

  @Override
  public Users getBySpecification(Specification specification) {
    try {
      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<Users> criteriaQuery = specification.getCriteriaQuery(builder);
      return em.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public List<Users> getListBySpecification(
      Specification specification, int pageNumber, int maxResults) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Users> criteriaQuery = specification.getCriteriaQuery(builder);
    TypedQuery<Users> typedQuery = em.createQuery(criteriaQuery);
    typedQuery.setFirstResult((pageNumber - 1) * maxResults);
    typedQuery.setMaxResults(maxResults);
    return typedQuery.getResultList();
  }

  @Override
  public Orders addOrders(Orders orders, BigDecimal newUserBalance) {
    orders.setTimestamp(LocalDate.now());
    em.persist(orders);

    Users fromUsers = em.find(Users.class, new Long(orders.getUsersId()));
    em.detach(fromUsers);
    fromUsers.setUsersBalance(newUserBalance);
    em.merge(fromUsers);
    return orders;
  }

  @Override
  public List<Orders> getAllOrdersByUsersId(Long usersId, int pageNumber, int maxResults) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Orders> criteriaQuery = builder.createQuery(Orders.class);
    Root<Orders> root = criteriaQuery.from(Orders.class);
    criteriaQuery.where(builder.equal(root.get("usersId"), usersId));
    TypedQuery<Orders> typedQuery = em.createQuery(criteriaQuery);
    typedQuery.setFirstResult((pageNumber - 1) * maxResults);
    typedQuery.setMaxResults(maxResults);
    return typedQuery.getResultList();
  }
}
