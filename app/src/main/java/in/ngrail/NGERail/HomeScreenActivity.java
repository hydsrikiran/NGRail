package in.ngrail.NGERail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String Type = "type";
    SharedPreferences sharedpreferences;
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    /*TextView lblMessage;
    Controller aController;
    private BroadcastReceiver sendBroadcastReceiver;
    private BroadcastReceiver deliveryBroadcastReceiver;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";*/



    public static String name;
    public static String email;
    public static String type;
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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        type = sharedpreferences.getString(Type, null);
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
                        editor.putString(Type, "NA");
                        editor.putString("fload", "1");
                        editor.commit();

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

        //PNR Status Button

        ImageView pnrstat = (ImageView)findViewById(R.id.pnrstatus);
        if(pnrstat!=null)
        {
            pnrstat.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if (type==null || !type.equals("NA")) {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else {
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
                }
            });
        }


        //SMART Search Button

        ImageView smartsearch = (ImageView)findViewById(R.id.smartsearch);
        if(smartsearch!=null)
        {
            smartsearch.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (type==null || !type.equals("NA")) {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else
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
                }
            });
        }



        //Train Route Button

        ImageView trainroute = (ImageView)findViewById(R.id.trainroute);
        if(trainroute!=null)
        {
            trainroute.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (type==null || !type.equals("NA")) {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else
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
                            i = new Intent(HomeScreenActivity.this, TrainRouteActivity.class);
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
                    if(type==null || !type.equals("NA"))
                    {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else {
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
                }
            });
        }


        //SPOT Train Button

        ImageView trainsnext = (ImageView)findViewById(R.id.trainsstation);
        if(trainsnext!=null)
        {
            trainsnext.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if(type==null || !type.equals("NA"))
                    {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else {
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
                                i = new Intent(HomeScreenActivity.this, TrainsAtStation.class);
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
                }
            });
        }

        //Conatact Button


        TableRow cntus1 = (TableRow)findViewById(R.id.regid);
        if(type==null || !type.equals("NA"))
        {
            if(cntus1.getVisibility()==View.GONE)
            {
                cntus1.setVisibility(View.VISIBLE);
            }
        }

        TableRow cntus11 = (TableRow)findViewById(R.id.regid1);
        if(type==null || !type.equals("NA"))
        {
            if(cntus11.getVisibility()==View.GONE)
            {
                cntus11.setVisibility(View.VISIBLE);
            }
        }

        ImageView cntus2 = (ImageView)findViewById(R.id.otp);
        if(cntus2!=null)
        {
            cntus2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    EditText rt = (EditText)findViewById(R.id.otp1);
                    if(rt.getText().length() <5)
                    {
                        showToast("Please valide your mobile number By entering OTP/ Contact Admin");
                    }
                    else {
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        loadigIcon.setVisibility(View.GONE);
                        loadigIcon.post(new Starter("http://api.ngrail.in/regotp/user/" + sharedpreferences.getString(Mail, null) + "/mobnum/"+sharedpreferences.getString(Phone,null)+"/otp/"+rt.getText()+"/"));

                    }
                }
            });
        }


        ImageView cntus3 = (ImageView)findViewById(R.id.otpnot);
        if(cntus3!=null)
        {
            cntus3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        loadigIcon.setVisibility(View.GONE);
                        loadigIcon.post(new Starter("http://api.ngrail.in/nootp/user/" + sharedpreferences.getString(Mail, null) + "/mobnum/"+sharedpreferences.getString(Phone,null)+"/"));
                }
            });
        }


        ImageView cntus = (ImageView)findViewById(R.id.aboutus);
        if(cntus!=null)
        {
            cntus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    splashTimer = new Timer();
                    splashTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent i;
                            i = new Intent(HomeScreenActivity.this, ContactUsActivity.class);
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

    class Starter implements Runnable {
        String url=null;
        public Starter(String str){
            url=str;
        }
        public void run() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            final String phonenum = sharedpreferences.getString("name", null);
            //EditText et1 = (EditText) findViewById(R.id.qqqq);
            //et1.setText("QQQ"+url+"/key/"+phonenum+"/");
            //start Asyn Task here
            new DownloadTask().execute(url);
        }
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                RailwayAPI railapi = new RailwayAPI();
                return railapi.getRegister(urls[0]);
            } catch (Exception e) {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server/Network issue. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return "Connection Error";
            }
        }
        @Override
        protected void onPreExecute() {
            //Start  Loading Animation
            loadingLayout.setVisibility(View.VISIBLE);
            loadigText.setVisibility(View.VISIBLE);
            loadigIcon.setVisibility(View.VISIBLE);
            //loadingViewAnim.start();
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                loadingLayout.setVisibility(View.GONE);
                loadigText.setVisibility(View.GONE);
                loadigIcon.setVisibility(View.GONE);
                Context context = getApplicationContext();
                if(result.equals("0\n"))
                {
                    CharSequence text = "Enquiry not possible in Tatkal Time";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                try{
                    JSONObject jsonobj = new JSONObject(result);
                    if(jsonobj.getString("responsecode").equals("200"))
                    {
                        if (jsonobj.getString("error").equals("NOOTP"))
                        {
                            showToast("Request Sent To Admin. Will get back to you.");
                            return;
                        }
                        else {
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Type, "NA");
                            editor.commit();

                            splashTimer = new Timer();
                            splashTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent i;
                                    i = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
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
                    }
                    else
                    {
                        if (jsonobj.getString("error").equals("NOOTP"))
                        {
                            showToast("Error while Sending to Admin.");
                            return;
                        }
                        else {
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            showToast("Invalid OTP. Please try again/Contact Admin!!");
                            editor.putString(Type, "R");
                            editor.commit();
                        }

                        /*splashTimer = new Timer();
                        splashTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent i;
                                i = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
                                i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                                i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                                HomeScreenActivity.this.finish();
                                HomeScreenActivity.this.startActivity(i);
                                // This makes the new screen slide up as it fades in
                                // while the current screen slides up as it fades out.
                                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                            }
                        }, DELAY);
                        scheduled = true;*/

                        return;
                    }
                }catch (JSONException j)
                {
                    context = getApplicationContext();
                    CharSequence text = "Back-end Server/Network issue. Please try again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
            catch (Exception e)
            {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server/Network issue. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

        private View.OnClickListener ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_item = (Integer) v.getId();
                //Log.d("OUT END", String.valueOf(selected_item));
            }
        };
    }

    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }

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
