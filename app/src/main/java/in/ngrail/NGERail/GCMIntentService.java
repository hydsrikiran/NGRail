package in.ngrail.NGERail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {

    public static final String MyPREFERENCES = "NGRailPrefs" ;
    SharedPreferences sharedpreferences;
    private static final String TAG = "GCMIntentService";

    private Controller aController = null;

    public GCMIntentService() {
        // Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context,
                "Your device registred with GCM");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("regid", registrationId);
        //editor.commit();
        editor.apply();
        //Log.d("NAME", MainScreen.name);
        aController.register(context, MainScreen.name,
                MainScreen.email, registrationId);
    }

    /**
     * Method called on device unregistred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if(aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_unregistered));

        aController.unregister(context, registrationId);
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            aController.unregister(context, registrationId);
        }
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received message"+intent.getExtras().toString());
        String message = intent.getExtras().getString("price");

        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

        if (aController == null)
            aController = (Controller) getApplicationContext();

        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_recoverable_error,
                        errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {


        String title = context.getString(R.string.app_name);

        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pending = PendingIntent.getActivity(context, 0, new Intent(), 0);

        //notif.notify(0, notify);
        //Notification.Builder builder = new Notification.Builder(context);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ngrailsmlogo)
                .setContentTitle(title)
                .setContentInfo("NGRail")
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setContentText(message)
                .setContentIntent(pending)
                .build();

        //Notification notify = builder.getNotification();
        notif.notify(R.drawable.ngrailsmlogo, notify);
    }

}
