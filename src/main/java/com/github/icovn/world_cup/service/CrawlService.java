package com.github.icovn.world_cup.service;

import com.github.icovn.world_cup.dto.MatchDTO;
import java.util.List;
import java.util.Set;

public interface CrawlService {
  
  List<MatchDTO> crawlMatch();
  
  Set<String> crawlTeams();
}
