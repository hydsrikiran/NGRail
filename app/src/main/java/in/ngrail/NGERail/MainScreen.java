package in.ngrail.NGERail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class MainScreen extends FragmentActivity{
    //private static final long DELAY = 500;
    //private boolean scheduled = false;
    //private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    SharedPreferences sharedpreferences;
    private Handler handler;
    private Runnable delayRunnable;
    //TextView lblMessage;
    Controller aController;
    /*private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;

    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;*/

    //private Speaker speaker;

    //private ToggleButton toggle;
    //private RadioGroup.OnCheckedChangeListener toggleListener;


    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    public static String name="8885033313";
    public static String email="info@ngrail.in";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (savedInstanceState == null) {
        }
        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        else
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        final String email = sharedpreferences.getString("email", null);
        final String phonenum = sharedpreferences.getString("name", null);


        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        aController = (Controller) getApplicationContext();


        // Check if Internet present
        if (!aController.isConnectingToInternet()) {

            // Internet Connection is not present
            aController.showAlertDialog(MainScreen.this,
                    "Internet Connection Error",
                    "Please connect to Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        /*if (Config.YOUR_SERVER_URL == null
                || Config.GOOGLE_SENDER_ID == null
                || Config.YOUR_SERVER_URL.length() == 0
                || Config.GOOGLE_SENDER_ID.length() == 0) {*/
        if (Config.YOUR_SERVER_URL.length() == 0
                || Config.GOOGLE_SENDER_ID.length() == 0) {

            // GCM sernder id / server url is missing
            aController.showAlertDialog(MainScreen.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);

            // stop executing code by return
            return;
        }
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        // Register custom Broadcast receiver to show messages on activity
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                Config.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        if (regId.equals("")) {

            // Register with GCM
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

        }else {
            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("regid", regId);
                //editor.commit();
                editor.apply();
                /*Toast.makeText(getApplicationContext(),
                        "Already registered with GCM Server",
                        Toast.LENGTH_LONG).
                        show();*/

            }
            else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.

                final Context context = this;

                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        // Register on our server
                        // On server creates a new user
                        aController.register(context, "8885033313", "NGRail Admin", regId);
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("regid", regId);
                        //editor.commit();
                        editor.apply();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
                };

                // execute AsyncTask
                mRegisterTask.execute(null, null, null);

            }
        }



        //splashTimer = new Timer();

        //splashTimer.schedule(new TimerTask() {
            //@Override
            //public void run() {
        handler = new Handler();
        delayRunnable = new Runnable() {

            @Override
            public void run() {
                Intent i;
                if (email == null || phonenum == null || email.equals("NA") || phonenum.equals("NA")) {
                    i = new Intent(MainScreen.this, RegistrationActivity.class);
                } else {
                    i = new Intent(MainScreen.this, HomeScreenActivity.class);
                }
                MainScreen.this.finish();
                //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                //MainScreen.this.finish();
                MainScreen.this.startActivity(i);
                // This makes the new screen slide up as it fades in
                // while the current screen slides up as it fades out.
                overridePendingTransition(R.anim.slide_in_f, R.anim.slide_out_f);
            }
        };
        handler.postDelayed(delayRunnable, 1000);
            //}
        //}, DELAY);
        //scheduled = true;

    }

    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());

            // Display message on the screen
            //lblMessage.append(newMessage + " ");

            Toast.makeText(getApplicationContext(),
                    "Got Message: " + newMessage,
                    Toast.LENGTH_LONG).show();

            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /*if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();*/

        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            /*if (scheduled)
                splashTimer.cancel();
            if(splashTimer!=null)
                splashTimer.purge();*/
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
            //unregisterReceiver(smsReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            //Log.d("UnRegister ", e.getMessage());
        }
    }

    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }

}
