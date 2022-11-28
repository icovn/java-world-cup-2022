package com.github.icovn.world_cup.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wc_tournament")
public class Tournament extends BaseEntity {
  
  @NotBlank
  private String name;
  
  public static Tournament of(@NonNull String id, @NonNull String name) {
    var tournament = new Tournament();
    tournament.setId(id);
    tournament.setName(name);
    return tournament;
  }
}
