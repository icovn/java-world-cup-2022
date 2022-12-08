package com.github.icovn.world_cup_test.component;

import com.github.icovn.world_cup.entity.Match;
import com.github.icovn.world_cup.entity.Team;
import com.github.icovn.world_cup.entity.Tournament;
import com.github.icovn.world_cup.entity.TournamentTeam;
import com.github.icovn.world_cup.repository.MatchRepository;
import com.github.icovn.world_cup.repository.TeamRepository;
import com.github.icovn.world_cup.repository.TournamentRepository;
import com.github.icovn.world_cup.repository.TournamentTeamRepository;
import com.github.icovn.world_cup.service.CrawlService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDataComponent {
  
  private static final String TOURNAMENT_NAME = "WORLD CUP 2022";
  
  private final CrawlService crawlService;
  private final MatchRepository matchRepository;
  private final TeamRepository teamRepository;
  private final TournamentRepository tournamentRepository;
  private final TournamentTeamRepository tournamentTeamRepository;
  
  private final List<Team> teams = List.of(
      Team.of("Argentina","Argentina"),
      Team.of("Australia","Australia"),
      Team.of("Belgium","Bỉ"),
      Team.of("Brazil","Brazil"),
      Team.of("Cameroon","Cameroon"),
      Team.of("Canada","Canada"),
      Team.of("Costa Rica","Costa Rica"),
      Team.of("Croatia","Croatia"),
      Team.of("Denmark","Đan Mạch"),
      Team.of("Ecuador","Ecuador"),
      Team.of("England","Anh"),
      Team.of("France","Pháp"),
      Team.of("Germany","Đức"),
      Team.of("Ghana","Ghana"),
      Team.of("Iran","Iran"),
      Team.of("Japan","Nhật Bản"),
      Team.of("Mexico","Mexico"),
      Team.of("Morocco","Morocco"),
      Team.of("Netherlands","Hà Lan"),
      Team.of("Poland","Ba Lan"),
      Team.of("Portugal","Bồ Đào Nha"),
      Team.of("Qatar","Qatar"),
      Team.of("Saudi Arabia","Saudi Arabia"),
      Team.of("Senegal","Senegal"),
      Team.of("Serbia","Serbia"),
      Team.of("South Korea","Hàn Quốc"),
      Team.of("Spain","Tây Ban Nha"),
      Team.of("Switzerland","Thụy Sĩ"),
      Team.of("Tunisia","Tunisia"),
      Team.of("Uruguay","Uruguay"),
      Team.of("USA","Mỹ"),
      Team.of("Wales","Wales")
  );
  
  @Value("${application.default.tournament_id:world_cup_2022}")
  private String tournamentId;
  
  @Value("${application.will_clear_data:false}")
  private Boolean willClearData;
  
  public void init(){
    if (willClearData) {
      matchRepository.deleteAll();
      tournamentTeamRepository.deleteAll();
      tournamentRepository.deleteAll();
      teamRepository.deleteAll();
    }
    
    var countTeams = teamRepository.count();
    if (countTeams > 0) {
      return;
    }
    
    // create teams
    var savedTeams = teamRepository.saveAll(teams);
  
    // create tournament
    var tournament = Tournament.of(tournamentId, TOURNAMENT_NAME);
    tournamentRepository.save(tournament);
    
    // create tournament's teams
    for (var team: savedTeams) {
      var tournamentTeam = TournamentTeam.of(tournamentId, team.getId());
      tournamentTeamRepository.save(tournamentTeam);
    }
    
    var matches = crawlService.crawlMatch();
    for (var match: matches) {
      log.debug("(testCrawl)match: {}", match);
      
      var team1Id = Team.getTeamIdByName(match.getTeam1Name(), savedTeams);
      log.debug("(testCrawl)team1Id: {}", team1Id);
      
      var team2Id = Team.getTeamIdByName(match.getTeam2Name(), savedTeams);
      log.debug("(testCrawl)team2Id: {}", team2Id);
      
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
  
  
}
