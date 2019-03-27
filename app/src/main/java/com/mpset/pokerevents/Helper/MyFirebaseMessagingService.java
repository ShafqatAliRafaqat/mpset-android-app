package com.mpset.pokerevents.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mpset.pokerevents.Activities.Inbox;
import com.mpset.pokerevents.MainActivity;
import com.mpset.pokerevents.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
                if (remoteMessage.getNotification() != null) {
            Log.d("body", "Message Notification Body: " + remoteMessage.getNotification().getBody());


            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
                    Intent intent = new Intent(this, Inbox.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.putExtra("Notification", true);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //Setting up Notification channels for android O and above
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        setupChannels();
                    }
                    int notificationId = new Random().nextInt(60000);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1222")
                            .setSmallIcon(R.mipmap.ic_launcher)  //a resource for your custom small icon
                            .setContentTitle(remoteMessage.getData().get(title)) //the "title" value you sent in your notification
                            .setContentText(remoteMessage.getData().get(body)) //ditto
                            .setAutoCancel(true)  //dismisses the notification on click
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager2 =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager2.notify(notificationId /* ID of notification */, notificationBuilder.build());

        }
    }
}