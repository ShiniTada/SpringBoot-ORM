package com.epam.esm.entity;

import com.epam.esm.entity.listener.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "tag")
@EntityListeners(AuditingEntityListener.class)
public class Tag extends JpaEntity {

  @Column(name = "name", unique = true)
  private String name;

  public Tag(Long id, String name) {
    super(id);
    this.name = name;
  }

  public Tag(Long id) {
    this(id, null);
  }

  public Tag(String name) {
    this(null, name);
  }

  public Tag() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
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
    Tag tag = (Tag) o;
    return Objects.equals(name, tag.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }
}
