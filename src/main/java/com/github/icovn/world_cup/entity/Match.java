package com.github.icovn.world_cup.entity;

import com.github.icovn.world_cup.constant.MatchType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Match extends BaseEntity {
  
  private static final String DRAW_RESULT = "DRAW";
  
  @NotNull
  private MatchType type;
  
  private int date = 0;
  private int startTime = 0;
  private int team1Goals = 0;
  private int team2Goals = 0;
  
  private String result;
  
  private String slackMessageId;
  
  @NotBlank
  private String team1Id;
  
  @NotBlank
  private String team2Id;
  
  @NotBlank
  private String tournamentId;
  
  public static Match of(
      @NonNull String tournamentId, 
      @NonNull String team1Id, 
      @NonNull String team2Id, 
      int date, 
      int startTime
  ) {
    var match = new Match();
    match.setTournamentId(tournamentId);
    match.setTeam1Id(team1Id);
    match.setTeam2Id(team2Id);
    match.setDate(date);
    match.setStartTime(startTime);
    return match;
  }
}
