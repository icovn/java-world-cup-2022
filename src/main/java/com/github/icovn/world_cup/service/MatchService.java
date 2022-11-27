package com.github.icovn.world_cup.service;

import com.github.icovn.world_cup.entity.Match;
import lombok.NonNull;

public interface MatchService {
  
  void create(Match match);
  
  Match find(@NonNull String tournamentId, int date, int startTime);
}
