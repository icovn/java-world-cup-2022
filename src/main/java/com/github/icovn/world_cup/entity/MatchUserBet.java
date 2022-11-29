package com.github.icovn.world_cup.entity;

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
@Table(name = "wc_match_user_bet")
public class MatchUserBet extends BaseEntity {
  
  private String bet;
  
  private int result = 0;
  
  @NotBlank
  private String matchId;
  
  @NotBlank
  private String userId;
  
  public static MatchUserBet of(@NonNull String matchId, @NonNull String userId, String bet) {
    var matchUserBet = new MatchUserBet();
    matchUserBet.setMatchId(matchId);
    matchUserBet.setUserId(userId);
    matchUserBet.setBet(bet);
    return matchUserBet;
  }
}
