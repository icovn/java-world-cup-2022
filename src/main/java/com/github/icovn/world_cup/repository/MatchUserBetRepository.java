package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.dto.MatchUserBetDTO;
import com.github.icovn.world_cup.dto.MatchUserBetResultDTO;
import com.github.icovn.world_cup.entity.MatchUserBet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchUserBetRepository extends JpaRepository<MatchUserBet, String> {
  
  MatchUserBet findFirstByMatchIdAndUserId(String matchId, String userId);
  
  List<MatchUserBet> findAllByResultLessThan(int result);
  
  @Query("SELECT m.id as matchId, m.date as date, m.startTime as startTime, t1.name as team1Name, t2.name as team2Name, m.team1Id as team1Id, m.team2Id as team2Id, m.result as matchResult,  mub.result as betResult, mub.bet as userBet, mub.status as betStatus, mub.createdAt as betAt "
      + "FROM MatchUserBet mub " 
      + "INNER JOIN Match m ON mub.matchId = m.id " 
      + "INNER JOIN Team t1 ON m.team1Id = t1.id " 
      + "INNER JOIN Team t2 ON m.team2Id = t2.id " 
      + "WHERE mub.userId = :userId " 
      + "ORDER BY m.date DESC, m.startTime DESC "
  )
  List<MatchUserBetDTO> findHistory(@Param("userId") String userId);

  @Query("SELECT SUM(result) AS score, userId FROM MatchUserBet "
      + "GROUP BY userId "
      + "ORDER BY total DESC ")
  List<MatchUserBetResultDTO> findScore();
}
