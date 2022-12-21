package com.github.icovn.world_cup_test;

import com.github.icovn.world_cup.model.SlackMessageSection;
import com.github.icovn.world_cup.service.CrawlService;
import com.github.icovn.world_cup.service.SlackService;
import com.github.icovn.world_cup_test.component.InitDataComponent;
import com.github.icovn.world_cup_test.component.ProcessOldDataFromSlack;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
    "com.github.icovn.world_cup.service.*",
    "com.github.icovn.world_cup.facade.*"
}, basePackageClasses = {
    InitDataComponent.class,
    ProcessOldDataFromSlack.class
})
@EnableJpaRepositories(basePackages = {"com.github.icovn.world_cup.repository"})
@EntityScan(basePackages = {"com.github.icovn.world_cup.entity"})
@Slf4j
@SpringBootApplication
public class WorldCupTestApplication implements CommandLineRunner {
  
  @Value("${git.commit.id:}")
  private String commitId;
  @Value("${git.commit.message.short:}")
  private String commitMessage;
  
  @Autowired private CrawlService crawlService;
  @Autowired private InitDataComponent initDataComponent;
  @Autowired private ProcessOldDataFromSlack processOldDataFromSlack;
  @Autowired private SlackService slackService;
  
  public static void main(String[] args) {
    SpringApplication.run(WorldCupTestApplication.class, args);
  }
  
  @Override
  public void run(String... args) {
    log.info("(run)commit id: {}, message: {} .....", commitId, commitMessage);
  
//    initDataComponent.init();
//    processOldDataFromSlack.loadUsers();
//    processOldDataFromSlack.loadUserBets();
  
//    testCrawlMatches();
//    testFilterMessages(false);
//    testGetUsers();
//    testSendLeaderBoard();
//    testSendUserBetHistories();
  }
  
  private void testCrawlMatches() {
    var matches = crawlService.crawlMatch();
    for (var match: matches) {
      log.info("(testCrawl)match: {}", match);
    }
    log.info("(testCrawl)matches: {}", matches.size());
  }
  
  private void testCrawlTeams() {
    var teamNames = crawlService.crawlTeams();
    log.info("(testCrawl)teamNames: {}", teamNames);
  }
  
  private void testFilterMessages(boolean willGetReplies) {
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
    
      if (!willGetReplies) {
        continue;
      }
      var replies = slackService.getReplies(channelId, message.getTs());
      for (var reply: replies) {
        log.info(
            "(run)user: {}, text: {}, ts: {}", reply.getUser(), reply.getText(), reply.getTs()
        );
      }
    }
  }
  
  private void testGetUsers() {
    var users = slackService.getUsers();
    for (var user: users) {
      log.info("(testGetUsers)user: {}", user);
    }
  }
  
  private void testSendLeaderBoard() {
    var lines = List.of(
        "*1. huynq* - 3 trận đúng\n",
        "*2. phuongdv* - 5 trận đúng\n",
        "*3. dattv* - 6 trận đúng\n"
    );
    slackService.publishMessage(
        "test-world-cup", 
        "Leader board", 
        lines.stream().map(SlackMessageSection::of).collect(Collectors.toList())
    );
  }
  
  private void testSendUserBetHistories() {
    var lines = List.of(
        "*1. 03/12/2022 (2h) - Serbia vs Thụy Sĩ*\n- kết quả: `Serbia thắng`\n- đã bet: `Serbia` lúc 19:30 02/12/2022\n- `sai`",
        "*2. 03/12/2022 (2h) - Serbia vs Thụy Sĩ*\n- kết quả: `Serbia thắng`\n- đã bet: `Serbia` lúc 19:30 02/12/2022\n- `đúng`"
    );
    slackService.publishMessage(
        "test-world-cup", 
        "Lịch sử cược của *HuyNQ*: 12 trận đúng",
        lines.stream().map(
            o -> SlackMessageSection.of(
                o, 
                "https://as2.ftcdn.net/v2/jpg/02/01/83/17/1000_F_201831763_OhQZdsAmuHkcBLpCFQvL7vznoKSJpcD3.jpg"
            )
        ).collect(Collectors.toList())
    );
  }
  
  private void testSendMessage() {
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
    // publish rich layout message
    var choices = new HashMap<String, String>();
    choices.put("Switzerland", "Match1_Switzerland");
    choices.put("Cameroon", "Match1_Cameroon");
    slackService.publishMatch(
        "test-world-cup",
        "24/11/2022 (17h) - Bảng G: Switzerland vs Cameroon",
        choices
    );
  }
}
