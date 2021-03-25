package com.example.hrishi.security.repositories;

import com.example.hrishi.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.username=:username")
    Optional<User> findByUserName(@Param("username") String username);
}
