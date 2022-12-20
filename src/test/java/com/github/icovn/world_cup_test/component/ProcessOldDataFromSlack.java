package com.github.icovn.world_cup_test.component;

import com.github.icovn.world_cup.constant.BetStatus;
import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.MatchUserBet;
import com.github.icovn.world_cup.entity.Team;
import com.github.icovn.world_cup.entity.User;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.MatchUserBetRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.repository.UserRepository;
import com.github.icovn.world_cup.service.SlackService;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessOldDataFromSlack {

  @Autowired
  private MatchUserBetRepository matchUserBetRepository;

  private final MatchRepository matchRepository;
  private final SlackService slackService;
  private final TeamRepository teamRepository;
  private final UserRepository userRepository;
  
  public void loadUsers() {
    var users = slackService.getUsers();
    for (var user: users) {
      var existUser = userRepository.findById(user.getId()).orElse(null);
      if (existUser == null) {
        var userFullName = user.getProfile().getDisplayName();
        if (userFullName.isBlank()) {
          userFullName = user.getName();
        }
        userRepository.save(User.of(user.getId(), userFullName));
      }
    }
  }
  
  public void loadUserBets() {
    log.info("(loadUserBets)");
    
    var teams = teamRepository.findAll();
    log.info("(loadUserBets)teams: {}", teams.size());
    
    // find public channel
    var channelId = slackService.findConversation("world-cup-2022", false);
  
    // get channel's messages
    var messages = slackService.getMessages(channelId);
    log.info("(loadUserBets)messages: {}", messages.size());
    
    for (var message: messages) {
      if (!message.getText().contains("11/2022")) {
        continue;
      }
  
      log.info("(loadUserBets)text: {}, ts: {}", message.getText(), message.getTs());
      var match = matchRepository.findFirstBySlackMessageId(message.getTs());
      if (match == null)  {
        log.warn("(loadUserBets)ts: {}, MATCH_NOT_EXIST", message.getTs());
        continue;
      }
      log.info(
          "(loadUserBets)match team1: {}, team2: {}, validTime: {}", 
          match.getTeam1Id(), match.getTeam2Id(), match.getStartTimeInTimestamp()
      );
      
      var replies = slackService.getReplies(channelId, message.getTs());
      for (var reply: replies) {
        try {
          var isValid = Long.parseLong(reply.getTs().split("\\.")[0]) < match.getStartTimeInTimestamp();
          var userBetTeam = Team.getTeamIdByCode(reply.getText(), teams);
          log.info(
              "(loadUserBets)user: {}, text: {}, ts: {}, isValid: {}, userBetTeam: {}",
              reply.getUser(), reply.getText(), reply.getTs(), isValid, userBetTeam
          );

          if (userBetTeam == null) {
            if (reply.getText().equalsIgnoreCase("hoà") || reply.getText().equalsIgnoreCase("hòa")) {
              userBetTeam = Match.DRAW_RESULT;
            } else {
              log.warn("(loadUserBets)team: {}, TEAM_NOT_EXIST", reply.getText());
              continue;
            }
          }

          MatchUserBet matchUserBet;
          if (isValid) {
            matchUserBet = MatchUserBet.of(
                match.getId(), reply.getUser(), userBetTeam
            );
          } else {
            matchUserBet = MatchUserBet.of(
                match.getId(), reply.getUser(), BetStatus.LATE_BET
            );
          }
          matchUserBetRepository.save(matchUserBet);
          log.info("(loadUserBets)matchUserBet: {}", matchUserBet);
        } catch (Exception ex) {
          log.warn("(loadUserBets)ex: {}", ex.getMessage());
        }
      }
    }
  }
}
