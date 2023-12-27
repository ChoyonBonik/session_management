package com.example.demoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demoo.LoginEntity;
import com.example.demoo.SessionEntity;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
	//@Query("SELECT l FROM auth l WHERE l.username = :username AND l.password = :password")
	LoginEntity findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
  }
