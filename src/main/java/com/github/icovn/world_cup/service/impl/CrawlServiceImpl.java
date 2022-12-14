package com.github.icovn.world_cup.service.impl;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.constant.MatchType;
import com.github.icovn.world_cup.dto.MatchDTO;
import com.github.icovn.world_cup.exception.InvalidMatchTypeException;
import com.github.icovn.world_cup.service.CrawlService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlServiceImpl implements CrawlService {
  
  @Value("${application.match_url}")
  private String matchUrl;
  
  @Override
  public List<MatchDTO> crawlMatch() {
    log.info("(crawl)matchUrl: {}", matchUrl);
    
    var matches = new ArrayList<MatchDTO>();
    try {
      var doc = Jsoup.connect(matchUrl).get();
      log.info("(crawl)title: {}", doc.title());
      var elements = doc.select("div.box-items");
      for (var element : elements) {
        log.info("(crawl)text: {}", element.text());
        
        var boxTable = element.getElementsByClass("box-table");
        log.debug("(crawl)table: {}", boxTable.text());
        
        var boxTime = element.getElementsByClass("box-time");
        log.debug("(crawl)time: {}", boxTime.text());
        
        var boxScore = element.getElementsByClass("box-score");
        log.debug("(crawl)score: {}", boxScore.text());
        
        var teams = element.getElementsByClass("team-name");
        var isValid = true;
        var teamNames = new ArrayList<String>();
        for (var team: teams) {
          log.debug("(crawl)team: {}", team.text());
          if (!isValidTeam(team.text())) {
            isValid = false;
            break;
          }
          teamNames.add(team.text());
        }
        
        if (!isValid) {
          break;
        }
        
        var matchDTO = MatchDTO.of(
            getType(boxTable.text()),
            getDate(boxTime.text()),
            getStartTime(boxTime.text()),
            getTeamGoals(boxScore.text(), true),
            getTeamGoals(boxScore.text(), false),
            getTeamPens(boxScore.text(), true),
            getTeamPens(boxScore.text(), false),
            teamNames.get(0),
            teamNames.get(1)
        );
        matches.add(matchDTO);
      }
    } catch (IOException ex) {
      log.error("(crawl)ex: {}", getFullStackTrace(ex));
    }
    
    return matches;
  }
  
  @Override
  public Set<String> crawlTeams() {
    log.info("(crawl)matchUrl: {}", matchUrl);
    
    var teamNames = new HashSet<String>();
    try {
      var doc = Jsoup.connect(matchUrl).get();
      log.info("(crawl)title: {}", doc.title());
      var elements = doc.select("div.box-items");
      for (var element : elements) {
        var teams = element.getElementsByClass("team-name");
        for (var team: teams) {
          log.info("(crawl)team: {}", team.text());
          if (isValidTeam(team.text())) {
            teamNames.add(team.text());
          }
        }
      }
    } catch (IOException ex) {
      log.error("(crawl)ex: {}", getFullStackTrace(ex));
    }
    return teamNames;
  }
  
  private int getDate(String input) {
    var parts = input.split(" ")[0].split("/");
    return Integer.parseInt("2022" + parts[1] + parts[0]);
  }
  
  private int getStartTime(String input) {
    var parts = input.split(" ")[1].split(":");
    return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
  }
  
  private int getTeamGoals(String input, boolean isFirstTeam) {
    if (input == null || input.isBlank()) {
      return -1;
    }
    
    var parts = input.split(" ");
    if (isFirstTeam) {
      return Integer.parseInt(parts[0]);
    }
  
    return Integer.parseInt(parts[1]);
  }
  
  private int getTeamPens(String input, boolean isFirstTeam) {
    if (input == null || input.isBlank()) {
      return -1;
    }
    
    var parts = input.split(" ");
    if (parts.length < 4) {
      return -1;
    }
    
    var penParts = parts[3].replace(")", "").split("-");
    if (isFirstTeam) {
      return Integer.parseInt(penParts[0]);
    }
    
    return Integer.parseInt(penParts[1]);
  }
  
  private MatchType getType(String input) {
    if (input == null || input.isBlank()) {
      throw new InvalidMatchTypeException(input);
    }
    
    if (input.startsWith("B???ng")) {
      return MatchType.GROUP_STAGE;
    }
  
    if (input.equals("V??ng 1/8")) {
      return MatchType.EIGHTH_FINAL;
    }
  
    if (List.of("V??ng t??? k???t", "T??? k???t 1", "T??? k???t 2", "T??? k???t 3", "T??? k???t 4").contains(input)) {
      return MatchType.QUARTER_FINAL;
    }
  
    if (List.of("V??ng b??n k???t", "B??n k???t 1", "B??n k???t 2").contains(input)) {
      return MatchType.SEMI_FINAL;
    }
  
    if (input.equals("Chung k???t")) {
      return MatchType.FINAL;
    }
  
    if (input.equals("Tranh 3/4")) {
      return MatchType.BRONZE_MEDAL_MATCH;
    }
    
    throw new InvalidMatchTypeException(input);
  }
  
  private boolean isValidTeam(String input) {
    if (input == null || input.isBlank()) {
      return false;
    }
    
    if (input.startsWith("Nh???t") || input.startsWith("Nh??") || input.startsWith("Th???ng")
        || input.startsWith("Thua")) {
      return false;
    }
    
    return true;
  }
}
