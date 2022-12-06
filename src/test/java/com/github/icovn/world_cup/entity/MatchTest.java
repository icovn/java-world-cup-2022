package com.github.icovn.world_cup.entity;

import junit.framework.TestCase;

public class MatchTest extends TestCase {
  
  /**
   * 2h 07/12/2022
   */
  public void testGetStartTimeInTimestamp() {
    var match = new Match();
    match.setDate(20221207);
    match.setStartTime(120);
    assertEquals(1670353200000L, match.getStartTimeInTimestamp());
  }
  
  /**
   * 2h 07/12/2022
   */
  public void testGetStartTimeInTimestamp2() {
    var match = new Match();
    match.setDate(20221206);
    match.setStartTime(1320);
    assertEquals(1670338800000L, match.getStartTimeInTimestamp());
  }
}