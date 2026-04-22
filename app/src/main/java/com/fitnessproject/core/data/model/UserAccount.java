package com.fitnessproject.core.data.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room Entity representing a locally registered user in the database.
 * The username is globally unique to prevent duplicate accounts.
 */
@Entity(tableName = "users", indices = {@Index(value = "username", unique = true)})
public class UserAccount {

    @PrimaryKey(autoGenerate = true)
    private Long userId;

    private String username;
    private String passwordSalt;
    private String passwordHash;
    private long createdAt;
    private long lastLoginAt;
    private boolean active;

    // Room requires an empty constructor or getters/setters for all properties.
    public UserAccount() {
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(long lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

