package com.alvastudio.election2.Controllers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.alvastudio.election2.Models.User;

public class UserController {
    public static User loadUser(Context context) {
        User user = new User();

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        user.setUserId(sharedPreferences.getString("userId", ""));
        user.setUserName(sharedPreferences.getString("userName", ""));
        user.setVoteId(sharedPreferences.getInt("voteId", -1));
        user.setLastVoteId(sharedPreferences.getInt("lastVoteId", -1));

        return user;
    }
    public static void writeUser(User user, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getUserId());
        editor.putString("userName", user.getUserName());
        editor.putInt("voteId", user.getVoteId());
        editor.putInt("lastVoteId", user.getLastVoteId());

        editor.apply();
    }
}
