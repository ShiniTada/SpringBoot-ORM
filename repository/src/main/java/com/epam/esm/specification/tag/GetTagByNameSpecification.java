package com.epam.esm.specification.tag;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetTagByNameSpecification implements Specification<Tag> {

  private String name;

  public GetTagByNameSpecification(String name) {
    this.name = name;
  }

  @Override
  public CriteriaQuery<Tag> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
    Root<Tag> root = criteria.from(Tag.class);
    criteria.where(builder.equal(root.get("name"), name));
    return criteria;
  }
}
