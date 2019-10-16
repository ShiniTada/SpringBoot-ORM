package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

  private static final String SORTING =
      "order by "
          + "case when ?2 ='name' then gc.name end asc, "
          + "case when ?2='description' then gc.description end asc, "
          + "case when ?2 ='last_modified' then TO_CHAR(gc.lastModified, 'YYYY-MM-DD') end desc, "
          + "case when ?2 ='creation_date' then TO_CHAR(gc.creationDate, 'YYYY-MM-DD') end desc, "
          + "gc.id asc ";

  private static final String GET_GIFT_CERTIFICATES_BY_TAG =
      "select gc "
          + "from GiftCertificate gc "
          + "join GiftCertificateToTag gctt on gc.id = gctt.giftCertificateId "
          + "join Tag t on t.id = gctt.tagId "
          + "where t.name = ?1 and gc.deleted = 'false' "
          + SORTING;

  private static final String GET_GIFT_CERTIFICATES_BY_USERS_ID =
      "select gc "
          + "from GiftCertificate gc join Orders o "
          + "on gc.id = o.giftCertificateId where o.toWhichUsersId = ?1 and gc.deleted = 'false' "
          + SORTING;

  @PersistenceContext private EntityManager em;

  @Override
  public GiftCertificate create(GiftCertificate giftCertificate) {
    try {
      giftCertificate.setCreationDate(LocalDate.now());
      giftCertificate.setLastModified(LocalDate.now());
      em.persist(giftCertificate);
      return giftCertificate;
    } catch (EntityExistsException e) {
      return null;
    }
  }

  @Override
  public GiftCertificate update(GiftCertificate giftCertificate) {
    GiftCertificate oldGiftCertificate =
        em.find(GiftCertificate.class, new Long(giftCertificate.getId()));
    if (oldGiftCertificate == null) {
      return null;
    }
    em.detach(oldGiftCertificate);
    oldGiftCertificate.setName(giftCertificate.getName());
    oldGiftCertificate.setDescription(giftCertificate.getDescription());
    oldGiftCertificate.setPrice(giftCertificate.getPrice());
    oldGiftCertificate.setDurationInDays(giftCertificate.getDurationInDays());
    oldGiftCertificate.setLastModified(LocalDate.now());
    em.merge(oldGiftCertificate);
    return oldGiftCertificate;
  }

  @Override
  public void delete(GiftCertificate giftCertificate) {
    GiftCertificate oldGiftCertificate =
        em.find(GiftCertificate.class, new Long(giftCertificate.getId()));
    em.detach(oldGiftCertificate);
    oldGiftCertificate.setDeleted(true);
    em.merge(oldGiftCertificate);
  }

  @Override
  public GiftCertificate getBySpecification(Specification specification) {
    try {
      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<GiftCertificate> criteriaQuery = specification.getCriteriaQuery(builder);
      GiftCertificate giftCertificate = em.createQuery(criteriaQuery).getSingleResult();
      return (giftCertificate.isDeleted()) ? null : giftCertificate;
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public List<GiftCertificate> getListBySpecification(
      Specification specification, int pageNumber, int maxResults) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<GiftCertificate> criteriaQuery = specification.getCriteriaQuery(builder);
    TypedQuery<GiftCertificate> typedQuery = em.createQuery(criteriaQuery);
    typedQuery.setFirstResult((pageNumber - 1) * maxResults);
    typedQuery.setMaxResults(maxResults);
    return typedQuery.getResultList();
  }

  @Override
  public List<GiftCertificate> getListByTag(Tag tag, String sort) {
    return em.createQuery(GET_GIFT_CERTIFICATES_BY_TAG)
        .setParameter(1, tag.getName())
        .setParameter(2, sort)
        .getResultList();
  }

  @Override
  public List<GiftCertificate> getListByPartNameOrDescription(
      GiftCertificate giftCertificate, String sort) {
    if (giftCertificate.getName() == null) {
      giftCertificate.setName("");
    }
    if (giftCertificate.getDescription() == null) {
      giftCertificate.setDescription("");
    }
    if (sort == null) {
      sort = "";
    }
    return em.createNamedStoredProcedureQuery("getGiftCertificate")
        .setParameter(2, giftCertificate.getName())
        .setParameter(3, giftCertificate.getDescription())
        .setParameter(4, sort)
        .getResultList();
  }

  @Override
  public List<GiftCertificate> getListByUsersId(
      long id, String sort, int pageNumber, int maxResults) {
    return em.createQuery(GET_GIFT_CERTIFICATES_BY_USERS_ID)
        .setParameter(1, id)
        .setParameter(2, sort)
        .setFirstResult((pageNumber - 1) * maxResults)
        .setMaxResults(maxResults)
        .getResultList();
  }
}
