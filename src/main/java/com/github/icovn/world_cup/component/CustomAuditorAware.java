package com.github.icovn.world_cup.component;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class CustomAuditorAware implements AuditorAware<String> {
  
  @Override
  public Optional<String> getCurrentAuditor() {
    String userId = "SYSTEM_ID";
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }
    log.debug("(getCurrentAuditor)userId: {}", userId);
    
    return Optional.of(userId);
  }
}
