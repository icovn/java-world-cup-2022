package com.github.icovn.world_cup.controller;

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
  
  @PostMapping("/interactive-endpoint")
  void handle(@RequestBody String payload) {
    log.info("(handle)payload: {}", payload);
  }
  
  @PostMapping("/options-load-endpoint")
  void loadOptions(@RequestBody String payload) {
    log.info("(loadOptions)payload: {}", payload);
  }
}
