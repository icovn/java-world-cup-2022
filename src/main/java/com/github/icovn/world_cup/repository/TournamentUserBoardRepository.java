package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.TournamentUserBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentUserBoardRepository extends JpaRepository<TournamentUserBoard, String> {
  
}
