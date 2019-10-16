package com.epam.esm.entity;

import com.epam.esm.entity.listener.Action;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "auditing_history")
public class AuditingHistory extends JpaEntity {

  @Column(name = "entity_content")
  private String entityContent;

  @CreatedBy
  @Column(name = "modified_by")
  private String modifiedBy;

  @CreatedDate
  @Column(name = "modified_date")
  private LocalDate modifiedDate;

  @Enumerated(STRING)
  @Column(name = "action")
  private Action action;

  public AuditingHistory() {}

  public AuditingHistory(String entityContent, Action action) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    this.modifiedBy = authentication.getName();
    this.entityContent = entityContent;
    this.modifiedDate = LocalDate.now();
    this.action = action;
  }
}
