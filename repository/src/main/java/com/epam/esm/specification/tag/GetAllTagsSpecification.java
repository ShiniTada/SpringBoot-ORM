package com.epam.esm.specification.tag;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetAllTagsSpecification implements Specification<Tag> {

  private String sort;

  public GetAllTagsSpecification(String sort) {
    this.sort = sort;
  }

  @Override
  public CriteriaQuery<Tag> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
    Root<Tag> root = criteria.from(Tag.class);
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
