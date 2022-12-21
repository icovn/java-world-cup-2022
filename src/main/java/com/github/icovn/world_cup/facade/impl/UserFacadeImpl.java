package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.util.MapperUtil;
import com.github.icovn.world_cup.constant.BetStatus;
import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.MatchUserBet;
import com.github.icovn.world_cup.exception.MatchNotFoundException;
import com.github.icovn.world_cup.exception.TeamNotFoundException;
import com.github.icovn.world_cup.facade.UserFacade;
import com.github.icovn.world_cup.model.SlackMessageSection;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.MatchUserBetRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.repository.UserRepository;
import com.github.icovn.world_cup.service.SlackService;
import com.github.icovn.world_cup.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {
  
  @Value("${application.default.bet_right_image_url:https://t4.ftcdn.net/jpg/02/02/78/81/360_F_202788149_9sndfcPPme9zRtstjROSmyLFma2UMYaM.jpg}")
  private String betRight;
  
  @Value("${application.default.bet_wrong_image_url:https://as2.ftcdn.net/v2/jpg/02/01/83/17/1000_F_201831763_OhQZdsAmuHkcBLpCFQvL7vznoKSJpcD3.jpg}")
  private String betWrong;
  
  @Value("${application.default.bet_match_not_start_image_url:https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Blue_question_mark_icon.svg/2048px-Blue_question_mark_icon.svg.png}")
  private String betMatchNotStart;
  
  @Value("${application.default.world_cup_channel_name:test-world-cup}")
  private String channelName;
  
  private final MatchRepository matchRepository;
  private final MatchUserBetRepository matchUserBetRepository;
  private final SlackService slackService;
  private final TeamRepository teamRepository;
  private final UserRepository userRepository;
  
  @Override
  public void bet(
      @NonNull String matchId, 
      @NonNull String userId,
      @NonNull String userName,
      @NonNull String bet
  ) {
    log.info("(bet)matchId: {}, userId: {}, userName: {}, bet: {}", matchId, userId, userName, bet);
    
    // get match information
    var match = matchRepository.findById(matchId).orElse(null);
    if (match == null) {
      throw new MatchNotFoundException(matchId);
    }
    
    // check for exist bet
    var existBet = matchUserBetRepository.findFirstByMatchIdAndUserId(matchId, userId);
    if (existBet != null) {
      // notify user for exist bet
      if (existBet.getBet().equals(Match.DRAW_RESULT)) {
        slackService.replyMessage(
            channelName, 
            match.getSlackMessageId(),
            "*" + userName + "* đã bet: `Hòa`"
        );
      } else {
        var team = teamRepository.findById(existBet.getBet()).orElse(null);
        if (team == null) {
          throw new TeamNotFoundException(existBet.getBet());
        }
        slackService.replyMessage(
            channelName, 
            match.getSlackMessageId(),
            "*" + userName + "* đã bet: `" + team.getName() + "`"
        );
      }
      return;
    }
    
    if (System.currentTimeMillis() >= match.getStartTimeInTimestamp()) {
      // make a new invalid bet
      matchUserBetRepository.save(MatchUserBet.of(
          matchId, userId, BetStatus.LATE_BET
      ));
  
      slackService.replyMessage(
          channelName,
          match.getSlackMessageId(),
          ":thumbsdown: *" + userName + "* bet: `Muộn`"
      );
      
      return;
    }
    
    // make a new bet
    matchUserBetRepository.save(MatchUserBet.of(
        matchId, userId, bet
    ));
    
    // notify user for new bet
    if (bet.equals(Match.DRAW_RESULT)) {
      slackService.replyMessage(
          channelName,
          match.getSlackMessageId(),
          ":thumbsup: *" + userName + "* bet: `Hòa`"
      );
    } else {
      var team = teamRepository.findById(bet).orElse(null);
      if (team == null) {
        throw new TeamNotFoundException(bet);
      }
      slackService.replyMessage(
          channelName,
          match.getSlackMessageId(),
          ":thumbsup: *" + userName + "* bet: `" + team.getName() + "`"
      );
    }
  }
  
  @Override
  public void notifyNotBet() {
    log.info("(notifyNotBet)");
  }
  
  @Override
  public void updateScore() {
    log.info("(updateScore)");
    var matches = matchRepository.findAll();
    log.info("(updateScore)matches: {}", matches.size());
    var userBets = matchUserBetRepository.findAllByResultLessThan(0);
    log.info("(updateScore)userBets: {}", userBets.size());
    
    for (var userBet: userBets) {
      log.info("(updateScore)userBet: {}", userBet);
      var match = Match.getMatch(userBet.getMatchId(), matches);
      if (match == null) {
        log.warn("(updateScore)userBet: {} --> MATCH_NOT_FOUND", userBet);
        continue;
      }
      if (match.getResult() == null) {
        log.warn("(updateScore)match: {} --> MATCH_NOT_FINISHED", userBet);
        continue;
      }

      if (userBet.getStatus().equals(BetStatus.LATE_BET)) {
        userBet.setResult(0);
      } else {
        if (userBet.getBet().equals(match.getResult())) {
          userBet.setResult(1);
        } else {
          userBet.setResult(0);
        }
      }

      log.info("(updateScore)userBet: {}, match: {}", userBet.getBet(), match.getResult());
      matchUserBetRepository.save(userBet);
    }
  }
  

  @Async
  @Override
  public void viewLeaderBoard(@NonNull String channelName) {
    log.info("(viewLeaderBoard)channelName: {}", channelName);
    
  }

  @Async
  @Override
  public void viewMyBet(@NonNull String userId, @NonNull String channelName) {
    log.info("(viewMyBet)userId: {}, channelName: {}", userId, channelName);
    
    var user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      log.error("(viewMyBet)userId: {} --> NOT_EXIST", userId);
      return;
    }
    
    var userBets = matchUserBetRepository.findHistory(userId);
    log.info("(viewMyBet)userBets: {}", userBets.size());
    
    var index = 1;
    var numberOfBetRight = 0;
    var sections = new ArrayList<SlackMessageSection>();
    for (var userBet: userBets) {
      log.debug("(viewMyBet)userBet: {}", MapperUtil.toJson(userBet));
      if (userBet.getDate() == null) {
        continue;
      }
      
      var matchResult = "Hòa";
      if (!Objects.equals(userBet.getMatchResult(), Match.DRAW_RESULT)) {
        if (Objects.equals(userBet.getMatchResult(), userBet.getTeam1Id())) {
          matchResult = userBet.getTeam1Name() + " thắng";
        }
        if (Objects.equals(userBet.getMatchResult(), userBet.getTeam2Id())) {
          matchResult = userBet.getTeam2Name() + " thắng";
        }
      }
  
      var userBetResult = "Hòa";
      if (!Objects.equals(userBet.getUserBet(), Match.DRAW_RESULT)) {
        if (Objects.equals(userBet.getUserBet(), userBet.getTeam1Id())) {
          userBetResult = userBet.getTeam1Name() + " thắng";
        }
        if (Objects.equals(userBet.getUserBet(), userBet.getTeam2Id())) {
          userBetResult = userBet.getTeam2Name() + " thắng";
        }
      }
      
      var text = String.format(
          "*%s. %s %s - %s vs %s*\n- kết quả: `%s`\n- đã bet: `%s` lúc %s",
          index,
          DateTimeUtil.getDateString(userBet.getDate()),
          DateTimeUtil.getTimeString(userBet.getStartTime()),
          userBet.getTeam1Name(),
          userBet.getTeam2Name(),
          matchResult,
          userBetResult,
          DateTimeUtil.convertTimestampToString(userBet.getBetAt())
      );
      
      var betImage = betMatchNotStart;
      if (userBet.getBetResult() == 0) {
        betImage = betWrong;
      }
      if (userBet.getBetResult() > 0) {
        betImage = betRight;
        numberOfBetRight++;
      }
      
      sections.add(SlackMessageSection.of(text, betImage));
      index++;
    }
    
    var title = "Lịch sử cược của *" + user.getFullName() + "*: " + numberOfBetRight + " trận đúng";
    slackService.publishMessage(channelName, title, sections);
  }
}
