package com.epam.esm.entity;

import com.epam.esm.entity.listener.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Orders extends JpaEntity {

  @Column(name = "users_id")
  private Long usersId;

  @Column(name = "to_which_users_id")
  private Long toWhichUsersId;

  @Column(name = "gift_certificate_id")
  private Long giftCertificateId;

  @Column(name = "cost")
  private BigDecimal cost;

  @CreatedDate
  @Column(name = "timestamp")
  private LocalDate timestamp;

  public Orders(
      Long id,
      Long usersId,
      Long toWhichUsersId,
      Long giftCertificateId,
      BigDecimal cost,
      LocalDate timestamp) {
    super(id);
    this.usersId = usersId;
    this.toWhichUsersId = toWhichUsersId;
    this.giftCertificateId = giftCertificateId;
    this.cost = cost;
    this.timestamp = timestamp;
  }

  public Orders(
      Long usersId,
      Long toWhichUsersId,
      Long giftCertificateId,
      BigDecimal cost) {
    this(null, usersId, toWhichUsersId, giftCertificateId, cost, null);
  }

  public Orders() {}

  public Long getUsersId() {
    return usersId;
  }

  public void setUsersId(Long usersId) {
    this.usersId = usersId;
  }

  public Long getGiftCertificateId() {
    return giftCertificateId;
  }

  public void setGiftCertificateId(Long giftCertificateId) {
    this.giftCertificateId = giftCertificateId;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public Long getToWhichUsersId() {
    return toWhichUsersId;
  }

  public void setToWhichUsersId(Long toWhichUsersId) {
    this.toWhichUsersId = toWhichUsersId;
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
    Orders orders = (Orders) o;
    return Objects.equals(usersId, orders.usersId)
        && Objects.equals(toWhichUsersId, orders.toWhichUsersId)
        && Objects.equals(giftCertificateId, orders.giftCertificateId)
        && Objects.equals(cost, orders.cost)
        && Objects.equals(timestamp, orders.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(), usersId, toWhichUsersId, giftCertificateId, cost, timestamp);
  }

  @Override
  public String toString() {
    return "Orders{"
        + "id=" + id
        + ", usersId="
        + usersId
        + ", toWhichUsersId="
        + toWhichUsersId
        + ", giftCertificateId="
        + giftCertificateId
        + ", cost="
        + cost
        + ", timestamp="
        + timestamp
        + '}';
  }
}
