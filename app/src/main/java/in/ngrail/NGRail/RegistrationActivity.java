package in.ngrail.NGRail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import in.ngrail.NGRail.R;

/**
 * Created by kiran on 11-12-2015.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final long DELAY = 1000;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    SharedPreferences sharedpreferences;
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    String email =null;
    String phone =null;
    String setPnr=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
        }
        setContentView(R.layout.registration_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("NGRail");
        setSupportActionBar(myToolbar);
        //myToolbar.setLogo(getDrawable(R.drawable.ngraillogo));
        //myToolbar.setTitle("NGRail");
        /*myToolbar.setNavigationIcon(R.drawable.leftarrow);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashTimer = new Timer();
                splashTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent i;
                        i = new Intent(RegistrationActivity.this, MainScreen.class);
                        i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                        i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                        RegistrationActivity.this.finish();
                        RegistrationActivity.this.startActivity(i);
                        // This makes the new screen slide up as it fades in
                        // while the current screen slides up as it fades out.
                        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                    }
                }, DELAY);
                scheduled = true;
            }
        });*/

        myToolbar.setSubtitle("One Stop Train Enquiry Hub");
        myToolbar.setLogo(R.mipmap.ngrailsmlogo);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Button imgv = (Button)findViewById(R.id.regbutton);
        imgv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText email1 = (EditText) findViewById(R.id.regemail);
                EditText phone1 = (EditText) findViewById(R.id.mobnum);
                email = email1.getText().toString();
                phone = phone1.getText().toString();
                Context context = getApplicationContext();
                CharSequence text=null;
                if(email.length() == 0 || phone.length() == 0)
                {
                    text = "All fields are mandatory.!!";
                }
                else {
                    text = "We are registering. Please wait!!";
                }
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                loadingLayout = (LinearLayout)findViewById(R.id.LinearLayout1);
                loadingLayout.setVisibility(View.GONE);

                loadigText = (TextView) findViewById(R.id.textView111);
                loadigText.setVisibility(View.GONE);
                String regid = sharedpreferences.getString("regid", null);
                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                loadigIcon.setVisibility(View.GONE);
                if(regid==null)
                    regid="NA";
                setPnr = "http://api.ngrail.in/register1/user/"+email+"/mobnum/"+phone+"/regid/"+regid+"/";
                loadigIcon.post(new Starter(setPnr));
            }
        });
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }*/

    class Starter implements Runnable {
        String url=null;
        public Starter(String str){
            url=str;
        }
        public void run() {
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
                Context context = getApplicationContext();
                try{
                    JSONObject jsonobj = new JSONObject(result);
                    if(jsonobj.getString("responsecode").equals("200")) {
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Mail, email);
                        editor.putString(Phone, phone);
                        editor.commit();
                        CharSequence text = jsonobj.getString("error");
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent i = new Intent(RegistrationActivity.this, MainScreen.class);
                        RegistrationActivity.this.finish();
                        RegistrationActivity.this.startActivity(i);
                        loadingLayout.setVisibility(View.GONE);
                        loadigText.setVisibility(View.GONE);
                        loadigIcon.setVisibility(View.GONE);
                    }
                    else if(jsonobj.getString("responsecode").equals("201")) {
                        CharSequence text = "Registration Failed. "+jsonobj.getString("error");
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        loadingLayout.setVisibility(View.GONE);
                        loadigText.setVisibility(View.GONE);
                        loadigIcon.setVisibility(View.GONE);
                    }
                    else{
                        CharSequence text = "Registration Failed. Please caontact admin.";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        loadingLayout.setVisibility(View.GONE);
                        loadigText.setVisibility(View.GONE);
                        loadigIcon.setVisibility(View.GONE);
                    }

                }catch (JSONException e)
                {
                    CharSequence text = "Registration failed. Please contact admin";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    loadingLayout.setVisibility(View.GONE);
                    loadigText.setVisibility(View.GONE);
                    loadigIcon.setVisibility(View.GONE);
                }catch (Exception e){
                    CharSequence text = "Registration failed. Please contact admin";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    loadingLayout.setVisibility(View.GONE);
                    loadigText.setVisibility(View.GONE);
                    loadigIcon.setVisibility(View.GONE);
                }
                //sendSMSMessage(text.toString());

            }
            catch (Exception e)
            {
                ;
            }
        }

        private View.OnClickListener ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_item = (Integer) v.getId();
            }
        };
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
}
