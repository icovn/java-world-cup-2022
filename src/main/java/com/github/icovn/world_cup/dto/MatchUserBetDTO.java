package com.github.icovn.world_cup.dto;

import com.github.icovn.world_cup.constant.BetStatus;

public interface MatchUserBetDTO {
  
  String getMatchId();
  
  Integer getDate();
  
  Integer getStartTime();
  
  String getTeam1Id();
  
  String getTeam1Name();
  
  String getTeam2Id();
  
  String getTeam2Name();
  
  String getMatchResult();
  
  String getUserBet();
  
  BetStatus getBetStatus();
  
  Integer getBetResult();
  
  long getBetAt();
}
