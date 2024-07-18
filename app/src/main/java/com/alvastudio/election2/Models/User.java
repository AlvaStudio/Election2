package com.alvastudio.election2.Models;

public class User {

    String userId;
    String userName;
    int voteId;
    int lastVoteId;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getLastVoteId() {
        return lastVoteId;
    }

    public void setLastVoteId(int lastVoteId) {
        this.lastVoteId = lastVoteId;
    }
}
