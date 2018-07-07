package android.example.com.squawker.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by brunogtavares on 7/7/18.
 */

public class SquawkFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static String TAG = SquawkFirebaseInstanceIdService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of the previous token had
     * been compromised. Note that this is called when the InstanceID token is initally generated to this is
     * where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refresh token: " + refreshToken);

        // If you want to send messages to this application instance or
        // manage this apps subscription on the server side, send the Instance ID
        // token to your app server
        sendRegistrationToServer(refreshToken);
    }

    /**
     * Persist token to third-party servers.
     */
    private void sendRegistrationToServer(String token) {
        // This method is blank, but if you were to build a server that stores users token
        // information, this is where you'd send the token to the server.
    }


}
