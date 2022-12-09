package com.github.icovn.world_cup.service;

import com.github.icovn.world_cup.model.SlackMessageSection;
import com.slack.api.model.Message;
import com.slack.api.model.User;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

public interface SlackService {
  
  /**
   * Find public/private channel by channel's name
   * @param channelName Channel's name
   * @param isPrivate Is private or public
   * @return ID of the channel
   */
  String findConversation(@NonNull String channelName, boolean isPrivate);
  
  /**
   * Find user by user's id
   * @param userId
   * @return User information
   */
  User findUser(@NonNull String userId);
  
  /**
   * Get (root) messages by channel's id
   * @param channelId
   * @return
   */
  List<Message> getMessages(@NonNull String channelId);
  
  /**
   * Get replies by channel's id and message timestamp
   * @param channelId
   * @param ts
   * @return
   */
  List<Message> getReplies(@NonNull String channelId, @NonNull String ts);
  
  /**
   * Get all users of the workspace
   * @return
   */
  List<User> getUsers();
  
  /**
   * Publish match rich message layout 
   * @param channelName Channel's name
   * @param text Text of the message
   * @param choices Choices of the message: text - team's name, value - combine of match's ID and team's ID
   * @return
   */
  String publishMatch(
      @NonNull String channelName,
      @NonNull String text, 
      Map<String, String> choices
  );
  
  /**
   * Publish simple text message
   * @param channelName
   * @param text
   * @return
   */
  String publishMessage(@NonNull String channelName, @NonNull String text);
  
  /**
   * Publish leader board
   * @param channelName Channel's name
   * \@param title Message's title
   * @param sections List of message's sections
   * @return
   */
  String publishMessage(
      @NonNull String channelName, 
      String title, 
      List<SlackMessageSection> sections
  );
  
  /**
   * Reply to the message
   * @param channelName Channel's name
   * @param ts Timestamp of the message
   * @param text Text of the reply message
   * @return
   */
  String replyMessage(@NonNull String channelName, @NonNull String ts, @NonNull String text);
}
