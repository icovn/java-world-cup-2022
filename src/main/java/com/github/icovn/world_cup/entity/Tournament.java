package com.github.icovn.world_cup.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor(staticName = "of")
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Tournament extends BaseEntity {
  
  @NotBlank
  private String name;
}
