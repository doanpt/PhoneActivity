package com.ddona.call;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class IncomingCallService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = buildNotification();
        startForeground(1, notification);
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification buildNotification() {
        Intent fullScreenIntent = new Intent(this, IncomingCallActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_google_assistant)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);
        notificationBuilder.setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("my_call_app", "Call App", NotificationManager.IMPORTANCE_HIGH));
            notificationBuilder.setChannelId("my_call_app");
        }
        return notificationBuilder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
