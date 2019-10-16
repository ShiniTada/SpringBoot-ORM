package com.epam.esm.specification.giftcertficate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class GetGiftCertificateByIdSpecification implements Specification<GiftCertificate> {

  private long id;

  public GetGiftCertificateByIdSpecification(long id) {
    this.id = id;
  }

  @Override
  public CriteriaQuery<GiftCertificate> getCriteriaQuery(CriteriaBuilder builder) {
    CriteriaQuery<GiftCertificate> criteria = builder.createQuery(GiftCertificate.class);
    Root<GiftCertificate> root = criteria.from(GiftCertificate.class);
    criteria.where(builder.equal(root.get("id"), id), builder.equal(root.get("deleted"), false));
    return criteria;
  }
}
