package com.github.icovn.world_cup.facade;

import lombok.NonNull;

public interface UserFacade {
  
  /**
   * User give bet to a match
   * @param matchId
   * @param userId
   * @param bet
   */
  void bet(
      @NonNull String matchId, 
      @NonNull String userId, 
      @NonNull String userName, 
      @NonNull String bet
  );
  
  /**
   * Notify user when not bet 12h before match start
   */
  void notifyNotBet();
  
  /**
   * Update user bet result and core in board
   */
  void updateScore();
  
  void viewLeaderBoard(@NonNull String userId);
  
  void viewMyBet(@NonNull String userId);
}
