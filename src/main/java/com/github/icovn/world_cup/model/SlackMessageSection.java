package com.github.icovn.world_cup.model;

import lombok.Data;

@Data
public class SlackMessageSection {
  
  private String text = "";
  private String image = "";
  
  public static SlackMessageSection of(String text) {
    var slackMessage = new SlackMessageSection();
    slackMessage.setText(text);
    return slackMessage;
  }
  
  public static SlackMessageSection of(String text, String image) {
    var slackMessage = SlackMessageSection.of(text);
    slackMessage.setImage(image);
    return slackMessage;
  }
}
