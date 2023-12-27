package com.example.demoo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SessionEntity {

    @Id
    private String sessionId;
    
    private String username;
    private String password;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
    
    
    

}