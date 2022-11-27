package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, String> {
  
}
