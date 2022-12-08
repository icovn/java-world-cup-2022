package com.github.icovn.world_cup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wc_user")
public class User extends BaseEntity {
  
  @NotBlank
  private String fullName;
  
  @Column(unique = true)
  @NotBlank
  private String slackUserId;
  
  public static User of(String slackId, String fullName){
    var user = new User();
    user.setId(slackId);
    user.setSlackUserId(slackId);
    user.setFullName(fullName);
    return user;
  }
}
