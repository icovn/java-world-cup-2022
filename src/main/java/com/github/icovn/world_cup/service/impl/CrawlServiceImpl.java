package com.github.icovn.world_cup.service.impl;

import com.github.icovn.world_cup.dto.MatchDTO;
import com.github.icovn.world_cup.service.CrawlService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlServiceImpl implements CrawlService {
  
  @Override
  public List<MatchDTO> crawl() {
    return List.of();
  }
}
