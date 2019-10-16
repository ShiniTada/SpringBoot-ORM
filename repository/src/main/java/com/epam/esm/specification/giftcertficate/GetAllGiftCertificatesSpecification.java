package com.epam.esm.specification.giftcertficate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetAllGiftCertificatesSpecification implements Specification<GiftCertificate> {

  private String sort;

  public GetAllGiftCertificatesSpecification(String sort) {
    this.sort = sort;
  }

  @Override
  public CriteriaQuery<GiftCertificate> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<GiftCertificate> criteria = builder.createQuery(GiftCertificate.class);
    Root<GiftCertificate> root = criteria.from(GiftCertificate.class);
    criteria.where(builder.equal(root.get("deleted"), false));
    if (sort != null) {
      if (sort.equals("name")
          || sort.equals("description")
          || sort.equals("last_modified")
          || sort.equals("creation_date")) {
        criteria.orderBy(builder.asc(root.get(sort)));
      }
    } else {
      criteria.orderBy(builder.asc(root.get("id")));
    }
    return criteria;
  }
}
