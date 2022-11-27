package com.github.icovn.world_cup.facade;

public interface MatchFacade {
  
  /**
   * Create matches if not exist
   */
  void createMatches();
  
  /**
   * Post matches to Slack
   */
  void postMatches();
  
  /**
   * Update matches result
   */
  void updateMatchesResult();
}
