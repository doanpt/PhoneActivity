package com.ddona.call;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingCallActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        setShowWhenLocked(true);
        setTurnScreenOn(true);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        );
        stopService(new Intent(this, IncomingCallService.class));
    }
}
