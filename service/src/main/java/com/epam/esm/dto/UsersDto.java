package com.epam.esm.dto;

import com.epam.esm.dto.jsonview.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class UsersDto {

  @JsonView({Views.AllUsersView.class, Views.CurrentUsersView.class})
  private Long id;

  @JsonView({Views.AllUsersView.class, Views.CurrentUsersView.class})
  private String name;

  @JsonView()
  private String password;

  @JsonView(Views.CurrentUsersView.class)
  private BigDecimal usersBalance;

  public UsersDto() {}

  public UsersDto(Long id, String name, String password, BigDecimal usersBalance) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.usersBalance = usersBalance.setScale(2, RoundingMode.HALF_EVEN);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public BigDecimal getUsersBalance() {
    return usersBalance;
  }

  public void setUsersBalance(BigDecimal usersBalance) {
    this.usersBalance = usersBalance.setScale(2, RoundingMode.HALF_EVEN);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersDto usersDto = (UsersDto) o;
    return Objects.equals(id, usersDto.id)
        && Objects.equals(name, usersDto.name)
        && Objects.equals(password, usersDto.password)
        && Objects.equals(usersBalance, usersDto.usersBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, password, usersBalance);
  }

  @Override
  public String toString() {
    return "UsersDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", usersBalance="
        + usersBalance
        + '}';
  }
}
