package com.example.psafe.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class LoggedInUser {

    @PrimaryKey(autoGenerate = true)
    private String userId;

    private final String displayName;
    private final String position;

    public LoggedInUser()
    {
        userId = "default ID";
        displayName = "foo";
        position = "";

    }

    public LoggedInUser(String userId, String displayName, String position) {
        this.userId = userId;
        this.displayName = displayName;
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPosition() {
        return position;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}