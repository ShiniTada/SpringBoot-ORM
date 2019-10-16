package com.epam.esm.specification.users;

import com.epam.esm.entity.Users;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetAllUsersSpecification implements Specification<Users> {

  private String sort;

  public GetAllUsersSpecification(String sort) {
    this.sort = sort;
  }

  @Override
  public CriteriaQuery<Users> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<Users> criteria = builder.createQuery(Users.class);
    Root<Users> root = criteria.from(Users.class);
    if (sort != null) {
      if (sort.equals("name")) {
        criteria.orderBy(builder.asc(root.get(sort)));
      }
    } else {
      criteria.orderBy(builder.asc(root.get("id")));
    }
    return criteria;
  }
}
