package com.github.icovn.world_cup.exception;

public class TeamNotFoundException extends RuntimeException {
  
  public TeamNotFoundException(String message) {
    super(message);
  }
}
