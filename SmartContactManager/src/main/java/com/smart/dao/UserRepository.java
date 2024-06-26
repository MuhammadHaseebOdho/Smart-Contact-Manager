package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer>{
    @Query("select u from User u where u.email=:email")
    User getUserByName(@Param("email") String email);
}
