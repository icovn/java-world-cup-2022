package com.github.icovn.world_cup.service.impl;

import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.service.MatchService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MatchServiceImpl implements MatchService {
  
  private final MatchRepository repository;
  
  @Override
  public void create(Match match) {
    log.info("(crate)match: {}", match);
    repository.save(match);
  }
  
  @Override
  public Match find(@NonNull String tournamentId, int date, int startTime) {
    return repository.findFirstByTournamentIdAndDateAndStartTime(tournamentId, date, startTime);
  }
}
