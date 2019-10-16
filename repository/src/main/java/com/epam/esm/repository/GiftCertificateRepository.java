package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

/** Implementation of {@link IRepository} for working with {@link GiftCertificate} */
public interface GiftCertificateRepository extends IRepository<GiftCertificate> {

  /**
   * Get list of gift certificates by tag name
   *
   * @param tag - tag which have name field
   * @param sort - sorting param
   * @return list of gift certificates
   */
  List<GiftCertificate> getListByTag(Tag tag, String sort);

  /**
   * Get list of gift certificates by part of gift certificate name or description
   *
   * @param giftCertificate - gift certificate which have name or(and) description fields
   * @param sort - sorting param
   * @return list of gift certificates
   */
  List<GiftCertificate> getListByPartNameOrDescription(
      GiftCertificate giftCertificate, String sort);

  /**
   * Get list of gift certificates by user id
   *
   * @param id - user id
   * @param sort - sorting param
   * @param pageNumber - number or page
   * @param maxResults - how much elements should be shown on page
   * @return list of tags
   */
  List<GiftCertificate> getListByUsersId(long id, String sort, int pageNumber, int maxResults);
}
