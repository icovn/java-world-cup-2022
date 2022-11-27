package com.github.icovn.world_cup.scheduler;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserScheduler {
  
  private final UserFacade userFacade;
  
  /**
   * Auto notify user when not bet each 10 minutes
   */
  @Scheduled(cron = "${application.cron.notify_not_bet:0 0/10 * * * *}")
  public void notifyNotBet() {
    log.info("(notifyNotBet)start");
    try {
      userFacade.notifyNotBet();
    } catch (Exception ex) {
      log.info("(notifyNotBet)ex: {}", getFullStackTrace(ex));
    }
    log.info("(notifyNotBet)end");
  }
  
  /**
   * Auto update score each 10 minutes
   */
  @Scheduled(cron = "${application.cron.update_score:0 0/10 * * * *}")
  public void updateScore() {
    log.info("(updateScore)start");
    try {
      userFacade.updateScore();
    } catch (Exception ex) {
      log.info("(updateScore)ex: {}", getFullStackTrace(ex));
    }
    log.info("(updateScore)end");
  }
}
