package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.TournamentUserBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentUserBoardRepository extends JpaRepository<TournamentUserBoard, String> {
 
  List<TournamentUserBoard> findAllByTournamentIdOrderByRankIndexAsc(String tournamentId);

  TournamentUserBoard findFirstByTournamentIdAndUserId(String tournamentId, String userId);
}
