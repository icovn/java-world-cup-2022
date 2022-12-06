package com.github.icovn.world_cup;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.component.CustomAuditorAware;
import com.github.icovn.world_cup.facade.MatchFacade;
import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.MessageChangedEvent;
import com.slack.api.model.event.MessageDeletedEvent;
import com.slack.api.model.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@Slf4j
@SpringBootApplication
public class WorldCupApplication implements CommandLineRunner {
  
  @Value("${git.commit.id:}")
  private String commitId;
  @Value("${git.commit.message.short:}")
  private String commitMessage;
  @Value("${application.mode:}")
  private String mode;
  
  @Bean
  public AuditorAware<String> auditorAware() {
    return new CustomAuditorAware();
  }
  
  @Autowired private MatchFacade matchFacade;
  
  public static void main(String[] args) {
    SpringApplication.run(WorldCupApplication.class, args);
  }
  
  @Override
  public void run(String... args) {
    log.info("(run)commit id: {}, message: {} .....", commitId, commitMessage);
  
    try {
      if (mode.equals("CREATE_MATCHES")) {
        matchFacade.createMatches();
      }
      if (mode.equals("POST_MATCHES")) {
        matchFacade.postMatches();
      }
      
      initSlack();
    } catch (Exception ex) {
      log.error("(run)ex: {}", getFullStackTrace(ex));
    }
  }
  
  private void initSlack() throws Exception {
    var app = new App();
  
    app.event(MessageChangedEvent.class, (payload, ctx) -> {
      log.info(
          "(initSlack)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId()
      );
      return ctx.ack();
    });
  
    app.event(MessageDeletedEvent.class, (payload, ctx) -> {
      log.info(
          "(initSlack)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId()
      );
      return ctx.ack();
    });
  
    app.event(MessageEvent.class, (payload, ctx) -> {
      log.info(
          "(initSlack)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId()
      );
      return ctx.ack();
    });
  
    var server = new SlackAppServer(app);
    server.start();
  }
}
