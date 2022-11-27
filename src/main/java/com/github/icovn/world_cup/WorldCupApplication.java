package com.github.icovn.world_cup;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.view.Views.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.event.MessageChangedEvent;
import com.slack.api.model.event.MessageDeletedEvent;
import com.slack.api.model.event.MessageEvent;
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
  
    try {
      var app = new App();
  
      app.event(MessageChangedEvent.class, (payload, ctx) -> {
        log.info("(run)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId());
        return ctx.ack();
      });
  
      app.event(MessageDeletedEvent.class, (payload, ctx) -> {
        log.info("(run)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId());
        return ctx.ack();
      });
      
      app.event(MessageEvent.class, (payload, ctx) -> {
        log.info("(run)message payload: {}, event id: {}", payload.getEvent(), payload.getEventId());
        return ctx.ack();
      });
  
      app.event(AppHomeOpenedEvent.class, (payload, ctx) -> {
        var appHomeView = view(view -> view
            .type("home")
            .blocks(asBlocks(
                section(section -> section.text(markdownText(mt -> mt.text("*Welcome to your _App's Home_* :tada:")))),
                divider(),
                section(section -> section.text(markdownText(mt -> mt.text("This button won't do much for now but you can set up a listener for it using the `actions()` method and passing its unique `action_id`. See an example on <https://slack.dev/java-slack-sdk/guides/interactive-components|slack.dev/java-slack-sdk>.")))),
                actions(actions -> actions
                    .elements(asElements(
                        button(b -> b.text(plainText(pt -> pt.text("Click me!"))).value("button1").actionId("button_1"))
                    ))
                )
            ))
        );
    
        var res = ctx.client().viewsPublish(r -> r
            .userId(payload.getEvent().getUser())
            .view(appHomeView)
        );
    
        return ctx.ack();
      });
      
      var server = new SlackAppServer(app);
      server.start();
    } catch (Exception ex) {
      log.error("(run)ex: {}", getFullStackTrace(ex));
    }
  }
}
