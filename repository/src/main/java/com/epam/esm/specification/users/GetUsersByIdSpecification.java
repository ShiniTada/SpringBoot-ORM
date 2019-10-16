package com.epam.esm.specification.users;

import com.epam.esm.entity.Users;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetUsersByIdSpecification implements Specification<Users> {

  private long id;

  public GetUsersByIdSpecification(long id) {
    this.id = id;
  }

  @Override
  public CriteriaQuery<Users> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<Users> criteria = builder.createQuery(Users.class);
    Root<Users> root = criteria.from(Users.class);
    criteria.where(builder.equal(root.get("id"), id));
    return criteria;
  }
}
