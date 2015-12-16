package in.ngrail.NGRail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiran on 11-12-2015.
 */
public class HomeScreenActivity extends AppCompatActivity{
    private static final long DELAY = 500;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    SharedPreferences sharedpreferences;
    /*TextView lblMessage;
    Controller aController;
    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";*/



    public static String name;
    public static String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("NGRail");
        setSupportActionBar(myToolbar);
        //myToolbar.setLogo(getDrawable(R.drawable.ngraillogo));
        //myToolbar.setTitle("NGRail");
        myToolbar.setNavigationIcon(R.drawable.logout);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashTimer = new Timer();
                splashTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Mail, "NA");
                        editor.putString(Phone, "NA");
                        editor.commit();

                        /*//Get Global Controller Class object (see application tag in AndroidManifest.xml)
                        aController = (Controller) getApplicationContext();


                        // Check if Internet present
                        if (!aController.isConnectingToInternet()) {

                            // Internet Connection is not present
                            aController.showAlertDialog(HomeScreenActivity.this,
                                    "Internet Connection Error",
                                    "Please connect to Internet connection", false);
                            // stop executing code by return
                            return;
                        }

                        // Getting name, email from intent
                        Intent i = getIntent();

                        name = i.getStringExtra("name");
                        email = i.getStringExtra("email");

                        // Make sure the device has the proper dependencies.
                        //GCMRegistrar.checkDevice(getApplicationContext());

                        // Make sure the manifest permissions was properly set
                        //GCMRegistrar.checkManifest(getApplicationContext());

                        lblMessage = (TextView) findViewById(R.id.lblMessage);

                        // Register custom Broadcast receiver to show messages on activity
                        //registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        //        Config.DISPLAY_MESSAGE_ACTION));

                        // Get GCM registration id
                        final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());

                        // Check if regid already presents
                        if (regId.equals("")) {

                            // Register with GCM
                            GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);

                        } else {

                            // Device is already registered on GCM Server
                            if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {

                                final Context context1 = getApplicationContext();
                                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                                    @Override
                                    protected Void doInBackground(Void... params) {

                                        // Register on our server
                                        // On server creates a new user
                                        aController.unregister(context1, regId);

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void result) {
                                        mRegisterTask = null;
                                        Intent i;
                                        i = new Intent(HomeScreenActivity.this, RegisterActivity.class);
                                                    i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                                                    i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                                                    HomeScreenActivity.this.finish();
                                                    HomeScreenActivity.this.startActivity(i);
                                                    // This makes the new screen slide up as it fades in
                                                    // while the current screen slides up as it fades out.
                                                    overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                                                }

                                };

                                // execute AsyncTask
                                mRegisterTask.execute(null, null, null);
                            }
                        }*/

                        Intent i1;
                        i1 = new Intent(HomeScreenActivity.this, MainScreen.class);
                        i1.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                        i1.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                        HomeScreenActivity.this.finish();
                        HomeScreenActivity.this.startActivity(i1);
                        // This makes the new screen slide up as it fades in
                        // while the current screen slides up as it fades out.
                        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                    }
                }, DELAY);
                scheduled = true;
            }
        });

        myToolbar.setSubtitle("One Stop Train Enquiry Hub");
        myToolbar.setLogo(R.mipmap.ngrailsmlogo);


        /*//Get Global Controller Class object (see application tag in AndroidManifest.xml)
        aController = (Controller) getApplicationContext();


        // Check if Internet present
        if (!aController.isConnectingToInternet()) {

            // Internet Connection is not present
            aController.showAlertDialog(HomeScreenActivity.this,
                    "Internet Connection Error",
                    "Please connect to Internet connection", false);
            // stop executing code by return
            return;
        }

        // Getting name, email from intent
        Intent i = getIntent();

        name = i.getStringExtra("name");
        email = i.getStringExtra("email");

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        //lblMessage = (TextView) findViewById(R.id.lblMessage);

        // Register custom Broadcast receiver to show messages on activity
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                Config.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {

            // Register with GCM
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

        } else {

            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Mail, email);
                editor.putString(Phone, name);
                editor.commit();
                // Skips registration.

                Toast.makeText(getApplicationContext(),
                        "Already registered with GCM Server",
                        Toast.LENGTH_LONG).
                        show();

            } else {

                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.

                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        // Register on our server
                        // On server creates a new user
                        aController.register(context, name, email, regId);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        final String email = sharedpreferences.getString("email", null);
                        final String phonenum = sharedpreferences.getString("name", null);
                        splashTimer = new Timer();

                        splashTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent i;
                                if (email == null || phonenum == null || email.equals("NA") || phonenum.equals("NA")) {
                                    i = new Intent(HomeScreenActivity.this, RegisterActivity.class);
                                    i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                                    i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                                    HomeScreenActivity.this.finish();
                                    HomeScreenActivity.this.startActivity(i);
                                    // This makes the new screen slide up as it fades in
                                    // while the current screen slides up as it fades out.
                                    overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                                }
                            }
                        }, DELAY);
                        scheduled = true;
                    }

                };

                // execute AsyncTask
                mRegisterTask.execute(null, null, null);
            }
        }*/

        //PNR Status Button

        ImageView pnrstat = (ImageView)findViewById(R.id.pnrstatus);
        if(pnrstat!=null)
        {
            pnrstat.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    splashTimer = new Timer();
                    splashTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Mail, "NA");
                            editor.putString(Phone, "NA");
                            editor.commit();*/
                            Intent i;
                            i = new Intent(HomeScreenActivity.this, PnrActivity.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            HomeScreenActivity.this.finish();
                            HomeScreenActivity.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                        }
                    }, DELAY);
                    scheduled = true;
                }
            });
        }


        //SMART Search Button

        ImageView smartsearch = (ImageView)findViewById(R.id.smartsearch);
        if(smartsearch!=null)
        {
            smartsearch.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    splashTimer = new Timer();
                    splashTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Mail, "NA");
                            editor.putString(Phone, "NA");
                            editor.commit();*/
                            Intent i;
                            i = new Intent(HomeScreenActivity.this, TrainsBetweenTwoStations.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            HomeScreenActivity.this.finish();
                            HomeScreenActivity.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                        }
                    }, DELAY);
                    scheduled = true;
                }
            });
        }


        //SPOT Train Button

        ImageView spottrain = (ImageView)findViewById(R.id.livestatus);
        if(spottrain!=null)
        {
            spottrain.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    splashTimer = new Timer();
                    splashTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Mail, "NA");
                            editor.putString(Phone, "NA");
                            editor.commit();*/
                            Intent i;
                            i = new Intent(HomeScreenActivity.this, SpotTrainMainActivity.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            HomeScreenActivity.this.finish();
                            HomeScreenActivity.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                        }
                    }, DELAY);
                    scheduled = true;
                }
            });
        }
    }

    /*// Create a broadcast receiver to get message and show on screen
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
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*// Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {*/
            if (scheduled)
                splashTimer.cancel();
        if(splashTimer!=null)
            splashTimer.purge();
            /*// Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.d("UnRegister ", e.getMessage());
        }*/

    }
    /*@Override
    protected void onStop()
    {
        unregisterReceiver(mHandleMessageReceiver);
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mHandleMessageReceiver);
    }*/
}
