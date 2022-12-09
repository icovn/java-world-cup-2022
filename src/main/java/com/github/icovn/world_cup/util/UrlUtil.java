package com.github.icovn.world_cup.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlUtil {
  
  public static String getParamValue(String input, String param) {
    log.debug("(getParamValue)input: {}, param: {}", input, param);
    if(input.isBlank() || param.isBlank()) {
      return null;
    }
    
    var topParts = input.split(param + "=");
    if (topParts.length < 2) {
      return null;
    }
    
    var subParts = topParts[1].split("&");
    if (subParts.length < 2) {
      return topParts[1];
    }
    
    return subParts[0];
  }
}
