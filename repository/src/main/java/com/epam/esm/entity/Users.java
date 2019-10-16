package com.epam.esm.entity;

import com.epam.esm.entity.listener.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class Users extends JpaEntity {

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private RoleEnum role;

  @Column(name = "users_balance")
  private BigDecimal usersBalance;

  public Users() {}

  public Users(Long id, String name, String password, RoleEnum role, BigDecimal usersBalance) {
    super(id);
    this.name = name;
    this.password = password;
    this.role = role;
    this.usersBalance = usersBalance;
  }

  public Users(Long id) {
    this(id, null, null, RoleEnum.ROLE_USER, new BigDecimal("0.0"));
  }

  public Users(Long id, String name, String password, BigDecimal usersBalance) {
    this(id, name, password, RoleEnum.ROLE_USER, usersBalance);
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

  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public BigDecimal getUsersBalance() {
    return usersBalance;
  }

  public void setUsersBalance(BigDecimal usersBalance) {
    this.usersBalance = usersBalance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Users users = (Users) o;
    return Objects.equals(name, users.name)
        && Objects.equals(password, users.password)
        && role == users.role
        && Objects.equals(usersBalance, users.usersBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, password, role, usersBalance);
  }

  @Override
  public String toString() {
    return "Users{"
        + "id=" + id
        + ", name='"
        + name
        + '\''
        + ", role="
        + role
        + ", usersBalance="
        + usersBalance
        + '}';
  }
}
