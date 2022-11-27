package com.github.icovn.world_cup.service;

import com.github.icovn.world_cup.dto.MatchDTO;
import java.util.List;

public interface CrawlService {
  
  List<MatchDTO> crawl();
}
