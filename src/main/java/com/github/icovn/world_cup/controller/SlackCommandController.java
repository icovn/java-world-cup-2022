package com.github.icovn.world_cup.controller;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.facade.UserFacade;
import com.github.icovn.world_cup.util.UrlUtil;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/slack/command")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SlackCommandController {
  
  private final UserFacade userFacade;
  
  @PostMapping("/leader-board")
  public void viewLeaderBoard(@RequestBody String payload) {
    log.info("(viewLeaderBoard)payload: {}", payload);
    try {
      var decoded = URLDecoder.decode(payload, StandardCharsets.UTF_8.name());
      log.info("(viewLeaderBoard)decoded: {}", decoded);
      
      var channelName = UrlUtil.getParamValue(decoded, "channel_name");
      var userId = UrlUtil.getParamValue(decoded, "user_id");
      var userName = UrlUtil.getParamValue(decoded, "user_name");
      log.info(
          "(viewLeaderBoard)userId: {}, userName: {}, channelName: {}", 
          userId, userName, channelName
      );
  
      userFacade.viewLeaderBoard(channelName);
    } catch (Exception ex) {
      log.error("(viewLeaderBoard)ex: {}", getFullStackTrace(ex));
    }
  }
  
  @PostMapping("/my-bets")
  public void viewMyBets(@RequestBody String payload) {
    log.info("(viewMyBets)payload: {}", payload);
    try {
      var decoded = URLDecoder.decode(payload, StandardCharsets.UTF_8.name());
      log.info("(viewMyBets)decoded: {}", decoded);
  
      var channelName = UrlUtil.getParamValue(decoded, "channel_name");
      var userId = UrlUtil.getParamValue(decoded, "user_id");
      var userName = UrlUtil.getParamValue(decoded, "user_name");
      log.info(
          "(viewMyBets)userId: {}, userName: {}, channelName: {}",
          userId, userName, channelName
      );
  
      userFacade.viewMyBet(userId, channelName);
    } catch (Exception ex) {
      log.error("(viewLeaderBoard)ex: {}", getFullStackTrace(ex));
    }
  }
}
