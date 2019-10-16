package com.epam.esm.service.impl;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Orders;
import com.epam.esm.entity.RoleEnum;
import com.epam.esm.entity.Users;
import com.epam.esm.exception.NotEnoughMoneyException;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.mapper.OrdersModelMapper;
import com.epam.esm.mapper.UsersModelMapper;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.impl.UsersRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersServiceImplTest {

  @Mock UsersRepositoryImpl repository;
  @Mock UsersModelMapper mapper;
  @Mock PasswordEncoder passwordEncoder;
  @Mock GiftCertificateRepositoryImpl giftCertificateRepository;
  @Mock OrdersModelMapper ordersMapper;
  @InjectMocks UsersServiceImpl service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAll_success() {
    // given
    List<Users> entities = new ArrayList<>();
    entities.add(new Users(1L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23")));
    entities.add(new Users(2L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23")));
    entities.add(new Users(3L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23")));

    when(repository.getListBySpecification(anyObject(), anyInt(), anyInt())).thenReturn(entities);
    List<UsersDto> expected = mapper.listToDto(entities);
    // when
    List<UsersDto> actual = service.getAll(null, 1, 3);
    // then
    verify(repository, times(1)).getListBySpecification(anyObject(), anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test
  public void getById_success() {
    // given
    Users entity = new Users(1L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23"));
    when(repository.getBySpecification(anyObject())).thenReturn(entity);
    UsersDto expected = mapper.toDto(entity);
    // when
    UsersDto actual = service.getById(1L);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void getByName_success() {
    // given
    Users entity = new Users(1L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23"));
    when(repository.getBySpecification(anyObject())).thenReturn(entity);
    UsersDto expected = mapper.toDto(entity);
    // when
    UsersDto actual = service.getByName("name");
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void createUsers_allParamsValid_created() {
    // given
    Users expectedUsers =
        new Users(1L, "name", "password", RoleEnum.ROLE_USER, new BigDecimal("23"));
    when(mapper.toEntity(anyObject())).thenReturn(expectedUsers);
    when(repository.create(anyObject())).thenReturn(expectedUsers);
    UsersDto expected = mapper.toDto(expectedUsers);
    // when
    UsersDto newUsers = new UsersDto(null, "name", "password", new BigDecimal("23"));
    UsersDto actual = service.create(newUsers);
    // then
    verify(passwordEncoder, times(1)).encode(anyString());
    verify(repository, times(1)).create(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void updateUsers_allParamsValid_updated() {
    // given
    Users oldUsers = new Users(1L, "name", "password", new BigDecimal("23"));
    when(repository.getBySpecification(anyObject())).thenReturn(oldUsers);

      Users newUsers = new Users(1L, null, null, new BigDecimal("600"));
    when(mapper.toEntity(anyObject())).thenReturn(newUsers);

    Users expectedEntity = new Users(1L, "name", "password", new BigDecimal("600"));
    when(repository.update(anyObject())).thenReturn(expectedEntity);
    UsersDto expected = mapper.toDto(expectedEntity);
    // when
    UsersDto newActualUsers = new UsersDto(null, null, null, new BigDecimal("600"));
    UsersDto actual = service.update(1L, newActualUsers);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    verify(repository, times(1)).update(anyObject());
    assertEquals(expected, actual);
  }

  @Test
  public void addOrders_added() {
    // given
    Users fromUsers = new Users(2L, "name", "password", new BigDecimal("100"));
    when(repository.getBySpecification(anyObject())).thenReturn(fromUsers);
    when(mapper.toEntity(anyObject())).thenReturn(fromUsers);
    GiftCertificate giftCertificate =
        new GiftCertificate(1L, "name", null, new BigDecimal("50"), null, null, 5, false);
    when(giftCertificateRepository.getBySpecification(anyObject())).thenReturn(giftCertificate);
    // when
    service.addOrders(1l, 1l, 2L);
    // then
    verify(repository, times(1)).getBySpecification(anyObject());
    verify(giftCertificateRepository, times(1)).getBySpecification(anyObject());
    verify(repository, times(1)).addOrders(anyObject(), anyLong(), anyLong(), anyObject());
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void addOrders_notEnoughMoney_exception() {
    // given
    Users toUsers = new Users(1L, "name", "password", new BigDecimal("23"));
    when(repository.getBySpecification(anyObject())).thenReturn(toUsers);
    when(mapper.toEntity(anyObject())).thenReturn(toUsers);
    Users fromUsers = new Users(2L, "name", "password", new BigDecimal("10"));
    when(repository.getBySpecification(anyObject())).thenReturn(fromUsers);
    when(mapper.toEntity(anyObject())).thenReturn(fromUsers);
    GiftCertificate giftCertificate =
        new GiftCertificate(1L, "name", null, new BigDecimal("50"), null, null, 5, false);
    when(giftCertificateRepository.getBySpecification(anyObject())).thenReturn(giftCertificate);
    // when
    service.addOrders(1l, 1l, 2L);
  }

  @Test
  public void getAllOrdersByUsersId_success() {
    // given
    List<Orders> orders = new ArrayList<>();
    orders.add(new Orders(1L, 2L, 91L, 15L, new BigDecimal("23"), LocalDate.now()));
    orders.add(new Orders(1L, 2L, 47L, 16L, new BigDecimal("23"), LocalDate.now()));
    orders.add(new Orders(1L, 2L, 579L, 17L, new BigDecimal("23"), LocalDate.now()));
    when(repository.getAllOrdersByUsersId(anyLong(), anyInt(), anyInt())).thenReturn(orders);

    List<OrdersDto> expected = ordersMapper.listToDto(orders);
    // when
    List<OrdersDto> actual = service.getAllOrdersByUsersId(2L, 1, 3);
    // then
    verify(repository, times(1)).getAllOrdersByUsersId(anyLong(), anyInt(), anyInt());
    assertEquals(expected, actual);
  }

  @Test(expected = WrongInputDataException.class)
  public void validateName_exception() {
    UsersDto users = new UsersDto(1L, "n", "password", new BigDecimal("23"));
    service.create(users);
  }

  @Test(expected = WrongInputDataException.class)
  public void validatePassword_exception() {
    UsersDto users = new UsersDto(1L, "name", null, new BigDecimal("23"));
    service.create(users);
  }

  @Test
  public void deleteUsers_deleted() {
    // when
    service.deleteById(1L);
    // then
    verify(repository, times(1)).delete(anyObject());
  }
}
