package com.example.safehomecoming.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.safehomecoming.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService
{
    String TAG = "MyFirebaseInstanceIDService";

    /**
     * 구글 토큰을 얻는 값입니다.
     * 아래 토큰은 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용됩니다.
     **/

    @Override
    public ComponentName startService(Intent service)
    {
        Log.e(TAG, "startService");
        return super.startService(service);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDeletedMessages()
    {
        Log.e(TAG, "onDeletedMessages");
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s)
    {
        Log.e(TAG, "onMessageSent");
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e)
    {
        super.onSendError(s, e);
        Log.e(TAG, "onMessageSent s: " + s + " // e: " + e);
    }

    @Override
    public void onNewToken(String s)
    {
        Log.e(TAG, "onNewToken: " + s);
        super.onNewToken(s);
    }

    /**
     * 메세지를 받았을 경우 그 메세지에 대한 구현을 하는 부분입니다.
     **/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "onMessageReceived: remoteMessage: " + remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        sendNotification(remoteMessage);
    }

    private void showNotification(String title, String message)
    {
        Log.e(TAG, "showNotification: " );
/*        Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());*/
    }

    /**
     * remoteMessage 메세지 안애 getData와 getNotification이 있습니다.
     * 이부분은 차후 테스트 날릴때 설명 드리겠습니다.
     **/
    private void sendNotification(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "sendNotification: remoteMessage: " + remoteMessage);


//        String title = remoteMessage.getData().get("title");
//        String message = remoteMessage.getData().get("message");
//
//        Log.e(TAG, "sendNotification: title: " + title);
//        Log.e(TAG, "sendNotification: message: " + message);
//
//        /**
//         * 오레오 버전부터는 Notification Channel이 없으면 푸시가 생성되지 않는 현상이 있습니다.
//         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            String channel = "smartOffice_7777"; // 채널
//            String channel_nm = "smartOffice"; // 채널명
//
//            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
//                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
//            channelMessage.setDescription("smartOffice channel"); // 채널에 대한 설명
//            channelMessage.enableLights(true);
//            channelMessage.enableVibration(true);
//            channelMessage.setShowBadge(false);
//            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
//            notichannel.createNotificationChannel(channelMessage);
//
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channel)
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setChannelId(channel)
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        } else
//        {
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, "smartOffice_7777")
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        }

    }
}
