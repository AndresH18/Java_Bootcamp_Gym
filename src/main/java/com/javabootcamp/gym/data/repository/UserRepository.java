package com.javabootcamp.gym.data.repository;

import com.javabootcamp.gym.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT COUNT(*) FROM User u WHERE u.username  LIKE :prefix%")
    long countUsernamesLike(@Param("prefix") String usernamePrefix);

    long countUserByUsernameStartingWith(String prefix);

    boolean existsUserById(int id);
}
