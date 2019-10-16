package com.epam.esm.entity;

import com.epam.esm.entity.listener.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@NamedStoredProcedureQuery(
    name = "getGiftCertificate",
    procedureName = "get_gift_certificate_by_parts",
    resultClasses = GiftCertificate.class,
    parameters = {
      @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = void.class),
      @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class),
      @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class),
      @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class)
    })
@Entity
@Table(name = "gift_certificate")
@EntityListeners(AuditingEntityListener.class)
public class GiftCertificate extends JpaEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "creation_date")
  private LocalDate creationDate;

  @Column(name = "last_modified")
  private LocalDate lastModified;

  @Column(name = "duration_in_days")
  private int durationInDays;

  @Column(name = "deleted")
  private boolean deleted;

  public GiftCertificate(
      Long id,
      String name,
      String description,
      BigDecimal price,
      LocalDate creationDate,
      LocalDate lastModified,
      int durationInDays,
      boolean deleted) {
    super(id);
    this.name = name;
    this.description = description;
    this.price = price;
    this.creationDate = creationDate;
    this.lastModified = lastModified;
    this.durationInDays = durationInDays;
    this.deleted = deleted;
  }

  public GiftCertificate(Long id) {
    this(id, null, null, new BigDecimal("0.0"), null, null, 0, false);
  }

  public GiftCertificate() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDate getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDate lastModified) {
    this.lastModified = lastModified;
  }

  public int getDurationInDays() {
    return durationInDays;
  }

  public void setDurationInDays(int durationInDays) {
    this.durationInDays = durationInDays;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
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
    GiftCertificate that = (GiftCertificate) o;
    return durationInDays == that.durationInDays
        && deleted == that.deleted
        && Objects.equals(name, that.name)
        && Objects.equals(description, that.description)
        && Objects.equals(price, that.price)
        && Objects.equals(creationDate, that.creationDate)
        && Objects.equals(lastModified, that.lastModified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        name,
        description,
        price,
        creationDate,
        lastModified,
        durationInDays,
        deleted);
  }

  @Override
  public String toString() {
    return "GiftCertificate{"
        + "id=" + id
        + ", name='"
        + name
        + ", price="
        + price
        + ", durationInDays="
        + durationInDays
        + ", deleted="
        + deleted
        + '}';
  }
}
