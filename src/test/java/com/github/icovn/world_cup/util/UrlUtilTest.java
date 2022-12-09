package com.github.icovn.world_cup.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UrlUtilTest {
  
  @Test
  public void getParamValue() {
    var input = "token=XMBUls6hUUJtbKZN1wbRWpeC&team_id=T025KFE3DGA&team_domain=edupiatutor&channel_id=C04CHGE3ADB&channel_name=test-world-cup&user_id=U025CNW6RNJ&user_name=huynq&command=/test-leader-board&text=&api_app_id=A04CG3LCHLP&is_enterprise_install=false&response_url=https://hooks.slack.com/commands/T025KFE3DGA/4515343897408/jqY5fGcDJyUCczPrsYvrA5js&trigger_id=4477099242215.2189524115554.6f3f92f647cd12f9510a54000236f583";
    assertEquals("XMBUls6hUUJtbKZN1wbRWpeC", UrlUtil.getParamValue(input, "token"));
    assertEquals("T025KFE3DGA", UrlUtil.getParamValue(input, "team_id"));
    assertEquals("4477099242215.2189524115554.6f3f92f647cd12f9510a54000236f583", UrlUtil.getParamValue(input, "trigger_id"));
  }
}