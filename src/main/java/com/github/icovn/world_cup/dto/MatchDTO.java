package com.github.icovn.world_cup.dto;

import com.github.icovn.world_cup.constant.MatchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@Data
@NoArgsConstructor
public class MatchDTO {
  
  private MatchType type;
  
  private int date = 0;
  private int startTime = 0;
  private int team1Goals = -1;
  private int team2Goals = -1;
  
  private String team1Name;
  private String team2Name;
}
