package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WrongIdException;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.mapper.TagModelMapper;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.tag.GetAllTagsSpecification;
import com.epam.esm.specification.tag.GetTagByIdSpecification;
import com.epam.esm.specification.tag.GetTagByNameSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class TagServiceImpl implements TagService {

  private static final int MIN_NAME_LENGTH = 2;
  private static final int MAX_NAME_LENGTH = 30;

  @Autowired private TagRepository tagRepository;

  @Autowired private TagModelMapper tagModelMapper;

  @Override
  public List<TagDto> getAll(String sort, int pageNumber, int maxResults) {
    List<Tag> tags =
        tagRepository.getListBySpecification(
            new GetAllTagsSpecification(sort), pageNumber, maxResults);
    return tagModelMapper.listToDto(tags);
  }

  @Override
  public TagDto getById(long id) {
    Tag tag = tagRepository.getBySpecification(new GetTagByIdSpecification(id));
    return tagModelMapper.toDto(tag);
  }

  @Override
  public TagDto getByName(String name) {
    Tag tag = tagRepository.getBySpecification(new GetTagByNameSpecification(name));
    return tagModelMapper.toDto(tag);
  }

  @Override
  public List<TagDto> getMostPopularTags(long usersId, int limit) {
    List<Tag> tags = tagRepository.getMostPopularTags(usersId, limit);
    return tagModelMapper.listToDto(tags);
  }

  @Override
  public List<TagDto> getTagsByGiftCertificateId(long id, String sort) {
    List<Tag> tags = tagRepository.getListByGiftCertificateId(id, sort);
    return tagModelMapper.listToDto(tags);
  }

  @Override
  public TagDto create(TagDto tagDto) {
    validate(tagDto);
    Tag tag = tagModelMapper.toEntity(tagDto);
    Tag outTag = tagRepository.create(tag);
    if (outTag == null) {
      throw new ServiceException("Server error. Tag not added.");
    }
    return tagModelMapper.toDto(outTag);
  }

  @Override
  public TagDto update(long id, TagDto tagDto) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createTags(GiftCertificateDto giftCertificateDto) {
    List<Tag> oldTags = tagRepository.getListByGiftCertificateId(giftCertificateDto.getId(), null);
    if (giftCertificateDto.getListTagDto() == null) {
      deleteTagsFromGiftCertificate(oldTags, giftCertificateDto.getId());
    } else {
      List<Tag> newTags = tagModelMapper.listToEntity(giftCertificateDto.getListTagDto());

      List<Tag> tagsShouldBeRemove = new ArrayList<>(oldTags);
      tagsShouldBeRemove.removeAll(newTags);
      deleteTagsFromGiftCertificate(tagsShouldBeRemove, giftCertificateDto.getId());

      List<Tag> tagsShouldBeAdded = new ArrayList<>(newTags);
      tagsShouldBeAdded.removeAll(oldTags);
      addNewTagsToGiftCertificate(tagsShouldBeAdded, giftCertificateDto.getId());
    }
  }

  private void deleteTagsFromGiftCertificate(List<Tag> tagsShouldBeRemove, Long giftCertificateId) {
    for (Tag tag : tagsShouldBeRemove) {
      Tag oldOutTag;
      if (tag.getId() != null) {
        oldOutTag = tagRepository.getBySpecification(new GetTagByIdSpecification(tag.getId()));
      } else {
        oldOutTag = tagRepository.getBySpecification(new GetTagByNameSpecification(tag.getName()));
      }
      tagRepository.deleteTagFromGiftCertificate(giftCertificateId, oldOutTag.getId());
    }
  }

  private void addNewTagsToGiftCertificate(List<Tag> tagsShouldBeAdded, Long giftCertificateId) {
    for (Tag tag : tagsShouldBeAdded) {
      validate(tagModelMapper.toDto(tag));
      Tag outTag;
      if (tag.getId() != null) {
        outTag = tagRepository.getBySpecification(new GetTagByIdSpecification(tag.getId()));
        if (outTag == null) {
          throw new WrongIdException("Tag with id " + tag.getId() + " does not exist.");
        }
      } else {
        outTag = tagRepository.getBySpecification(new GetTagByNameSpecification(tag.getName()));
        if (outTag == null) {
          outTag = tagRepository.create(tag);
        }
      }
      boolean isAdded =
          tagRepository.isTagAddedToGiftCertificate(outTag.getId(), giftCertificateId);
      if (!isAdded) {
        tagRepository.addTagToGiftCertificate(giftCertificateId, outTag.getId());
      }
    }
  }

  private void validate(TagDto tagDto) {
    List<String> exceptionMessages = new ArrayList<>();
    if (tagDto.getId() == null && tagDto.getName() == null) {
      exceptionMessages.add("Tag must contain id or name.");
    }
    if (tagDto.getName() != null) {
      if (tagDto.getName().length() < MIN_NAME_LENGTH
          || tagDto.getName().length() > MAX_NAME_LENGTH) {
        exceptionMessages.add("Name must be between 3 and 100 characters.");
      }
    }
    if (!exceptionMessages.isEmpty()) {
      throw new WrongInputDataException(exceptionMessages);
    }
  }

  @Override
  public void deleteById(long id) {
    tagRepository.delete(new Tag(id));
  }
}
