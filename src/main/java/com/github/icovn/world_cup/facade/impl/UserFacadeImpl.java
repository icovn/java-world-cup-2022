package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.world_cup.facade.UserFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {
  
  @Override
  public void bet(@NonNull String userId, @NonNull String matchId, @NonNull String bet) {
    
  }
  
  @Override
  public void notifyNotBet() {
    
  }
  
  @Override
  public void updateScore() {
    
  }
}
