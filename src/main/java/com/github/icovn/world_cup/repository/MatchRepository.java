package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, String> {
  
  Match findFirstByTournamentIdAndDateAndStartTime(String tournamentId, int date, int startTime);
}
