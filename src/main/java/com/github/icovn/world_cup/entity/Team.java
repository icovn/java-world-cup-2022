package com.github.icovn.world_cup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "wc_team")
public class Team extends BaseEntity {
  
  @Column(unique = true)
  @NotBlank
  private String code;
  
  @NotBlank
  private String name;
  
  private String flagUrl;
  
  public static Team of(@NonNull String code, @NonNull String name) {
    var team = new Team();
    team.setCode(code);
    team.setName(name);
    return team;
  }
}
