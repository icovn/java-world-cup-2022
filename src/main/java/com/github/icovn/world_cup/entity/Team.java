package com.github.icovn.world_cup.entity;

import java.util.List;
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
  
  public static String getTeamIdByCode(String teamCode, List<Team> teams) {
    var team = teams.stream()
        .filter(o -> o.getCode().equals(teamCode)).findFirst().orElse(null);
    if (team == null) {
      return null;
    }
    
    return team.getId();
  }
  
  public static String getTeamIdByName(String teamName, List<Team> teams) {
    var team = teams.stream()
        .filter(o -> o.getName().equals(teamName)).findFirst().orElse(null);
    if (team == null) {
      return null;
    }
    
    return team.getId();
  }
  
  public static String getTeamName(String teamId, List<Team> teams) {
    var team = teams.stream()
        .filter(o -> o.getId().equals(teamId)).findFirst().orElse(null);
    if (team == null) {
      return null;
    }
    return team.getName();
  }
}
