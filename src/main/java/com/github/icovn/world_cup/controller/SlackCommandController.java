package com.github.icovn.world_cup.controller;

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
  
  @PostMapping("/leader-board")
  public void viewLeaderBoard(@RequestBody String payload) {
    log.info("(viewLeaderBoard)payload: {}", payload);
  }
  
  @PostMapping("/my-bets")
  public void viewMyBets(@RequestBody String payload) {
    log.info("(viewMyBets)payload: {}", payload);
  }
}
