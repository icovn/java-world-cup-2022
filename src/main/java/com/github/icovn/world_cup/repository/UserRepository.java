package com.github.icovn.world_cup.repository;

import com.github.icovn.world_cup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  
}
