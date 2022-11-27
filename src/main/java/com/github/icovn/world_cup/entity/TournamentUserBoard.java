package com.github.icovn.world_cup.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class TournamentUserBoard extends BaseEntity {
  
  private int score = 0;
  private int rank = -1;
  
  @NotBlank
  private String tournamentId;
  
  @NotBlank
  private String userId;
  
  public static TournamentUserBoard of(@NonNull String tournamentId, @NonNull String userId) {
    var tournamentUserBoard = new TournamentUserBoard();
    tournamentUserBoard.setTournamentId(tournamentId);
    tournamentUserBoard.setUserId(userId);
    return tournamentUserBoard;
  }
}
