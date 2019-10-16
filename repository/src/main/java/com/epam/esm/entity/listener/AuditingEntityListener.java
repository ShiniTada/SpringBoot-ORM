package com.epam.esm.entity.listener;

import com.epam.esm.entity.AuditingHistory;
import com.epam.esm.entity.JpaEntity;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditingEntityListener {

  @PrePersist
  public void prePersist(JpaEntity entity) {
    perform(entity, Action.INSERTED);
  }

  @PreUpdate
  public void preUpdate(JpaEntity entity) {
    perform(entity, Action.UPDATED);
  }

  @PreRemove
  public void preRemove(JpaEntity entity) {
    perform(entity, Action.DELETED);
  }

  private void perform(JpaEntity entity, Action action) {
    EntityManager em = BeanUtil.getBean(EntityManager.class);
    em.persist(new AuditingHistory(entity.toString(), action));
  }
}
