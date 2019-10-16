package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.mapper.TagModelMapper;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagServiceImplTest {

  @Mock TagModelMapper mapper;
  @Mock TagRepositoryImpl repository;
  @InjectMocks TagServiceImpl service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAll_success() {
    // given
    List<Tag> entities = new ArrayList<>();
    entities.add(new Tag(1L, "test1"));
    entities.add(new Tag(2L, "test2"));
    entities.add(new Tag(3L, "test3"));
    when(repository.getListBySpecification(anyObject(), anyInt(), anyInt())).thenReturn(entities);
    List<TagDto> expected = mapper.listToDto(entities);
    // when
    List<TagDto> actual = service.getAll(null, 1, 3);
    // then
    verify(repository, times(1)).getListBySpecification(anyObject(), anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test
  public void getById_success() {
    // given
    Tag entity = new Tag(1L, "test1");
    when(repository.getBySpecification(anyObject())).thenReturn(entity);
    TagDto expected = mapper.toDto(entity);
    // when
    TagDto actual = service.getById(1L);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void getByName_success() {
    // given
    Tag entity = new Tag(1L, "test1");
    when(repository.getBySpecification(anyObject())).thenReturn(entity);
    TagDto expected = mapper.toDto(entity);
    // when
    TagDto actual = service.getByName("test1");
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void getMostPopularTags_success() {
    // given
    List<Tag> entities = new ArrayList<>();
    entities.add(new Tag(1L, "test1"));
    entities.add(new Tag(2L, "test2"));
    entities.add(new Tag(3L, "test3"));
    when(repository.getMostPopularTags(anyInt(), anyInt())).thenReturn(entities);
    List<TagDto> expected = mapper.listToDto(entities);
    // when
    List<TagDto> actual = service.getMostPopularTags(1, 3);
    // then
    verify(repository, times(1)).getMostPopularTags(anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test
  public void getTagsByGiftCertificateId_success() {
    // given
    List<Tag> entities = new ArrayList<>();
    entities.add(new Tag(1L, "test1"));
    entities.add(new Tag(2L, "test2"));
    entities.add(new Tag(3L, "test3"));
    when(repository.getListByGiftCertificateId(anyInt(), anyString()))
        .thenReturn(entities);
    List<TagDto> expected = mapper.listToDto(entities);
    // when
    List<TagDto> actual = service.getTagsByGiftCertificateId(1, null);
    // then
    verify(repository, times(1))
        .getListByGiftCertificateId(anyInt(), anyString());
    assertEquals(expected, actual);
  }

  @Test
  public void createTag_allParamsValid_created() {
    // given
    Tag expectedTag = new Tag(1L, "best");
    when(repository.create(anyObject())).thenReturn(expectedTag);
    TagDto expected = mapper.toDto(expectedTag);
    // when
    TagDto newTag = new TagDto(null, "best");
    TagDto actual = service.create(newTag);
    // then
    verify(repository, times(1)).create(anyObject());
    assertEquals(expected, actual);
  }

  @Test(expected = WrongInputDataException.class)
  public void createTag_ParamsInvalid_created_exception() {
    TagDto tagDto = new TagDto(null, "b");
    service.create(tagDto);
  }

  @Test
  public void deleteTag_deleted() {
    // when
    service.deleteById(1L);
    // then
    verify(repository, times(1)).delete(anyObject());
  }

  @Test
  public void createTags_NewGiftCertificateDoesNotHaveAnyTags_deleted() {
    // given
    List<Tag> oldTags = new ArrayList<>();
    oldTags.add(new Tag(1L, "test1"));
    oldTags.add(new Tag(2L, "test2"));
    when(repository.getListByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(oldTags);

    Tag oldOutTagOne = new Tag(1L, "test1");
    Tag oldOutTagTwo = new Tag(2L, "test2");
    when(repository.getBySpecification(anyObject())).thenReturn(oldOutTagOne);
    when(repository.getBySpecification(anyObject())).thenReturn(oldOutTagTwo);
    // when
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(1L, "name", null, new BigDecimal("2.3"), null, null, 2);
    service.createTags(giftCertificateDto);
    // then
    verify(repository, times(1))
        .getListByGiftCertificateId(anyLong(), anyString());
    verify(repository, times(2)).deleteTagFromGiftCertificate(anyLong(), anyLong());
  }

  @Test
  public void createTags_NewGiftCertificateHaveNewTags_deleted() {
    // given
    List<Tag> oldTags = new ArrayList<>();
    oldTags.add(new Tag(1L, "test1"));
    oldTags.add(new Tag(2L, "test2"));
    when(repository.getListByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(oldTags);

    List<Tag> expectedNewTags = new ArrayList<>();
    expectedNewTags.add(new Tag(1L, "test1"));
    expectedNewTags.add(new Tag(3L, "test3"));
    expectedNewTags.add(new Tag(4L, "test4"));
    when(mapper.listToEntity(anyListOf(TagDto.class))).thenReturn(expectedNewTags);

    Tag tagThree = new Tag(3L, "test3");
    when(mapper.toDto(anyObject())).thenReturn(new TagDto(3L, "test3"));
    when(repository.getBySpecification(anyObject())).thenReturn(tagThree);
    Tag tagFour = new Tag(4L, "test4");
    when(mapper.toDto(anyObject())).thenReturn(new TagDto(4L, "test4"));
    when(repository.getBySpecification(anyObject())).thenReturn(tagFour);
    // when
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(1L, "name", null, new BigDecimal("2.3"), null, null, 2);
    giftCertificateDto.setListTagDto(mapper.listToDto(expectedNewTags));
    service.createTags(giftCertificateDto);
    // then
    int shouldBeRemove = 1;
    int shouldBeAdded = 2;
    verify(repository, times(1))
        .getListByGiftCertificateId(anyLong(), anyString());
    verify(repository, times(shouldBeRemove)).deleteTagFromGiftCertificate(anyLong(), anyLong());
    verify(repository, times(shouldBeAdded)).addTagToGiftCertificate(anyLong(), anyLong());
  }
}
