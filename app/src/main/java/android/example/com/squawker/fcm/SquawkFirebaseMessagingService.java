package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by brunogtavares on 7/7/18.
 */

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

    private static final String AUTHOR = SquawkContract.COLUMN_AUTHOR;
    private static final String AUTHOR_KEY = SquawkContract.COLUMN_AUTHOR_KEY;
    private static final String MESSAGE = SquawkContract.COLUMN_MESSAGE;
    private static final String DATE = SquawkContract.COLUMN_DATE;
    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    // This method by default runs on the background thread, it doesn't need to call async tastk
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0) {

            Map<String, String> message = remoteMessage.getData();
            insertSquawk(message);

        }
    }

    private void insertSquawk(final Map<String, String> squawk) {

        ContentValues newSquawk = new ContentValues();
        newSquawk.put(AUTHOR, squawk.get(AUTHOR));
        newSquawk.put(MESSAGE, squawk.get(MESSAGE));
        newSquawk.put(DATE, squawk.get(DATE));
        newSquawk.put(AUTHOR_KEY, squawk.get(AUTHOR_KEY));
        getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newSquawk);
    }

    private void sendNotification(Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request Code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(AUTHOR);
        String message = data.get(MESSAGE);

        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
