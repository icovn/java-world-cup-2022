package com.github.icovn.world_cup.entity;

import com.github.icovn.util.DateUtil;
import com.github.icovn.world_cup.constant.MatchType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "wc_match")
public class Match extends BaseEntity {
  
  public static final String DRAW_RESULT = "DRAW";
  
  @NotNull
  private MatchType type;
  
  private int date = 0;
  private int startTime = 0;
  private int team1Goals = -1;
  private int team2Goals = -1;
  private int team1Pens = -1;
  private int team2Pens = -1;
  
  private String result;
  
  @Column(unique = true)
  private String slackMessageId;
  
  @NotBlank
  private String team1Id;
  
  @NotBlank
  private String team2Id;
  
  @NotBlank
  private String tournamentId;
  
  @Transient
  public String getDateString() {
    var matchDate = DateUtil.toDate(Integer.toString(date), "yyyyMMdd");
    return DateUtil.toString(matchDate, "dd/MM/yyyy");
  }
  
  @Transient
  public long getStartTimeInTimestamp() {
    var hour = startTime/60;
    var minutes = startTime % 60;
    var dateAndTime = date + " " + hour + ":" + minutes;
    var matchDate = DateUtil.toDate(dateAndTime, "yyyyMMdd H:m");
    return matchDate.getTime();
  }
  
  @Transient
  public String getTimeString() {
    var hour = startTime/60;
    var minutes = startTime % 60;
    if (minutes == 0) {
      return "(" + hour + "h" + ")";  
    } 
    return "(" + hour + "h:" + minutes + ")";
  }
  
  public static Match of(
      @NonNull String tournamentId,
      int date,
      int startTime,
      @NonNull MatchType type,
      @NonNull String team1Id,
      @NonNull String team2Id
  ) {
    var match = new Match();
    match.setTournamentId(tournamentId);
    match.setDate(date);
    match.setStartTime(startTime);
    match.setType(type);
    match.setTeam1Id(team1Id);
    match.setTeam2Id(team2Id);
    return match;
  }
  
  public static Match of(
      @NonNull String tournamentId,
      int date,
      int startTime,
      @NonNull MatchType type,
      @NonNull String team1Id, 
      @NonNull String team2Id,
      int team1Goals,
      int team2Goals,
      int team1Pens,
      int team2Pens
  ) {
    var match = Match.of(tournamentId, date, startTime, type, team1Id, team2Id);
    match.setTeam1Goals(team1Goals);
    match.setTeam2Goals(team2Goals);
    match.setTeam1Pens(team1Pens);
    return match;
  }
  
  public static Match getMatch(String matchId, List<Match> matches) {
    return matches.stream()
        .filter(o -> o.getId().equals(matchId))
        .findFirst().orElse(null);
  }
}
