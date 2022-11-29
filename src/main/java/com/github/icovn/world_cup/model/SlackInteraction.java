package com.github.icovn.world_cup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class SlackInteraction {
  
  @JsonProperty("api_app_id")
  private String apiAppId;
  private List<SlackAction> actions;
  private SlackUser user;
}