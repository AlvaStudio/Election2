package com.alvastudio.election2;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alvastudio.election2.Classes.FileUtils;
import com.alvastudio.election2.Classes.Utils;
import com.alvastudio.election2.Controllers.UserController;
import com.alvastudio.election2.Models.User;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getSupportActionBar().hide();

        createUser();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startApp();
            }
        }, 2000);
    }

    private void createUser() {
        User user = UserController.loadUser(this);
        if (user.getUserId().length() == 0) {
            user.setUserId(Utils.getUniDeviceId());
            user.setUserName(Utils.getDeviceName());
            user.setVoteId(-1);
            user.setLastVoteId(-1);
            UserController.writeUser(user, this);
        }
    }

    private void startApp() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}