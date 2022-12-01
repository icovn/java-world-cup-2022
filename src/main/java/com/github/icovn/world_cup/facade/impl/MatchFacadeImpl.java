package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.util.DateUtil;
import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.Team;
import com.github.icovn.world_cup.exception.TeamNotFoundException;
import com.github.icovn.world_cup.facade.MatchFacade;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.repository.TournamentRepository;
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
      var team1Id = Team.getTeamId(match.getTeam1Name(), teams);
      if (team1Id == null) {
        throw new TeamNotFoundException(match.getTeam1Name());
      }
      var team2Id = Team.getTeamId(match.getTeam2Name(), teams);
      if (team2Id == null) {
        throw new TeamNotFoundException(match.getTeam2Name());
      }
  
      var existMatch = matchRepository.findFirstByTournamentIdAndDateAndTeam1IdAndTeam2Id(
          tournamentId,
          match.getDate(),
          team1Id, 
          team2Id
      );
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
          match.getTeam2Goals()
      ));
    }
  }
  
  @Override
  public void postMatches() {
    var matches = matchRepository.findAllBySlackMessageIdIsNullOrderByDateAscStartTimeAsc();
    var teams = teamRepository.findAll();
    log.info("(createMatches)teams: {}", teams.size());
    
    var currentDate = Integer.parseInt(DateUtil.toString(new Date(), "yyyyMMdd"));
    log.info("(createMatches)currentDate: {}", currentDate);
    
    for (var match: matches) {
      if (currentDate >= match.getDate()) {
        continue;
      }
      
      var team1Name = Team.getTeamName(match.getTeam1Id(), teams);
      var team2Name = Team.getTeamName(match.getTeam2Id(), teams);
      var choices = new HashMap<String, String>();
      choices.put(team1Name, match.getId() + "_" + match.getTeam1Id());
      choices.put(team2Name, match.getId() + "_" + match.getTeam2Id());
      choices.put("Hòa", match.getId() + "_" + Match.DRAW_RESULT);
      
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
    
  }
}
