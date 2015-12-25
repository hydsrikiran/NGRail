package in.ngrail.NGERail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ContactUsActivity extends AppCompatActivity { //implements OnMapReadyCallback, OnMarkerClickListener{
    //private static final long DELAY = 500;
    //private boolean scheduled = false;
    //private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_main);
        try {
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail Contact Us");
            setSupportActionBar(myToolbar);
            //myToolbar.setLogo(getDrawable(R.drawable.ngraillogo));
            //myToolbar.setTitle("NGRail");
            myToolbar.setNavigationIcon(R.drawable.leftarrow);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //splashTimer = new Timer();
                    //splashTimer.schedule(new TimerTask() {
                        //@Override
                        //public void run() {
                        /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Mail, "NA");
                        editor.putString(Phone, "NA");
                        editor.commit();*/
                            Intent i;
                            ContactUsActivity.this.finish();
                            i = new Intent(ContactUsActivity.this, HomeScreenActivity.class);
                            /*i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);*/
                            //ContactUsActivity.this.finish();
                            ContactUsActivity.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.slide_in_b, R.anim.slide_out_b);
                        //}
                    //}, DELAY);
                    //scheduled = true;
                }
            });

            //myToolbar.setSubtitle("One Stop Train Enquiry Hub");
            myToolbar.setLogo(R.mipmap.ngrailsmlogo);
        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server/Network issue. Please try again!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    @Override
    public void onBackPressed() {
        // your code.
        //splashTimer = new Timer();
        //splashTimer.schedule(new TimerTask() {
            //@Override
            //public void run() {
                        /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Mail, "NA");
                        editor.putString(Phone, "NA");
                        editor.commit();*/
                Intent i;
        ContactUsActivity.this.finish();
                i = new Intent(ContactUsActivity.this, HomeScreenActivity.class);
                //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                //ContactUsActivity.this.finish();
                ContactUsActivity.this.startActivity(i);
                // This makes the new screen slide up as it fades in
                // while the current screen slides up as it fades out.
                overridePendingTransition(R.anim.slide_in_b, R.anim.slide_out_b);
            //}
        //}, DELAY);
        //scheduled = true;
        //return;
    }

    /*public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }*/
}
