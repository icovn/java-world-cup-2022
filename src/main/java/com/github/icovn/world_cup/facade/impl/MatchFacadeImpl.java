package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.util.DateUtil;
import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.Team;
import com.github.icovn.world_cup.exception.TeamNotFoundException;
import com.github.icovn.world_cup.facade.MatchFacade;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.service.CrawlService;
import com.github.icovn.world_cup.service.SlackService;
import java.util.Date;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchFacadeImpl implements MatchFacade {
  
  private final CrawlService crawlService;
  private final MatchRepository matchRepository;
  private final SlackService slackService;
  private final TeamRepository teamRepository;
  
  @Value("${application.default.world_cup_channel_name:test-world-cup}")
  private String channelName;
  
  @Value("${application.default.enable_draw:false}")
  private Boolean enableDraw;
  
  @Value("${application.default.world_cup_tournament_id:2022_11_QATAR}")
  private String tournamentId;
  
  @Override
  public void createMatches() {
    log.info("(createMatches)");
    var matches = crawlService.crawlMatch();
    log.info("(createMatches)matches: {}", matches.size());
    var teams = teamRepository.findAll();
    log.info("(createMatches)teams: {}", teams.size());
    
    for (var match: matches) {
      log.info("(createMatches)match: {}", match);
      var team1Id = Team.getTeamIdByName(match.getTeam1Name(), teams);
      if (team1Id == null) {
        throw new TeamNotFoundException(match.getTeam1Name());
      }
      var team2Id = Team.getTeamIdByName(match.getTeam2Name(), teams);
      if (team2Id == null) {
        throw new TeamNotFoundException(match.getTeam2Name());
      }
  
      var existMatch = matchRepository.findFirstByTournamentIdAndDateAndTeam1IdAndTeam2Id(
          tournamentId,
          match.getDate(),
          team1Id, 
          team2Id
      );
      log.info("(createMatches)existMatch: {}", existMatch);
      if (existMatch != null) {
        continue;
      }
      
      matchRepository.save(Match.of(
          tournamentId,
          match.getDate(),
          match.getStartTime(),
          match.getType(),
          team1Id,
          team2Id,
          match.getTeam1Goals(),
          match.getTeam2Goals(),
          match.getTeam1Pens(),
          match.getTeam2Pens()
      ));
    }
  }
  
  @Override
  public void postMatches() {
    var matches = matchRepository.findAllBySlackMessageIdIsNullOrderByDateAscStartTimeAsc();
    var teams = teamRepository.findAll();
    log.info("(postMatches)teams: {}", teams.size());
    
    var currentDate = Integer.parseInt(DateUtil.toString(new Date(), "yyyyMMdd"));
    log.info("(postMatches)currentDate: {}", currentDate);
    
    for (var match: matches) {
      log.info("(postMatches)match dat: {}, detail: {}", match.getDate(), match);
      if (currentDate > match.getDate()) {
        continue;
      }
      
      var team1Name = Team.getTeamName(match.getTeam1Id(), teams);
      var team2Name = Team.getTeamName(match.getTeam2Id(), teams);
      var choices = new HashMap<String, String>();
      choices.put(team1Name, match.getId() + "_" + match.getTeam1Id());
      choices.put(team2Name, match.getId() + "_" + match.getTeam2Id());
      if (enableDraw) {
        choices.put("Hòa", match.getId() + "_" + Match.DRAW_RESULT);
      }
      
      var messageText = match.getDateString() + " " + match.getTimeString() + " - " + team1Name + " vs " + team2Name;
      var messageTs = slackService.publishMatch(
          channelName,
          messageText,
          choices
      );
  
      match.setSlackMessageId(messageTs);
      matchRepository.save(match);
    }
    
  }
  
  @Override
  public void updateMatchesResult() {
    log.info("(updateMatchesResult)");
    var matches = crawlService.crawlMatch();
    log.info("(updateMatchesResult)matches: {}", matches.size());
    var teams = teamRepository.findAll();
    log.info("(updateMatchesResult)teams: {}", teams.size());
  
    for (var match: matches) {
      log.info("(updateMatchesResult)match: {}", match);
      if (match.getTeam1Goals() < 0 || match.getTeam2Goals() < 0) {
        continue;
      }
      
      var team1Id = Team.getTeamIdByName(match.getTeam1Name(), teams);
      if (team1Id == null) {
        throw new TeamNotFoundException(match.getTeam1Name());
      }
      var team2Id = Team.getTeamIdByName(match.getTeam2Name(), teams);
      if (team2Id == null) {
        throw new TeamNotFoundException(match.getTeam2Name());
      }
  
      var existMatch = matchRepository.findFirstByTournamentIdAndDateAndTeam1IdAndTeam2Id(
          tournamentId,
          match.getDate(),
          team1Id,
          team2Id
      );
      log.info("(updateMatchesResult)existMatch: {}", existMatch);
      if (existMatch != null && existMatch.getResult() == null) {
        existMatch.setTeam1Goals(match.getTeam1Goals());
        existMatch.setTeam2Goals(match.getTeam2Goals());
        existMatch.setTeam1Pens(match.getTeam1Pens());
        existMatch.setTeam2Pens(match.getTeam2Pens());
        
        // not draw in main time
        if (match.getTeam1Goals() > match.getTeam2Goals()) {
          existMatch.setResult(existMatch.getTeam1Id());
        }
        if (match.getTeam1Goals() < match.getTeam2Goals()) {
          existMatch.setResult(existMatch.getTeam2Id());
        }
        
        // draw in main time
        if (match.getTeam1Goals() == match.getTeam2Goals()) {
          if (match.getTeam1Pens() > match.getTeam2Pens()) {
            existMatch.setResult(existMatch.getTeam1Id());
          }
          
          if (match.getTeam1Pens() < match.getTeam2Pens()) {
            existMatch.setResult(existMatch.getTeam2Id());
          }
  
          if (match.getTeam1Pens() == match.getTeam2Pens()) {
            existMatch.setResult(Match.DRAW_RESULT);
          }
        }
        matchRepository.save(existMatch);
      }
    }
  }
}
