package com.github.icovn.world_cup.facade;

import lombok.NonNull;

public interface UserFacade {
  
  /**
   * User give bet to a match
   * @param userId
   * @param matchId
   * @param bet
   */
  void bet(@NonNull String userId, @NonNull String matchId, @NonNull String bet);
  
  /**
   * Notify user when not bet 12h before match start
   */
  void notifyNotBet();
  
  /**
   * Update user bet result and core in board
   */
  void updateScore();
}
