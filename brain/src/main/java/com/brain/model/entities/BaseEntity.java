package com.brain.model.entities;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @TableGenerator(name = "baseID", initialValue = 1000)
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "baseID")
  @Column(name="id", nullable = false, updatable = false)
  private long id;

  public long getId() {
    return id;
  }

  public BaseEntity setId(long id) {
    this.id = id;
    return this;
  }
}
