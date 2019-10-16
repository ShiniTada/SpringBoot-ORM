package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.mapper.GiftCertificateModelMapper;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GiftCertificateServiceImplTest {

  @Mock GiftCertificateRepositoryImpl repository;
  @Mock GiftCertificateModelMapper mapper;
  @Mock TagServiceImpl tagService;
  @InjectMocks GiftCertificateServiceImpl service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAll_success() {
    // given
    List<GiftCertificate> entities = new ArrayList<>();
    entities.add(new GiftCertificate(1L, "1", null, new BigDecimal("23"), null, null, 2, false));
    entities.add(new GiftCertificate(2L, "1", null, new BigDecimal("23"), null, null, 2, false));
    entities.add(new GiftCertificate(3L, "1", null, new BigDecimal("23"), null, null, 2, false));
    when(repository.getListBySpecification(anyObject(), anyInt(), anyInt())).thenReturn(entities);
    List<GiftCertificateDto> expected = mapper.listToDto(entities);
    // when
    List<GiftCertificateDto> actual = service.getAll(null, 1, 3);
    // then
    verify(repository, times(1)).getListBySpecification(anyObject(), anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test
  public void getById_success() {
    // given
    GiftCertificate entity =
        new GiftCertificate(1L, "1", null, new BigDecimal("23"), null, null, 2, false);
    when(repository.getBySpecification(anyObject())).thenReturn(entity);

    GiftCertificateDto expected =
        new GiftCertificateDto(1L, "1", null, new BigDecimal("23"), null, null, 2);
    when(mapper.toDto(entity)).thenReturn(expected);

    List<TagDto> tags = new ArrayList<>();
    tags.add(new TagDto(1L, "test1"));
    tags.add(new TagDto(2L, "test2"));
    tags.add(new TagDto(3L, "test3"));
    when(tagService.getTagsByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(tags);
    expected.setListTagDto(tags);
    // when
    GiftCertificateDto actual = service.getById(1L);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    verify(tagService, times(1))
        .getTagsByGiftCertificateId(anyInt(), anyString());
    assertEquals(expected, actual);
  }

  @Test
  public void getGiftCertificatesByUsersId_success() {
    // given
    List<GiftCertificate> entities = new ArrayList<>();
    entities.add(new GiftCertificate(1L, "1", null, new BigDecimal("23"), null, null, 2, false));
    entities.add(new GiftCertificate(2L, "1", null, new BigDecimal("23"), null, null, 2, false));
    entities.add(new GiftCertificate(3L, "1", null, new BigDecimal("23"), null, null, 2, false));
    when(repository.getListByUsersId(anyLong(), anyString(), anyInt(), anyInt()))
        .thenReturn(entities);

    List<GiftCertificateDto> expected = new ArrayList<>();
    expected.add(new GiftCertificateDto(1L, "1", null, new BigDecimal("23"), null, null, 2));
    expected.add(new GiftCertificateDto(2L, "1", null, new BigDecimal("23"), null, null, 2));
    expected.add(new GiftCertificateDto(3L, "1", null, new BigDecimal("23"), null, null, 2));
    when(mapper.listToDto(entities)).thenReturn(expected);

    // when
    List<GiftCertificateDto> actual = service.getGiftCertificatesByUsersId(1L, null, 1, 3);
    // then
    verify(repository, times(1)).getListByUsersId(anyLong(), anyString(), anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test
  public void create_validParams_created() {
    // given
    GiftCertificate entity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.create(anyObject())).thenReturn(entity);

    GiftCertificateDto expected =
        new GiftCertificateDto(
            1L, "1", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2);
    when(mapper.toDto(entity)).thenReturn(expected);

    List<TagDto> tags = new ArrayList<>();
    tags.add(new TagDto(1L, "test1"));
    tags.add(new TagDto(2L, "test2"));
    tags.add(new TagDto(3L, "test3"));
    when(tagService.getTagsByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(tags);
    expected.setListTagDto(tags);
    // when
    GiftCertificateDto newGiftCertificate =
        new GiftCertificateDto(
            null, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2);
    GiftCertificateDto actual = service.create(newGiftCertificate);
    // then
    verify(repository, times(1)).create(anyObject());
    verify(tagService, times(1)).createTags(anyObject());
    verify(tagService, times(1))
        .getTagsByGiftCertificateId(anyLong(), anyString());
    assertEquals(expected, actual);
  }

  @Test(expected = WrongInputDataException.class)
  public void validateName_short_exception() {
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(null, "a", "1", new BigDecimal("23"), null, null, 2);
    service.create(giftCertificateDto);
  }

  @Test(expected = WrongInputDataException.class)
  public void validateName_null_exception() {
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(null, null, "1", new BigDecimal("23"), null, null, 2);
    service.create(giftCertificateDto);
  }

  @Test(expected = WrongInputDataException.class)
  public void validateDescription_long_exception() {
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(
            null,
            "name",
            "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
                + "wwwwwwwwwwwwwwwwwwwwwwewwwweeeeeeeeeeeeeeeeeeeeeeeeeeeeerr"
                + "rrrrrrrrrrrrrrrrttttttttttttttttttttttyyyyyyyyyyyyyyyyyyyy"
                + "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuyyyyyyyyyyyy"
                + "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu"
                + "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"
                + "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
                + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppp"
                + "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
                + "yyyyuuuuuuuuuuuuuuuuuuuuuuiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",
            new BigDecimal("23"),
            null,
            null,
            2);
    service.create(giftCertificateDto);
  }

  @Test(expected = WrongInputDataException.class)
  public void validatePrice_invalid_exception() {
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(null, "name", null, new BigDecimal("-23"), null, null, 2);
    service.create(giftCertificateDto);
  }

  @Test(expected = WrongInputDataException.class)
  public void validateDuration_exception() {
    GiftCertificateDto giftCertificateDto =
        new GiftCertificateDto(null, "name", null, new BigDecimal("23"), null, null, -2);
    service.create(giftCertificateDto);
  }

  @Test
  public void update_validParams_updated() {
    // given
    GiftCertificate oldEntity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.update(anyObject())).thenReturn(oldEntity);

    GiftCertificateDto expected =
        new GiftCertificateDto(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2);
    when(mapper.toDto(oldEntity)).thenReturn(expected);

    List<TagDto> tags = new ArrayList<>();
    tags.add(new TagDto(1L, "test1"));
    tags.add(new TagDto(2L, "test2"));
    when(tagService.getTagsByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(tags);
    expected.setListTagDto(tags);
    // when
    GiftCertificateDto newGiftCertificate =
        new GiftCertificateDto(null, "name", null, new BigDecimal("23"), null, null, 2);
    GiftCertificateDto actual = service.update(1L, newGiftCertificate);
    // then
    verify(repository, times(1)).update(anyObject());
    verify(tagService, times(1)).createTags(anyObject());
    verify(tagService, times(1))
        .getTagsByGiftCertificateId(anyLong(), anyString());
    assertEquals(expected, actual);
  }

  @Test
  public void updatePrice_validPrice_updated() {
    // given
    GiftCertificate oldEntity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.getBySpecification(anyObject())).thenReturn(oldEntity);

    GiftCertificate entity =
        new GiftCertificate(1L, "name", null, new BigDecimal("23"), null, null, 2, false);
    when(repository.update(anyObject())).thenReturn(entity);
    entity.setPrice(new BigDecimal("600"));
    when(repository.update(anyObject())).thenReturn(entity);

    GiftCertificateDto expected =
        new GiftCertificateDto(
            1L, "1", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2);
    when(mapper.toDto(entity)).thenReturn(expected);

    List<TagDto> tags = new ArrayList<>();
    tags.add(new TagDto(1L, "test1"));
    tags.add(new TagDto(2L, "test2"));
    when(tagService.getTagsByGiftCertificateId(anyLong(), anyString()))
        .thenReturn(tags);
    expected.setListTagDto(tags);
    // when
    GiftCertificateDto actual = service.updatePrice(1L, new BigDecimal("600"));
    // then
    verify(repository, times(1)).update(anyObject());
    verify(tagService, times(1))
        .getTagsByGiftCertificateId(anyLong(), anyString());
    assertEquals(expected, actual);
  }

  @Test(expected = WrongInputDataException.class)
  public void updatePrice_invalidPrice_exception() {
    // given
    GiftCertificate oldEntity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.getBySpecification(anyObject())).thenReturn(oldEntity);
    // when
    service.updatePrice(1L, new BigDecimal("-56.9"));
  }

  @Test(expected = WrongInputDataException.class)
  public void updatePrice_null_exception() {
    // given
    GiftCertificate oldEntity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.getBySpecification(anyObject())).thenReturn(oldEntity);
    // when
    service.updatePrice(1L, null);
  }

  @Test
  public void isDeleted_false() {
    // given
    GiftCertificate oldEntity =
        new GiftCertificate(
            1L, "name", null, new BigDecimal("23"), LocalDate.now(), LocalDate.now(), 2, false);
    when(repository.getBySpecification(anyObject())).thenReturn(oldEntity);
    // when
    boolean actual = service.isDeleted(1L);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    assertFalse(actual);
  }

  @Test
  public void deleteTag_deleted() {
    service.deleteById(1L);
    GiftCertificate giftCertificate = new GiftCertificate(1L);
    verify(repository, times(1)).delete(giftCertificate);
  }
}
