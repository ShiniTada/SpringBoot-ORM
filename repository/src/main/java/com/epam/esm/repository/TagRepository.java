package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;

/** Implementation of {@link IRepository} for working with {@link Tag} */
public interface TagRepository extends IRepository<Tag> {

  /**
   * Add tag to gift certificate
   *
   * @param giftCertificateId - gift certificate id
   * @param tagId - tag id
   */
  void addTagToGiftCertificate(long giftCertificateId, long tagId);

  /**
   * Delete tag from gift certificate
   *
   * @param giftCertificateId - gift certificate id
   * @param tagId - tag id
   */
  void deleteTagFromGiftCertificate(long giftCertificateId, long tagId);

  /**
   * Get list of tags by certificate id
   *
   * @param id - gift certificate id
   * @param sort - sorting param
   * @return list of tags
   */
  List<Tag> getListByGiftCertificateId(long id, String sort);

  /** @return the most popular tag of a User with the highest cost of all orders
   *
   * @param usersId - users id
   * @param limit - amount of tags
   * @return tags
   */
  List<Tag> getMostPopularTags(long usersId, int limit);

  /**
   * Check tag existing in gift certificate
   *
   * @param giftCertificateId - gift certificate id
   * @param tagId - tag id
   * @return {@code true} if tag exist in gift certificate, otherwise {@code false}
   */
  boolean isTagAddedToGiftCertificate(long tagId, long giftCertificateId);
}
