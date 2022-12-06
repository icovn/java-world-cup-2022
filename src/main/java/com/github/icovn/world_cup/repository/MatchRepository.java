package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, String> {
  
  List<Match> findAllBySlackMessageIdIsNullOrderByDateAscStartTimeAsc();
  
  Match findFirstBySlackMessageId(String slackMessageId);
  
  Match findFirstByTournamentIdAndDateAndTeam1IdAndTeam2Id(
      String tournamentId, 
      int date, 
      String team1Id,
      String team2Id
  );
}
