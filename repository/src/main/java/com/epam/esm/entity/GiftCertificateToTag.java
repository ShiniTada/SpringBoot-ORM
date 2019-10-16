package com.epam.esm.entity;

import com.epam.esm.entity.listener.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "gift_certificate_to_tag")
@EntityListeners(AuditingEntityListener.class)
public class GiftCertificateToTag extends JpaEntity {

  @Column(name = "gift_certificate_id")
  private Long giftCertificateId;

  @Column(name = "tag_id")
  private Long tagId;

  public GiftCertificateToTag(Long id, Long giftCertificateId, Long tagId) {
    super(id);
    this.giftCertificateId = giftCertificateId;
    this.tagId = tagId;
  }

  public GiftCertificateToTag(Long giftCertificateId, Long tagId) {
    this(null, giftCertificateId, tagId);
  }

  public GiftCertificateToTag() {}

  public Long getGiftCertificateId() {
    return giftCertificateId;
  }

  public void setGiftCertificateId(Long giftCertificateId) {
    this.giftCertificateId = giftCertificateId;
  }

  public Long getTagId() {
    return tagId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
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
    GiftCertificateToTag that = (GiftCertificateToTag) o;
    return Objects.equals(giftCertificateId, that.giftCertificateId)
        && Objects.equals(tagId, that.tagId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), giftCertificateId, tagId);
  }

  @Override
  public String toString() {
    return "GiftCertificateToTag{"
        + "id="
        + id
        + ", giftCertificateId="
        + giftCertificateId
        + ", tagId="
        + tagId
        + '}';
  }
}
