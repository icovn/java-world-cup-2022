package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.world_cup.facade.MatchFacade;
import com.github.icovn.world_cup.service.CrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchFacadeImpl implements MatchFacade {
  
  private final CrawlService crawlService;
  
  @Value("${application.default.world_cup_tournament_id:2022_11_QATAR}")
  private String defaultTournamentId;
  
  @Override
  public void createMatches() {
    
  }
  
  @Override
  public void postMatches() {
    
  }
  
  @Override
  public void updateMatchesResult() {
    
  }
}
