package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.math.BigDecimal;
import java.util.List;

public interface GiftCertificateService extends Service<GiftCertificateDto> {

  /**
   * Get gift certificates by users id.
   *
   * @param id - users id
   * @param sort - field by which the sorting will be. If sort is null sorting will be by id
   * @return giftCertificateDtos
   */
  List<GiftCertificateDto> getGiftCertificatesByUsersId(
      long id, String sort, int pageNumber, int maxResults);

  /**
   * Gives gift certificate dtos that match the criteria.
   *
   * @param giftCertificateDto - gift certificate dto which have various search criteria: name,
   *     description, tags
   * @param sort - field by which the sorting will be. If sort is null sorting will be by id
   * @return gift certificate dtos that match the criteria.
   */
  List<GiftCertificateDto> getGiftCertificates(
      GiftCertificateDto giftCertificateDto, String sort, int pageNumber, int maxResults);

  /**
   * Set gift certificate new price
   *
   * @param giftCertificateId - gift certificate id
   * @param price - new price of gift certificate
   * @return gift certificate with new price
   */
  GiftCertificateDto updatePrice(Long giftCertificateId, BigDecimal price);

  boolean isDeleted(Long id);
}
