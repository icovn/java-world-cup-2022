package com.github.icovn.world_cup_test;

import com.github.icovn.world_cup.service.impl.SlackServiceImpl;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class WorldCupTestApplication implements CommandLineRunner {
  
  @Value("${git.commit.id:}")
  private String commitId;
  @Value("${git.commit.message.short:}")
  private String commitMessage;
  
  public static void main(String[] args) {
    SpringApplication.run(WorldCupTestApplication.class, args);
  }
  
  @Override
  public void run(String... args) {
    log.info("(run)commit id: {}, message: {} .....", commitId, commitMessage);
  
    testSendMessageWithRichLayout();
  }
  
  private void testFilterMessages() {
    var slackService = new SlackServiceImpl();
    
    // find public channel
    var channelId = slackService.findConversation("world-cup-2022", false);
  
    // get channel's messages
    var messages = slackService.getMessages(channelId);
    log.info("(run)messages: {}", messages.size());
    for (var message: messages) {
      if (!message.getText().contains("11/2022")) {
        continue;
      }
    
      var user = slackService.findUser(message.getUser());
      log.info(
          "(run)user: {}, text: {}, ts: {}", user.getName(), message.getText(), message.getTs()
      );
    
      var replies = slackService.getReplies(channelId, message.getTs());
      for (var reply: replies) {
        log.info(
            "(run)user: {}, text: {}, ts: {}", reply.getUser(), reply.getText(), reply.getTs()
        );
      }
    }
  }
  
  private void testSendMessage() {
    var slackService = new SlackServiceImpl();
    
    // find private channel
    var channelId = slackService.findConversation("test-world-cup", true);
    
    // publish message
    var messageTs = slackService.publishMessage("test-world-cup", "test");
    
    // reply to published message
    var replyTs = slackService.replyMessage("test-world-cup", messageTs, "test reply");
    
    // reply to replied message
    slackService.replyMessage("test-world-cup", replyTs, "test reply2");
  }
  
  private void testSendMessageWithRichLayout() {
    var slackService = new SlackServiceImpl();
  
    // publish rich layout message
    var choices = new HashMap<String, String>();
    choices.put("Switzerland", "Switzerland");
    choices.put("Cameroon", "Cameroon");
    slackService.publishMessage(
        "test-world-cup",
        "24/11/2022 (17h) - Bảng G: Switzerland vs Cameroon",
        choices
    );
  }
}
