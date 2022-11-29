package com.github.icovn.world_cup.controller;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.util.MapperUtil;
import com.github.icovn.world_cup.model.SlackInteraction;
import com.github.icovn.world_cup.facade.UserFacade;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/slack")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SlackInteractiveController {
  
  private final UserFacade userFacade;
  
  @PostMapping("/interactive-endpoint")
  void handle(@RequestBody String payload) {
    log.info("(handle)payload: {}", payload);
    try {
      var decoded = URLDecoder.decode(payload, StandardCharsets.UTF_8.name());
      log.info("(handle)decoded: {}", decoded);
      
      var slackInteractive = MapperUtil.getMapper().readValue(
          decoded.substring(8),
          SlackInteraction.class
      );
      log.info("(handle)slackInteractive: {}", slackInteractive);
  
      var interactiveValues = slackInteractive.getActions().get(0).getValue().split("_");
      userFacade.bet(
          interactiveValues[0],
          slackInteractive.getUser().getId(),
          slackInteractive.getUser().getUsername(),
          interactiveValues[1]
      );
    } catch (Exception ex) {
      log.error("(handle)ex: {}", getFullStackTrace(ex));
    }
    
  }
  
  @PostMapping("/options-load-endpoint")
  void loadOptions(@RequestBody String payload) {
    log.info("(loadOptions)payload: {}", payload);
  }
}
