package com.fitnessproject.core.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fitnessproject.core.data.model.UserAccount;

@Dao
public interface UserDao {

    /**
     * Look up an existence or account info of a user by username (globally unique).
     * @param username String to search for
     * @return The UserAccount if found, otherwise null
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserAccount getUserByUsername(String username);

    /**
     * Look up an account by its primary key ID
     * @param userId Internal user mapping Id
     * @return The UserAccount if found, otherwise null
     */
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    UserAccount getUserById(Long userId);

    /**
     * Efficiently check whether a user exists without fetching the entire entity.
     * @param username String to search for
     * @return true if the username exists
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    boolean existsByUsername(String username);

    /**
     * Inserts a new user account into the local datastore. Room handles unique constraint errors.
     * @param account Model reference
     * @return Number showing rows affected, or exception if constraint breached
     */
    @Insert
    long insertUser(UserAccount account);

    /**
     * Efficiently updates only the last login timestamp instead of updating the whole user object.
     * @param userId The ID of the user
     * @param timestamp The new timestamp
     */
    @Query("UPDATE users SET lastLoginAt = :timestamp WHERE userId = :userId")
    void updateLastLogin(Long userId, long timestamp);

    @Update
    void updateUser(UserAccount account);
}
