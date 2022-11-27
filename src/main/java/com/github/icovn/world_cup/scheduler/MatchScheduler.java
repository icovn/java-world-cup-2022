package com.github.icovn.world_cup.scheduler;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.facade.MatchFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchScheduler {
  
  private final MatchFacade matchFacade;
  
  /**
   * Auto scan matches to create each hour
   */
  @Scheduled(cron = "${application.cron.create_matches:0 0 0/1 * * *}")
  public void createMatches() {
    log.info("(createMatches)start");
    try {
      matchFacade.createMatches();
    } catch (Exception ex) {
      log.info("(createMatches)ex: {}", getFullStackTrace(ex));
    }
    log.info("(createMatches)end");
  }
  
  /**
   * Auto post matches Slack channel each 10 minutes
   */
  @Scheduled(cron = "${application.cron.post_matches_to_slack:0 0/10 * * * *}")
  public void postMatches() {
    log.info("(postMatches)start");
    try {
      matchFacade.postMatches();
    } catch (Exception ex) {
      log.info("(postMatches)ex: {}", getFullStackTrace(ex));
    }
    log.info("(postMatches)end");
  }
  
  /**
   * Auto scan matches to update result each 10 minutes
   */
  @Scheduled(cron = "${application.cron.update_matches_result:0 0/10 * * * *}")
  public void updateResult() {
    log.info("(updateResult)start");
    try {
      matchFacade.updateMatchesResult();
    } catch (Exception ex) {
      log.info("(updateResult)ex: {}", getFullStackTrace(ex));
    }
    log.info("(updateResult)end");
  }
}
