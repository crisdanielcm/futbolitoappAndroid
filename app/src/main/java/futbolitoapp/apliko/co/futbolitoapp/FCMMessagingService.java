package futbolitoapp.apliko.co.futbolitoapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by iosdeveloper on 15/09/16.
 */
public class FCMMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("FIREBASE", remoteMessage.getNotification().getBody());

        showNotification(remoteMessage.getNotification().getBody());
    }

    private void showNotification(String message) {

        Intent i = new Intent(this,LigasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("FCM Test")
                .setContentText(message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }


}
