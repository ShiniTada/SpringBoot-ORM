package com.epam.esm.service.impl;

import com.epam.esm.dto.OrdersDto;
import com.epam.esm.dto.UsersDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Orders;
import com.epam.esm.entity.RoleEnum;
import com.epam.esm.entity.Users;
import com.epam.esm.exception.NotEnoughMoneyException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WrongIdException;
import com.epam.esm.exception.WrongInputDataException;
import com.epam.esm.mapper.OrdersModelMapper;
import com.epam.esm.mapper.UsersModelMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.UsersRepository;
import com.epam.esm.service.UsersService;
import com.epam.esm.specification.giftcertficate.GetGiftCertificateByIdSpecification;
import com.epam.esm.specification.users.GetAllUsersSpecification;
import com.epam.esm.specification.users.GetUsersByIdSpecification;
import com.epam.esm.specification.users.GetUsersByNameSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service("usersService")
public class UsersServiceImpl implements UsersService {

  private static final int MIN_NAME_LENGTH = 4;
  private static final int MAX_NAME_LENGTH = 15;
  private static final int MIN_PASSWORD_LENGTH = 4;
  private static final int MAX_PASSWORD_LENGTH = 15;
  private static final double MIN_BALANCE_VALUE = 0.0;

  @Autowired private UsersRepository usersRepository;

  @Autowired private UsersModelMapper usersModelMapper;

  @Autowired private GiftCertificateRepository giftCertificateRepository;

  @Autowired private OrdersModelMapper ordersModelMapper;

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public List<UsersDto> getAll(String sort, int pageNumber, int maxResults) {
    List<Users> usersList =
        usersRepository.getListBySpecification(
            new GetAllUsersSpecification(sort), pageNumber, maxResults);
    return usersModelMapper.listToDto(usersList);
  }

  @Override
  public UsersDto getById(long id) {
    Users users = usersRepository.getBySpecification(new GetUsersByIdSpecification(id));
    if (users != null) {
      return usersModelMapper.toDto(users);
    } else {
      return null;
    }
  }

  @Override
  public UsersDto getByName(String name) {
    Users tag = usersRepository.getBySpecification(new GetUsersByNameSpecification(name));
    return usersModelMapper.toDto(tag);
  }

  @Override
  public UsersDto create(UsersDto usersDto) {
    validate(usersDto, true);
    if (usersDto.getUsersBalance() == null) {
      usersDto.setUsersBalance(new BigDecimal("0.0"));
    }
    Users users = usersModelMapper.toEntity(usersDto);
    users.setPassword(passwordEncoder.encode(users.getPassword()));
    users.setRole(RoleEnum.ROLE_USER);
    users = usersRepository.create(users);
    if (users == null) {
      throw new ServiceException("Server error. User not added.");
    }
    usersDto.setId(users.getId());
    usersDto = usersModelMapper.toDto(users);
    return usersDto;
  }

  @Override
  public UsersDto update(long id, UsersDto usersDto) {
    validate(usersDto, false);
    usersDto.setId(id);
    Users oldU = usersModelMapper.toEntity(this.getById(id));
    Users newU = takeOldValuesIfNewOnesAreEmpty(oldU, usersModelMapper.toEntity(usersDto));

    Users outUsers = usersRepository.update(newU);
    if (outUsers == null) {
      throw new ServiceException("Server error.User does not updated.");
    }

    usersDto = usersModelMapper.toDto(outUsers);
    return usersDto;
  }

  private Users takeOldValuesIfNewOnesAreEmpty(Users oldU, Users newU) {
    newU.setName(oldU.getName());
    if (newU.getPassword() == null) {
      newU.setPassword(oldU.getPassword());
    } else {
      newU.setPassword(passwordEncoder.encode(newU.getPassword()));
    }
    if (newU.getUsersBalance() == null) {
      newU.setUsersBalance(oldU.getUsersBalance());
    }
    newU.setRole(oldU.getRole());
    return newU;
  }

  @Override
  public void deleteById(long id) {
    usersRepository.delete(new Users(id));
  }

  private void validate(UsersDto usersDto, boolean isCreate) {

    List<String> exceptionMessages = new ArrayList<>();
    if (isCreate && usersDto.getName() == null) {
      exceptionMessages.add("Name is required.");
    }
    if (usersDto.getName() != null
        && (usersDto.getName().length() < MIN_NAME_LENGTH
            || usersDto.getName().length() > MAX_NAME_LENGTH)) {
      exceptionMessages.add("Name must be between 4 and 15 characters.");
    }
    if (isCreate && usersDto.getPassword() == null) {
      exceptionMessages.add("Password is required.");
    }
    if (usersDto.getPassword() != null
        && (usersDto.getPassword().length() < MIN_PASSWORD_LENGTH
            || usersDto.getPassword().length() > MAX_PASSWORD_LENGTH)) {
      exceptionMessages.add("Password must be between 4 and 15 characters.");
    }
    if (usersDto.getUsersBalance() != null
        && usersDto.getUsersBalance().doubleValue() < MIN_BALANCE_VALUE) {
      exceptionMessages.add("Wrong balance value: " + usersDto.getUsersBalance());
    }
    if (!exceptionMessages.isEmpty()) {
      throw new WrongInputDataException(exceptionMessages);
    }
  }

  @Override
  public OrdersDto addOrders(Long giftCertificateId, Long toUsersId, Long fromUsersId) {
    Users fromUsers = usersModelMapper.toEntity(this.getById(fromUsersId));
    GiftCertificate giftCertificate =
        giftCertificateRepository.getBySpecification(
            new GetGiftCertificateByIdSpecification(giftCertificateId));
    if (giftCertificate == null) {
      throw new WrongIdException(
          "Gift certificate with id " + giftCertificateId + " does not exist.");
    }
    double fromUsersBalance = fromUsers.getUsersBalance().doubleValue();
    double price = giftCertificate.getPrice().doubleValue();
    if (fromUsersBalance < price) {
      throw new NotEnoughMoneyException(
          "Not enough money! The gift certificate price: "
              + price
              + ". Your balance: "
              + fromUsersBalance
              + ".");
    } else {
      double newUserBalance =
          fromUsers.getUsersBalance().doubleValue() - giftCertificate.getPrice().doubleValue();
      Orders orders =
          new Orders(fromUsersId, toUsersId, giftCertificate.getId(), giftCertificate.getPrice());
      return ordersModelMapper.toDto(
              usersRepository.addOrders(orders, BigDecimal.valueOf(newUserBalance)));
    }
  }

  @Override
  public List<OrdersDto> getAllOrdersByUsersId(Long usersId, int pageNumber, int maxResults) {
    return ordersModelMapper.listToDto(
        usersRepository.getAllOrdersByUsersId(usersId, pageNumber, maxResults));
  }

  @Override
  public UserDetails loadUserByUsername(String name) {
    Users users = usersRepository.getBySpecification(new GetUsersByNameSpecification(name));
    if (users == null) {
      throw new UsernameNotFoundException("Invalid username or password.");
    }
    GrantedAuthority authority = new SimpleGrantedAuthority(users.getRole().name());
    return new org.springframework.security.core.userdetails.User(
        users.getName(), users.getPassword(), Arrays.asList(authority));
  }
}
