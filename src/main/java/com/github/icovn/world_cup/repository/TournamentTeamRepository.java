package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.TournamentTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam, String> {
  
}
