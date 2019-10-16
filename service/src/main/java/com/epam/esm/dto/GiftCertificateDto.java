package com.epam.esm.dto;

import com.epam.esm.dto.jsonview.DateDeserializer;
import com.epam.esm.dto.jsonview.DateSerializer;
import com.epam.esm.dto.jsonview.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class GiftCertificateDto {

  @JsonView({Views.WithoutTags.class})
  private Long id;

  @JsonView({Views.WithoutTags.class})
  private String name;

  @JsonView({Views.WithoutTags.class})
  private String description;

  @JsonView({Views.WithoutTags.class})
  private BigDecimal price;

  @JsonView({Views.WithoutTags.class})
  @JsonDeserialize(using = DateDeserializer.class)
  @JsonSerialize(using = DateSerializer.class)
  private LocalDate creationDate;

  @JsonView({Views.WithoutTags.class})
  @JsonDeserialize(using = DateDeserializer.class)
  @JsonSerialize(using = DateSerializer.class)
  private LocalDate lastModified;

  @JsonView({Views.WithoutTags.class})
  private int durationInDays;

  @JsonView()
  @JsonProperty("tags")
  private List<TagDto> listTagDto;

  public GiftCertificateDto(
      Long id,
      String name,
      String description,
      BigDecimal price,
      LocalDate creationDate,
      LocalDate lastModified,
      int durationInDays) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price.setScale(2, RoundingMode.HALF_EVEN);
    this.creationDate = creationDate;
    this.lastModified = lastModified;
    this.durationInDays = durationInDays;
  }

  public GiftCertificateDto(String name, String description) {
    this(null, name, description, new BigDecimal("0.0"), null, null, 0);
  }

  public GiftCertificateDto() {}

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
    this.price = price.setScale(2, RoundingMode.HALF_EVEN);
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

  public List<TagDto> getListTagDto() {
    return listTagDto;
  }

  public void setListTagDto(List<TagDto> listTagDto) {
    this.listTagDto = listTagDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GiftCertificateDto that = (GiftCertificateDto) o;
    return durationInDays == that.durationInDays
        && Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(description, that.description)
        && Objects.equals(price, that.price)
        && Objects.equals(creationDate, that.creationDate)
        && Objects.equals(lastModified, that.lastModified)
        && Objects.equals(listTagDto, that.listTagDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, name, description, price, creationDate, lastModified, durationInDays, listTagDto);
  }

  @Override
  public String toString() {
    return "GiftCertificateDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", price="
        + price
        + ", creationDate="
        + creationDate
        + ", lastModified="
        + lastModified
        + ", durationInDays="
        + durationInDays
        + ", listTagDto="
        + listTagDto
        + '}';
  }
}
