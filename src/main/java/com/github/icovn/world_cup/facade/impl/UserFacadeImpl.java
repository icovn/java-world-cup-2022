package com.github.icovn.world_cup.facade.impl;

import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.MatchUserBet;
import com.github.icovn.world_cup.exception.MatchNotFoundException;
import com.github.icovn.world_cup.exception.TeamNotFoundException;
import com.github.icovn.world_cup.facade.UserFacade;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.MatchUserBetRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.repository.TournamentUserBoardRepository;
import com.github.icovn.world_cup.service.SlackService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {
  
  @Value("${application.default.world_cup_channel_name:test-world-cup}")
  private String channelName;
  
  @Value("${application.default.world_cup_tournament_id:2022_11_QATAR}")
  private String tournamentId;
  
  private final MatchRepository matchRepository;
  private final MatchUserBetRepository matchUserBetRepository;
  private final SlackService slackService;
  private final TeamRepository teamRepository;
  private final TournamentUserBoardRepository tournamentUserBoardRepository;
  
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
            userName + " đã bet: Hòa"
        );
      } else {
        var team = teamRepository.findById(existBet.getBet()).orElse(null);
        if (team == null) {
          throw new TeamNotFoundException(existBet.getBet());
        }
        slackService.replyMessage(
            channelName, 
            match.getSlackMessageId(), 
            userName + " đã bet: " + team.getName()
        );
      }
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
          userName + " bet: Hòa"
      );
    } else {
      var team = teamRepository.findById(bet).orElse(null);
      if (team == null) {
        throw new TeamNotFoundException(bet);
      }
      slackService.replyMessage(
          channelName,
          match.getSlackMessageId(),
          userName + " bet: " + team.getName()
      );
    }
  }
  
  @Override
  public void notifyNotBet() {
    
  }
  
  @Override
  public void updateScore() {
    
  }
  
  @Override
  public void viewLeaderBoard(@NonNull String userId) {
    log.info("(viewLeaderBoard)userId: {}", userId);
  }
  
  @Override
  public void viewMyBet(@NonNull String userId) {
    log.info("(viewMyBet)userId: {}", userId);
  }
}
