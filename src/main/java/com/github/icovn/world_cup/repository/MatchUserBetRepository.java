package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.MatchUserBet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchUserBetRepository extends JpaRepository<MatchUserBet, String> {
  
  MatchUserBet findFirstByMatchIdAndUserId(String matchId, String userId);
  
  List<MatchUserBet> findAllByResultLessThan(int result);
}
