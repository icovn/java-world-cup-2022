package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
  
}
