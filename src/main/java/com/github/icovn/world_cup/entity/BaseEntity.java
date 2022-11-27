package com.github.icovn.world_cup.entity;

import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@MappedSuperclass
public class BaseEntity {
  
  @Id
  private String id;
  
  @CreatedDate
  private Long createdAt;
  
  @LastModifiedDate
  private Long updatedAt;
  
  @PrePersist
  private void ensureId() {
    if (this.getId() == null || this.getId().isEmpty()) {
      this.setId(UUID.randomUUID().toString());
    }
  }
}
