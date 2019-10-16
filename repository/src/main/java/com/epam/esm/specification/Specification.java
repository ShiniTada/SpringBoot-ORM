package com.epam.esm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface Specification<T> {

  /** @return CriteriaBuilder */
  CriteriaQuery<T> getCriteriaQuery(CriteriaBuilder builder);
}
