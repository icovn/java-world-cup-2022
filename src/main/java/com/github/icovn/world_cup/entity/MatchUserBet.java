package com.github.icovn.world_cup.entity;

import com.github.icovn.world_cup.constant.BetStatus;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
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
@Table(
    name = "wc_match_user_bet",
    indexes = @Index(
        name = "id_match_user_bet_main", 
        columnList = "matchId,userId", 
        unique = true
    )
)
public class MatchUserBet extends BaseEntity {
  
  private String bet;
  
  private int result = -1;
  
  private BetStatus status;
  
  @NotBlank
  private String matchId;
  
  @NotBlank
  private String userId;
  
  public static MatchUserBet of(@NonNull String matchId, @NonNull String userId, String bet) {
    var matchUserBet = new MatchUserBet();
    matchUserBet.setMatchId(matchId);
    matchUserBet.setUserId(userId);
    matchUserBet.setBet(bet);
    matchUserBet.setStatus(BetStatus.SUCCESS);
    return matchUserBet;
  }
  
  public static MatchUserBet of(@NonNull String matchId, @NonNull String userId, BetStatus status) {
    var matchUserBet = new MatchUserBet();
    matchUserBet.setMatchId(matchId);
    matchUserBet.setUserId(userId);
    matchUserBet.setResult(0);
    matchUserBet.setStatus(status);
    return matchUserBet;
  }
}
