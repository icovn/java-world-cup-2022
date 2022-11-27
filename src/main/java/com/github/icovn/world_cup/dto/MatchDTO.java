package com.github.icovn.world_cup.dto;

import com.github.icovn.world_cup.constant.MatchType;
import lombok.Data;

@Data
public class MatchDTO {
  
  private MatchType type;
  
  private int date = 0;
  private int startTime = 0;
  private int team1Goals = 0;
  private int team2Goals = 0;
  
  private String team1Code;
  private String team2Code;
}
