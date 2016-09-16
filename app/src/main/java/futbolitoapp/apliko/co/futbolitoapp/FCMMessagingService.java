package futbolitoapp.apliko.co.futbolitoapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by iosdeveloper on 15/09/16.
 */
public class FCMMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("onMessageReceived: ","De: "+remoteMessage.getFrom());
        Log.d("onMessageReceived: ","texto: "+remoteMessage.getNotification().getBody());
    }
}
