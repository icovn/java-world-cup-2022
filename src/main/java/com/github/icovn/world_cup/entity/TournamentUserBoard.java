package com.github.icovn.world_cup.entity;

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
    name = "wc_tournament_user_board",
    indexes = @Index(
        name = "id_tournament_user_board_main", 
        columnList = "tournamentId,userId", 
        unique = true
    )
)
public class TournamentUserBoard extends BaseEntity {
  
  private int score = 0;
  private int rankIndex = -1;
  
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

  public static TournamentUserBoard of(
      @NonNull String tournamentId,
      @NonNull String userId,
      int rankIndex,
      int score) {
    var tournamentUserBoard = TournamentUserBoard.of(tournamentId, userId);
    tournamentUserBoard.setRankIndex(rankIndex);
    tournamentUserBoard.setScore(score);
    return tournamentUserBoard;
  }
}
