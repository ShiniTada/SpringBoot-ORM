package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.WrongIdException;
import com.epam.esm.exception.WrongInputDataException;

import java.util.List;

public interface TagService extends Service<TagDto> {

  /**
   * @param name - Tag name
   * @return {@link TagDto} entity
   */
  TagDto getByName(String name);

  /** @return the most popular tag of a User with the highest cost of all orders
   *
   * @param usersId - users id
   * @param limit - amount of tags
   * @return tagDtos
   */
  List<TagDto> getMostPopularTags(long usersId, int limit);

  /**
   * Get tags by gift certificate id.
   *
   * @param id - gift certificate id
   * @param sort - field by which the sorting will be. If sort is null sorting will be by id
   * @return tagDtos
   */
  List<TagDto> getTagsByGiftCertificateId(long id, String sort);

  /**
   * Create new tags, if they do not exist and exhausted tags with gift certificate.
   *
   * <p>If the tag has an id, tag will be exhausted and associated with the certificate. If the tag
   * with the specified id does not exist, an exception {@link WrongIdException} is thrown.
   *
   * <p>If the tag has a name, tag will be exhausted and associated with the certificate. If the tag
   * with the specified name does not exist, tag will be created and associated with the
   * certificate.
   *
   * <p>If tag has neither id nor name, an exception {@link WrongInputDataException} is thrown.
   *
   * @param giftCertificateDto which consist list of TagDto
   */
  void createTags(GiftCertificateDto giftCertificateDto);
}
