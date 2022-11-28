package com.github.icovn.world_cup.service.impl;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import com.github.icovn.world_cup.exception.FindConversationFailed;
import com.github.icovn.world_cup.exception.FindUserFailed;
import com.github.icovn.world_cup.exception.GetMessagesFailed;
import com.github.icovn.world_cup.exception.GetRepliesFailed;
import com.github.icovn.world_cup.exception.GetUsersFailed;
import com.github.icovn.world_cup.exception.PublishMessageFailed;
import com.github.icovn.world_cup.exception.ReplyMessageFailed;
import com.github.icovn.world_cup.service.SlackService;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.ConversationType;
import com.slack.api.model.Message;
import com.slack.api.model.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SlackServiceImpl implements SlackService {
  
  @Override
  public String findConversation(@NonNull String channelName, boolean isPrivate) {
    log.debug("(findConversation)channelName: {}, isPrivate: {}", channelName, isPrivate);
    var client = Slack.getInstance().methods();
    
    try {
      // find conversation
      ConversationsListResponse result;
      if (isPrivate) {
        result = client.conversationsList(r -> r
            .token(System.getenv("SLACK_BOT_TOKEN"))
            .types(List.of(
                ConversationType.PRIVATE_CHANNEL
            ))
        );
      } else {
        result = client.conversationsList(r -> r
            .token(System.getenv("SLACK_BOT_TOKEN"))
        );
      }
      log.trace("(findConversation)result: {}", result);
  
      // throw exception when get failed
      if (!result.isOk()) {
        throw new FindConversationFailed(result.getError());
      }
      
      for (var channel : result.getChannels()) {
        log.debug("(findConversation)channel id: {}, name: {}", channel.getId(), channel.getName());
        if (channel.getName().equals(channelName)) {
          var conversationId = channel.getId();
          log.debug("(findConversation)found conversation: {}", conversationId);
          return conversationId;
        }
      }
    } catch (SlackApiException | IOException ex) {
      log.error("(findConversation)ex: {}", getFullStackTrace(ex));
      throw new FindConversationFailed(ex.getMessage());
    }
    return null;
  }
  
  @Override
  public User findUser(@NonNull String userId) {
    log.debug("(findUser)userId: {}", userId);
    var client = Slack.getInstance().methods();
  
    try {
      var result = client.usersInfo(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .user(userId)
      );
      log.trace("(findUser)result: {}", result);
  
      // throw exception when get failed
      if (!result.isOk()) {
        throw new FindUserFailed(result.getError());
      }
      
      return result.getUser();
    } catch (SlackApiException | IOException ex) {
      log.error("(findConversation)ex: {}", getFullStackTrace(ex));
      throw new FindUserFailed(ex.getMessage());
    }
  }
  
  @Override
  public List<Message> getMessages(@NonNull String channelId) {
    log.debug("(getMessages)channelId: {}", channelId);
    var client = Slack.getInstance().methods();
    
    try {
      // get messages
      var result = client.conversationsHistory(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .channel(channelId)
      );
      log.trace("(getMessages)result: {}", result);
      
      // throw exception when get failed
      if (!result.isOk()) {
        throw new GetMessagesFailed(result.getError());
      }
      
      // return messages
      var messages = result.getMessages();
      if (messages == null) {
        return List.of();
      }
      return messages;
    } catch (IOException | SlackApiException ex) {
      log.error("(getMessages)ex: {}", getFullStackTrace(ex));
      throw new GetMessagesFailed(ex.getMessage());
    }
  }
  
  @Override
  public List<Message> getReplies(@NonNull String channelId, @NonNull String ts) {
    log.debug("(getReplies)channelId: {}, ts: {}", channelId, ts);
    var client = Slack.getInstance().methods();
  
    try {
      // get replies
      var result = client.conversationsReplies(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .channel(channelId)
          .ts(ts)
      );
      log.trace("(getReplies)result: {}", result);
    
      // throw exception when get failed
      if (!result.isOk()) {
        throw new GetRepliesFailed(result.getError());
      }
    
      // return messages
      var messages = result.getMessages();
      if (messages == null) {
        return List.of();
      }
      return messages;
    } catch (IOException | SlackApiException ex) {
      log.error("(getReplies)ex: {}", getFullStackTrace(ex));
      throw new GetRepliesFailed(ex.getMessage());
    }
  }
  
  @Override
  public List<User> getUsers() {
    log.debug("(getUsers)");
    var client = Slack.getInstance().methods();
    
    try {
      // get users
      var result = client.usersList(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
      );
      log.trace("(getUsers)result: {}", result);
      
      // throw exception when get failed
      if (!result.isOk()) {
        throw new GetUsersFailed(result.getError());
      }
      
      // return users
      var users = result.getMembers();
      if (users == null) {
        return List.of();
      }
      return users;
    } catch (IOException | SlackApiException ex) {
      log.error("(getUsers)ex: {}", getFullStackTrace(ex));
      throw new GetUsersFailed(ex.getMessage());
    }
  }
  
  @Override
  public String publishMessage(@NonNull String channelName, @NonNull String text) {
    log.info("(publishMessage)channelName: {}, text: {}", channelName, text);
    var client = Slack.getInstance().methods();
  
    try {
      var result = client.chatPostMessage(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .channel(channelName)
          .text(text)
      );
      log.info("(publishMessage)result: {}", result);
  
      // throw exception when get failed
      if (!result.isOk()) {
        throw new PublishMessageFailed(result.getError());
      }
      
      return result.getTs();
    } catch (SlackApiException | IOException ex) {
      log.error("(publishMessage)ex: {}", getFullStackTrace(ex));
      throw new PublishMessageFailed(ex.getMessage());
    }
  }
  
  @Override
  public String publishMessage(
      @NonNull String channelName, 
      @NonNull String text,
      Map<String, String> choices
  ) {
    log.info("(publishMessage)channelName: {}, text: {}, choices: {}}", channelName, text, choices);
    var client = Slack.getInstance().methods();
  
    try {
      var result = client.chatPostMessage(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .channel(channelName)
          .blocks(asBlocks(
              section(section -> section.text(markdownText(text))),
              divider(),
              actions(actions -> actions
                  .elements(
                      choices.entrySet().stream().map(o -> button(
                          b -> b.text(plainText(pt -> pt.emoji(true).text(o.getKey())))
                              .value(o.getValue())
                          )
                      ).collect(Collectors.toList())
                  )
              )
          ))
      );
      log.info("(publishMessage)result: {}", result);
    
      // throw exception when get failed
      if (!result.isOk()) {
        throw new PublishMessageFailed(result.getError());
      }
    
      return result.getTs();
    } catch (SlackApiException | IOException ex) {
      log.error("(publishMessage)ex: {}", getFullStackTrace(ex));
      throw new PublishMessageFailed(ex.getMessage());
    }
  }
  
  @Override
  public String replyMessage(
      @NonNull String channelName, 
      @NonNull String ts,
      @NonNull String text
  ) {
    log.info("(replyMessage)channelName: {}, ts: {}, text: {}", channelName, ts, text);
    var client = Slack.getInstance().methods();
  
    try {
      var result = client.chatPostMessage(r -> r
          .token(System.getenv("SLACK_BOT_TOKEN"))
          .channel(channelName)
          .threadTs(ts)
          .text(text)
      );
      log.info("(replyMessage)result: {}", result);
  
      // throw exception when get failed
      if (!result.isOk()) {
        throw new ReplyMessageFailed(result.getError());
      }
      
      return result.getTs();
    } catch (SlackApiException | IOException ex) {
      log.error("(replyMessage)ex: {}", getFullStackTrace(ex));
      throw new ReplyMessageFailed(ex.getMessage());
    }
  }
}
