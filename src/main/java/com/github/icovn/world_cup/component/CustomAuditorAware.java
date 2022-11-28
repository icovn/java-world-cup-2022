package com.github.icovn.world_cup.component;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

@Slf4j
public class CustomAuditorAware implements AuditorAware<String> {
  
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of("SYSTEM_ID");
  }
}
