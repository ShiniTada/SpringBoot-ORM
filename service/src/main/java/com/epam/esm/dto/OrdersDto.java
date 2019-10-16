package com.epam.esm.dto;

import com.epam.esm.dto.jsonview.DateDeserializer;
import com.epam.esm.dto.jsonview.DateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class OrdersDto {

  private Long id;

  @JsonProperty("userId")
  private Long toWhichUsersId;

  private Long giftCertificateId;
  private BigDecimal cost;

  @JsonDeserialize(using = DateDeserializer.class)
  @JsonSerialize(using = DateSerializer.class)
  @JsonProperty("purchaseDate")
  private LocalDate timestamp;

  public OrdersDto() {}

  public OrdersDto(
      Long id,
      Long toWhichUsersId,
      Long giftCertificateId,
      BigDecimal cost,
      LocalDate timestamp) {
    this.id = id;
    this.toWhichUsersId = toWhichUsersId;
    this.giftCertificateId = giftCertificateId;
    this.cost = cost.setScale(2, RoundingMode.HALF_EVEN);
    this.timestamp = timestamp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    this.cost = cost.setScale(2, RoundingMode.HALF_EVEN);
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
    OrdersDto ordersDto = (OrdersDto) o;
    return Objects.equals(id, ordersDto.id)
        && Objects.equals(toWhichUsersId, ordersDto.toWhichUsersId)
        && Objects.equals(giftCertificateId, ordersDto.giftCertificateId)
        && Objects.equals(cost, ordersDto.cost)
        && Objects.equals(timestamp, ordersDto.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, toWhichUsersId, giftCertificateId, cost, timestamp);
  }

  @Override
  public String toString() {
    return "OrdersDto{"
        + "id="
        + id
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
