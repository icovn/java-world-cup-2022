package com.github.icovn.world_cup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class WorldCupApplication implements CommandLineRunner {
  
  @Value("${git.commit.id:}")
  private String commitId;
  @Value("${git.commit.message.short:}")
  private String commitMessage;
  
  public static void main(String[] args) {
    SpringApplication.run(WorldCupApplication.class, args);
  }
  
  @Override
  public void run(String... args) {
    log.info("(run)commit id: {}, message: {} .....", commitId, commitMessage);
  }
}
