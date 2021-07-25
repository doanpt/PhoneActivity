# Display an activity over lock screen

To open Activity over lock screen. you can use a high-notification with "full-screen intent". This repository is a sample for using high notification with full screen intent to display an activity over lockscreen. Following below steps to create you app.

1. Create a **foreground service** then call buildNotification in onStartCommand method, the buildNotification method will return a notification which put into startForeground method parameter.
```
 public class IncomingCallService extends Service {
     public int onStartCommand(Intent intent, int flags, int startId) {
         Notification notification = buildNotification();
         startForeground(1, notification);
         return START_NOT_STICKY;
     }
 }
 ```
2. In buildNotification method, we will create **notification** with **high priority**, **call category** and a **full screen intent**.
```
 private Notification buildNotification() {
     Intent fullScreenIntent = new Intent(this, IncomingCallActivity.class);
     PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
     NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

     NotificationCompat.Builder notificationBuilder =
         new NotificationCompat.Builder(this)
                 .setSmallIcon(R.drawable.ic_notification_icon)
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
         notificationManager.createNotificationChannel(new NotificationChannel("123", "123", NotificationManager.IMPORTANCE_HIGH));
         notificationBuilder.setChannelId("123");
     }
     Notification incomingCallNotification = notificationBuilder.build();
     return incomingCallNotification;
 }
 ```
3. In onStartCommand, add a line of code to send **ACTION_CLOSE_SYSTEM_DIALOGS** broadcast action. **This verify IMPORTANT to kick off full screen pending intent**.
```
 public int onStartCommand(Intent intent, int flags, int startId) {
     Notification notification = buildNotification();
     startForeground(1, notification);
     sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
     return START_NOT_STICKY;
 }
 ```
4. Create full screen activity which you want to display over lock screen then you need to add setShowWhenLocked and setTurnScreenOn for display over lock screen. If not, your activity will be displayed behind lock screen. Below is my sample.
```
 public class IncomingCallActivity extends AppCompatActivity {
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
                 | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
     }
 }
 ```
5. Now you must start IncomingCallService when you receive a call from your logic.
```
 public void startCallService() {
     Intent intent = new Intent(context, IncomingCallService.class);
     startForegroundService(intent);
 }
You must declare activity, service and some permission in your manifest as below:

 <uses-permission android:name="android.permission.USE_FULL_SCREEN_INTENT" />
 <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 <application
    ...>
     <activity android:name=".IncomingCallActivity" />
     <service
         android:name=".IncomingCallService"
         android:enabled="true"
         android:exported="true" />
 </application>
 ```
**NOTE**: I tested on google, samsung, vsmart phone. It work well. But for xaomi device. you need to enable some permission by flow below steps:
```
Long click to you app icon
Open app info
Click to "Other permission" item
Allow show on Lock screen
```
Now your app will work on xaomi device. If you face any problems with my solution, please leave a comment here. I will help you If I could
