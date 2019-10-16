package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificateToTag;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
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
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

  private static final String GET_TAG_BY_GIFT_CERTIFICATE_ID =
      "select t from Tag t join GiftCertificateToTag gctt"
          + " on t.id = gctt.tagId where gctt.giftCertificateId = ?1 "
          + "order by case when ?2 ='name' then t.name end asc, t.id asc ";

  private static final String GET_MOST_POPULAR_TAG =
      "select t from Tag t "
          + "join GiftCertificateToTag gctt on gctt.tagId = t.id  "
          + "join Orders o on o.giftCertificateId = gctt.giftCertificateId "
          + "where o.usersId = ?1 "
          + "group by t.id "
          + "order by sum(o.cost) desc ";

  private static final String DELETE_TAG_FROM_GIFT_CERTIFICATE =
      "delete from GiftCertificateToTag where giftCertificateId = :giftCertificateId and tagId = :tagId";

  private static final String DELETE_TAG_FROM_GIFT_CERTIFICATES =
      "delete from GiftCertificateToTag where tagId = :tagId";

  private static final String TAG_ID = "tagId";
  private static final String GIFT_CERTIFICATE_ID = "giftCertificateId";

  @PersistenceContext private EntityManager em;

  @Override
  public Tag create(Tag tag) {
    try {
      em.persist(tag);
      return tag;
    } catch (EntityExistsException e) {
      return null;
    }
  }

  @Override
  public Tag update(Tag tag) {
    Tag oldTag = em.find(Tag.class, new Long(tag.getId()));
    if (oldTag == null) {
      return null;
    }
    em.detach(oldTag);
    oldTag.setName(tag.getName());
    em.merge(oldTag);
    return oldTag;
  }

  @Override
  public void addTagToGiftCertificate(long giftCertificateId, long tagId) {
    em.persist(new GiftCertificateToTag(giftCertificateId, tagId));
  }

  @Override
  public void deleteTagFromGiftCertificate(long giftCertificateId, long tagId) {
    em.createQuery(DELETE_TAG_FROM_GIFT_CERTIFICATE)
        .setParameter(GIFT_CERTIFICATE_ID, giftCertificateId)
        .setParameter(TAG_ID, tagId)
        .executeUpdate();
  }

  @Override
  public void delete(Tag tag) {
    em.createQuery(DELETE_TAG_FROM_GIFT_CERTIFICATES)
        .setParameter(TAG_ID, tag.getId())
        .executeUpdate();
    Tag oldTag = em.find(Tag.class, new Long(tag.getId()));
    em.remove(oldTag);
  }

  @Override
  public Tag getBySpecification(Specification specification) {
    try {
      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<Tag> criteriaQuery = specification.getCriteriaQuery(builder);
      return em.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public List<Tag> getListBySpecification(
      Specification specification, int pageNumber, int maxResults) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = specification.getCriteriaQuery(builder);
    TypedQuery<Tag> typedQuery = em.createQuery(criteriaQuery);
    typedQuery.setFirstResult((pageNumber - 1) * maxResults);
    typedQuery.setMaxResults(maxResults);
    return typedQuery.getResultList();
  }

  @Override
  public List<Tag> getListByGiftCertificateId(long id, String sort) {
    return em.createQuery(GET_TAG_BY_GIFT_CERTIFICATE_ID)
        .setParameter(1, id)
        .setParameter(2, sort)
        .getResultList();
  }

  @Override
  public List<Tag> getMostPopularTags(long usersId, int limit) {
    return em.createQuery(GET_MOST_POPULAR_TAG)
        .setParameter(1, usersId)
        .setMaxResults(limit)
        .getResultList();
  }

  @Override
  public boolean isTagAddedToGiftCertificate(long tagId, long giftCertificateId) {
    try {
      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<GiftCertificateToTag> criteria =
          builder.createQuery(GiftCertificateToTag.class);
      Root<GiftCertificateToTag> root = criteria.from(GiftCertificateToTag.class);
      criteria.where(
          builder.equal(root.get(TAG_ID), tagId),
          builder.equal(root.get(GIFT_CERTIFICATE_ID), giftCertificateId));
      em.createQuery(criteria).getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }
}
