package in.ngrail.NGERail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TrainsBetweenTwoStationsList extends AppCompatActivity{
    //private static final long DELAY = 500;
    //private boolean scheduled = false;
    //private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    public String dateval = null;
    public String jsonvalue = "";
    int selectedid = 0;
    //String Pnrstr = null;
    SharedPreferences sharedpreferences;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainbetweentwostationslist);
        Bundle bundle = getIntent().getExtras();
        jsonvalue = bundle.getString("jsonvalue");
        dateval = bundle.getString("dateval");
        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail Smart Search");
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
                            Intent i;
                            TrainsBetweenTwoStationsList.this.finish();
                            i = new Intent(TrainsBetweenTwoStationsList.this, TrainsBetweenTwoStations.class);
                            //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            //TrainsBetweenTwoStationsList.this.finish();
                            TrainsBetweenTwoStationsList.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.slide_out_b, R.anim.slide_in_b);
                        //}
                    //}, DELAY);
                    //scheduled = true;
                }
            });

            //myToolbar.setSubtitle("One Stop Train Enquiry Hub");
            myToolbar.setLogo(R.mipmap.ngrailsmlogo);
            //myToolbar.inflateMenu(R.menu.main);
            LinearLayout chat;// = (LinearLayout) findViewById(R.id.pnrstatusnum);
            //chat.setVisibility(View.INVISIBLE);

            try {
                JSONObject jsonobj = new JSONObject(jsonvalue);
                if(jsonobj.getString("responsecode").equals("200"))
                {
                    TextView trheader = (TextView)findViewById(R.id.trheader);
                    trheader.setText(jsonobj.getString("from")+" > "+jsonobj.getString("to"));

                    TextView trheader1 = (TextView)findViewById(R.id.trheader1);
                    trheader1.setText("Date Of Journey : "+jsonobj.getString("doj"));
                    JSONArray jaray = jsonobj.getJSONArray("details");
                    int totlcount = jaray.length();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.leftMargin=5;
                    params.rightMargin=5;
                    params.topMargin=5;
                    params.bottomMargin=10;
                    Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                    Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                    ImageView[] img1= new ImageView[totlcount];
                    ImageView[] img2= new ImageView[totlcount];
                    ImageView[] img3= new ImageView[totlcount];
                    TextView[] tv1= new TextView[totlcount];
                    //TextView[] tv2= new TextView[totlcount];
                    //TextView[] tv3= new TextView[totlcount];
                    TextView[] tv4= new TextView[totlcount];
                    TextView[] tv5= new TextView[totlcount];
                    TextView[] tv6= new TextView[totlcount];
                    //TextView[] tv7= new TextView[totlcount];
                    LinearLayout[] ll = new LinearLayout[jaray.length()];
                    for(int i=0; i<jaray.length(); i++) {
                        ll[i] = new LinearLayout(getApplicationContext());
                        ll[i].setOrientation(LinearLayout.VERTICAL);
                        ll[i].setPadding(3, 3, 3, 3);
                        ll[i].setId(((i + 1) * 10000) + 1);
                        ll[i].setBackgroundResource(R.drawable.pnrdiv);


                        //table lay out 1
                        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        tableLayoutParams.setMargins(0, 0, 0, 10);
                        TableLayout tableLayout = new TableLayout(getApplicationContext());
                        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        TableRow tableRow = new TableRow(getApplicationContext());
                        tableRow.setPadding(5, 5, 5, 5);

                        img1[i] = new ImageView(getApplicationContext());
                        img1[i].setImageResource(R.drawable.pnrtrain);
                        tableRow.addView(img1[i], tableRowParams);

                        tv1[i] = new TextView(getApplicationContext());
                        tv1[i].setTextSize(10);
                        if (Build.VERSION.SDK_INT >= 17)
                            tv1[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        tv1[i].setTypeface(null, Typeface.BOLD);
                        if (Build.VERSION.SDK_INT >= 23) {
                            tv1[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark,getApplicationContext().getTheme()));
                        }else {
                            tv1[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        String htmlText;
                        if (Build.VERSION.SDK_INT >= 23) {
                            htmlText = "<body>" +
                                    "<h3>"+jaray.getJSONObject(i).getString("tnumber")+" ("+jaray.getJSONObject(i).getString("tname")+")"+"</h3>"+
                                    "<font color=\""+getResources().getColor(R.color.colorPnrDate,getApplicationContext().getTheme())+"\">("+jaray.getJSONObject(i).getString("sdeparture")+")"+jaray.getJSONObject(i).getString("source").split("\\(")[0]+" ==> "+"("+jaray.getJSONObject(i).getString("darrival")+")"+jaray.getJSONObject(i).getString("destination").split("\\(")[0]+"</font>"+
                                    "</body>";
                        }else {
                            htmlText = "<body>" +
                                    "<h3>"+jaray.getJSONObject(i).getString("tnumber")+" ("+jaray.getJSONObject(i).getString("tname")+")"+"</h3>"+
                                    "<font color=\""+getResources().getColor(R.color.colorPnrDate)+"\">("+jaray.getJSONObject(i).getString("sdeparture")+")"+jaray.getJSONObject(i).getString("source").split("\\(")[0]+" ==> "+"("+jaray.getJSONObject(i).getString("darrival")+")"+jaray.getJSONObject(i).getString("destination").split("\\(")[0]+"</font>"+
                                    "</body>";
                        }

                        tv1[i].setText(Html.fromHtml(htmlText, null, null));
                        tv1[i].setId(((i + 1) * 1000) + 1);
                        tableRow.addView(tv1[i], tableRowParams);
                        tableLayout.addView(tableRow, tableRowParams);
                        ll[i].addView(tableLayout, tableLayoutParams);



                        //table lay out 3
                        TableLayout.LayoutParams tableLayoutParams2 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        TableLayout tableLayout2 = new TableLayout(getApplicationContext());
                        tableLayout2.setColumnStretchable(1, false);
                        tableLayout2.setColumnStretchable(0, true);
                        TableRow.LayoutParams tableRowParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        TableRow tableRow2 = new TableRow(getApplicationContext());
                        tableRow2.setPadding(10, 0, 0, 0);

                        tv4[i] = new TextView(getApplicationContext());
                        tv4[i].setTextSize(15);
                        if (Build.VERSION.SDK_INT >= 17)
                            tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        if (Build.VERSION.SDK_INT >= 23) {
                            tv4[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark,getApplicationContext().getTheme()));
                        }else {
                            tv4[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        tv4[i].setPadding(0, 0, 5, 0);
                        tv4[i].setId(((i + 1) * 1000) + 2);
                        try {
                            int dst2travel = Integer.parseInt(jaray.getJSONObject(i).getString("ddistance")) - Integer.parseInt(jaray.getJSONObject(i).getString("sdistance"));
                            tv4[i].setText("Distance : " + String.valueOf(dst2travel));
                        }catch (Exception e)
                        {
                            tv4[i].setText("Distance : " + String.valueOf(0));
                        }
                        tableRow2.addView(tv4[i], tableRowParams3);


                        img3[i] = new ImageView(getApplicationContext());
                        img3[i].setId(i + 1);
                        img3[i].setImageResource(R.drawable.pnrinfo);
                        img3[i].setBackground(getResources().getDrawable(R.drawable.button));
                        registerForContextMenu(img3[i]);
                        img3[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openContextMenu(v);
                            }
                        });
                        TableRow.LayoutParams tableRowParams7 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        tableRowParams7.setMargins(0, 0, 10, 0);
                        tableRow2.addView(img3[i], tableRowParams7);

                        tableLayout2.addView(tableRow2, tableRowParams3);
                        ll[i].addView(tableLayout2, tableLayoutParams2);






                        //table lay out 4
                        TableLayout.LayoutParams tableLayoutParams3 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        TableLayout tableLayout3 = new TableLayout(getApplicationContext());
                        tableLayout3.setColumnStretchable (2, true);
                        //tableLayout3.setColumnStretchable (3, true);
                        TableRow.LayoutParams tableRowParams4 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        TableRow tableRow3= new TableRow(getApplicationContext());
                        tableRow3.setPadding(10,0,0,20);

                        img2[i] = new ImageView(getApplicationContext());
                        img2[i].setImageResource(R.drawable.pnrchair);
                        TableRow.LayoutParams tableRowParams5 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        tableRowParams5.setMargins(0, 0, 4, 0);
                        tableRow3.addView(img2[i], tableRowParams5);


                        tv6[i] = new TextView(getApplicationContext());
                        tv6[i].setTextSize(12);
                        if (Build.VERSION.SDK_INT >= 17)
                            tv6[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                        String statuspnr = jaray.getJSONObject(i).getString("fare");
                        tv6[i].setTypeface(null, Typeface.BOLD);
                        String classes = jaray.getJSONObject(i).getString("classes");
                        String[] finalstr = classes.split(",");
                        String[] finalstr1 = statuspnr.split(",");
                        StringBuilder putstr = new StringBuilder();
                        for(int y=1; y<finalstr.length; y++)
                        {
                            putstr.append(finalstr[y]+"("+finalstr1[y]+")");
                            if(y < finalstr.length-1)
                                putstr.append(" | ");
                        }
                        if (putstr.toString().length()>0) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                tv6[i].setTextColor(getResources().getColor(R.color.colorPnrCnf,getApplicationContext().getTheme()));
                            }else {
                                tv6[i].setTextColor(getResources().getColor(R.color.colorPnrCnf));
                            }
                            tv6[i].setText(putstr.toString());
                        }
                        tv6[i].setPadding(0, 0, 5, 0);
                        tv6[i].setId(((i + 1) * 1000) + 3);
                        tableRow3.addView(tv6[i], tableRowParams4);

                        tv5[i] = new TextView(getApplicationContext());
                        String idstr = jaray.getJSONObject(i).getString("tnumber")+"#"+jaray.getJSONObject(i).getString("source")+"#"+jaray.getJSONObject(i).getString("destination")+"#"+jaray.getJSONObject(i).getString("classes")+"#"+jsonobj.getString("doj");
                        tv5[i].setText(idstr);
                        tv5[i].setId(((i + 1) * 1000) + 4);
                        tableRow3.addView(tv5[i], tableRowParams4);


                        tableLayout3.addView(tableRow3, tableRowParams4);
                        ll[i].addView(tableLayout3, tableLayoutParams3);



                        chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                        chat.addView(ll[i], params);
                        ll[i].setVisibility(View.INVISIBLE);
                    }
                    /*if(chat.getVisibility() == View.INVISIBLE)
                    {
                        chat.startAnimation(slideL);
                        chat.setVisibility(View.VISIBLE);
                    }
                    chat.startAnimation(slideR);*/
                    for(int i=0; i<jaray.length(); i++)
                    {
                        if(ll[i].getVisibility()==View.INVISIBLE){

                            ll[i].startAnimation(slideL);
                            ll[i].setVisibility(View.VISIBLE);
                        }
                        ll[i].startAnimation(slideR);
                    }

                }
            }catch (JSONException e){
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server/Network issue. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            ImageView leftarr = (ImageView)findViewById(R.id.goleft);
            leftarr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TextView dt = (TextView) findViewById(R.id.trheader1);
                        String dtp = dt.getText().toString().split(":")[1];
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(dtp));
                        c.add(Calendar.DATE, -1);  // number of days to add
                        dtp = sdf.format(c.getTime());  // dt is now the new date

                        TextView sd = (TextView)findViewById(R.id.trheader);
                        String sdp = sd.getText().toString();
                        String[] sdps = sdp.split(">");
                        String[] sdps1 = sdps[0].split("\\(");
                        String sourcen = sdps1[1].substring(0,sdps1[1].length()-2);

                        sdps1 = sdps[1].split("\\(");
                        String destn = sdps1[1].substring(0,sdps1[1].length()-1);

                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        if (Build.VERSION.SDK_INT >= 23) {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getApplicationContext().getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }else {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                        loadigIcon.setVisibility(View.GONE);

                        //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();
                        dateval = dtp;
                        // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                        loadigIcon.post(new Starter_tb("http://api.ngrail.in/trbwts/source/" + sourcen + "/destination/" + destn + "/doj/" + dtp));


                    }catch (ParseException e)
                    {
                        showToast("Date format Wrong. Please Contact Admin!!");
                    }catch (Exception e)
                    {
                        //showToast(e.getMessage());
                    }
                }
            });

            ImageView rightarr = (ImageView)findViewById(R.id.goright);
            rightarr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TextView dt = (TextView) findViewById(R.id.trheader1);
                        String dtp = dt.getText().toString().split(":")[1];
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(dtp));
                        c.add(Calendar.DATE, 1);  // number of days to add
                        dtp = sdf.format(c.getTime());  // dt is now the new date

                        TextView sd = (TextView) findViewById(R.id.trheader);
                        String sdp = sd.getText().toString();
                        String[] sdps = sdp.split(">");
                        String[] sdps1 = sdps[0].split("\\(");
                        String sourcen = sdps1[1].substring(0, sdps1[1].length() - 2);

                        sdps1 = sdps[1].split("\\(");
                        String destn = sdps1[1].substring(0, sdps1[1].length() - 1);

                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        if (Build.VERSION.SDK_INT >= 23) {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getApplicationContext().getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
                        } else {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                        loadigIcon.setVisibility(View.GONE);

                        //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();
                        dateval = dtp;
                        // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                        loadigIcon.post(new Starter_tb("http://api.ngrail.in/trbwts/source/" + sourcen + "/destination/" + destn + "/doj/" + dtp));

                    } catch (ParseException e) {
                        showToast("Date format Wrong. Please Contact Admin!!");
                    } catch (Exception e) {
                        //showToast(e.getMessage());
                    }
                }
            });

        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server/Network issue. Please try again!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }


    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }
    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        //mLevelTextView.setText("Level " + (++mLevel));
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    @Override
    public void onBackPressed() {
        // your code.
        //splashTimer = new Timer();
        //splashTimer.schedule(new TimerTask() {
            //@Override
            //public void run() {
                Intent i;
                TrainsBetweenTwoStationsList.this.finish();
                i = new Intent(TrainsBetweenTwoStationsList.this, TrainsBetweenTwoStations.class);
                //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                //TrainsBetweenTwoStationsList.this.finish();
                TrainsBetweenTwoStationsList.this.startActivity(i);
                // This makes the new screen slide up as it fades in
                // while the current screen slides up as it fades out.
                overridePendingTransition(R.anim.slide_in_b, R.anim.slide_out_b);
            //}
        //}, DELAY);
        //scheduled = true;
        //return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //int id = item.getItemId();

        /*if (id == R.id.action_status) {

            return true;
        }*/

        return super.onOptionsItemSelected(item);
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
            //Log.d("AAAA","QQQ"+url+"/key/"+phonenum+"/");
            //start Asyn Task here
            new DownloadTask().execute(url+"/key/"+phonenum+"/");
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
                int duration = Toast.LENGTH_LONG;
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
                    //return;
                }
                else
                {
                    try {
                        //Log.d("AAAA",result);
                        JSONObject jsnobj = new JSONObject(result);
                        if(jsnobj.getInt("responsecode") == 200) {
                            TrainsBetweenTwoStationsList.this.finish();
                            Intent i = new Intent(TrainsBetweenTwoStationsList.this, AdvSeatAvail.class);
                            //i.putExtra("anim id in", R.anim.fragment_slide_left_exit);
                            //i.putExtra("anim id out", R.anim.fragment_slide_right_enter);
                            i.putExtra("jsonval", result);
                            i.putExtra("jsonvalue", jsonvalue);
                            i.putExtra("dateval", dateval);

                            TrainsBetweenTwoStationsList.this.startActivity(i);
                            overridePendingTransition(R.anim.slide_in_f, R.anim.slide_out_f);
                        }
                        else
                        {
                            showToast("Back-end Server issue. Please try after some time.!");
                        }
                    }catch (JSONException e)
                    {
                        showToast("Back-end Server/Network issue. Please try again!");
                    }
                    //Log.d("ASAAS", jsonvalue + String.valueOf(dateval));
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

    }
    @Override

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        //if (v.getId()==R.id.usernameEdittext) {
        //getMenuInflater().inflate(R.menu.contextmenu, menu);
        //
        if(v.getId()>0) {
            selectedid = v.getId();
            int totalclasses = (selectedid * 1000) + 4;
            TextView tt = (TextView) findViewById(totalclasses);
            String[] splitstr = tt.getText().toString().split("#");
            String classes = splitstr[3];
            String[] spcl = classes.split(",");
            //String pnrval = tt.getText().toString();
            menu.setHeaderTitle("Seat Availability");
            menu.setHeaderIcon(R.drawable.ngrailsmlogo);
            for(int i=1; i<spcl.length; i++)
            {
                menu.add(0, (selectedid * 1000)+4+1, 0, spcl[i]);
            }
        }
        //menu.add(0, id2, 0, "Check For Duplicate"+pnr);
    }

    @Override

    public boolean onContextItemSelected(MenuItem item) {

        try {
            showInterstitial();
            TextView tt = (TextView) findViewById(item.getItemId() - 1);
            String[] splity = tt.getText().toString().split("#");
            loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
            loadingLayout.setVisibility(View.GONE);

            loadigText = (TextView) findViewById(R.id.textView111);
            loadigText.setVisibility(View.GONE);

            loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
            if (Build.VERSION.SDK_INT >= 23) {
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getApplicationContext().getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
            }else {
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            loadigIcon.setVisibility(View.GONE);
            String soures = splity[1].split("\\(")[1];
            soures = soures.substring(0, soures.length() - 1);
            String descs = splity[2].split("\\(")[1];
            descs = descs.substring(0, descs.length() - 1);
            String reqclass = item.getTitle().toString();
            String[] dojsp = splity[4].split("-");
            String dojf = dojsp[2] + "-" + dojsp[1] + "-" + dojsp[0];
            loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/" + soures + "/destination/" + descs + "/doj/" + dojf + "/train/" + splity[0] + "/class/" + reqclass));
        }catch (Exception e)
        {
            Log.d("Exception list", e.getMessage());
        }
        //Log.d("ASAAS", jsonvalue + String.valueOf(dateval));
        return true;
    }
    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

    }

    class Starter_tb implements Runnable {
        String url=null;
        public Starter_tb(String str){
            url=str;
        }
        public void run() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            final String phonenum = sharedpreferences.getString("name", null);
            //EditText et1 = (EditText) findViewById(R.id.qqqq);
            //et1.setText("QQQ"+url+"/key/"+phonenum+"/");
            //start Asyn Task here
            //Log.d("URL",url+"/key/"+phonenum+"/");
            new DownloadTask_tb().execute(url+"/key/"+phonenum+"/");
        }
    }

    private class DownloadTask_tb extends AsyncTask<String, Void, String> {

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
                    if(jsonobj.getString("responsecode").equals("200")) {
                        /*Intent i = new Intent(TrainsBetweenTwoStationsList.this, TrainsBetweenTwoStationsList.class);
                        i.putExtra("anim id in", R.anim.fragment_slide_left_exit);
                        i.putExtra("anim id out", R.anim.fragment_slide_right_enter);
                        TrainsBetweenTwoStationsList.this.finish();
                        i.putExtra("jsonvalue", result);
                        i.putExtra("dateval", dateval);

                        TrainsBetweenTwoStationsList.this.startActivity(i);*/
                        LinearLayout chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                        //chat.setVisibility(View.INVISIBLE);

                        try {
                            //JSONObject jsonobj = new JSONObject(jsonvalue);
                            if(jsonobj.getString("responsecode").equals("200"))
                            {
                                jsonvalue = result;
                                chat.removeAllViewsInLayout();
                                TextView trheader = (TextView)findViewById(R.id.trheader);
                                trheader.setText(jsonobj.getString("from")+" > "+jsonobj.getString("to"));

                                TextView trheader1 = (TextView)findViewById(R.id.trheader1);
                                trheader1.setText("Date Of Journey : "+jsonobj.getString("doj"));
                                JSONArray jaray = jsonobj.getJSONArray("details");
                                int totlcount = jaray.length();
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.leftMargin=5;
                                params.rightMargin=5;
                                params.topMargin=5;
                                params.bottomMargin=10;
                                Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                                Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                                ImageView[] img1= new ImageView[totlcount];
                                ImageView[] img2= new ImageView[totlcount];
                                ImageView[] img3= new ImageView[totlcount];
                                TextView[] tv1= new TextView[totlcount];
                                //TextView[] tv2= new TextView[totlcount];
                                //TextView[] tv3= new TextView[totlcount];
                                TextView[] tv4= new TextView[totlcount];
                                TextView[] tv5= new TextView[totlcount];
                                TextView[] tv6= new TextView[totlcount];
                                //TextView[] tv7= new TextView[totlcount];
                                LinearLayout[] ll = new LinearLayout[jaray.length()];
                                for(int i=0; i<jaray.length(); i++) {
                                    ll[i] = new LinearLayout(getApplicationContext());
                                    ll[i].setOrientation(LinearLayout.VERTICAL);
                                    ll[i].setPadding(3, 3, 3, 3);
                                    ll[i].setId(((i + 1) * 10000) + 1);
                                    ll[i].setBackgroundResource(R.drawable.pnrdiv);


                                    //table lay out 1
                                    TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tableLayoutParams.setMargins(0, 0, 0, 10);
                                    TableLayout tableLayout = new TableLayout(getApplicationContext());
                                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    TableRow tableRow = new TableRow(getApplicationContext());
                                    tableRow.setPadding(5, 5, 5, 5);

                                    img1[i] = new ImageView(getApplicationContext());
                                    img1[i].setImageResource(R.drawable.pnrtrain);
                                    tableRow.addView(img1[i], tableRowParams);

                                    tv1[i] = new TextView(getApplicationContext());
                                    tv1[i].setTextSize(10);
                                    if (Build.VERSION.SDK_INT >= 17)
                                        tv1[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                    tv1[i].setTypeface(null, Typeface.BOLD);
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        tv1[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark,getApplicationContext().getTheme()));
                                    }else {
                                        tv1[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    }
                                    String htmlText;
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        htmlText = "<body>" +
                                                "<h3>" + jaray.getJSONObject(i).getString("tnumber") + " (" + jaray.getJSONObject(i).getString("tname") + ")" + "</h3>" +
                                                "<font color=\"" + getResources().getColor(R.color.colorPnrDate,getApplicationContext().getTheme()) + "\">(" + jaray.getJSONObject(i).getString("sdeparture") + ")" + jaray.getJSONObject(i).getString("source").split("\\(")[0] + " ==> " + "(" + jaray.getJSONObject(i).getString("darrival") + ")" + jaray.getJSONObject(i).getString("destination").split("\\(")[0] + "</font>" +
                                                "</body>";
                                    }else {
                                        htmlText = "<body>" +
                                                "<h3>" + jaray.getJSONObject(i).getString("tnumber") + " (" + jaray.getJSONObject(i).getString("tname") + ")" + "</h3>" +
                                                "<font color=\"" + getResources().getColor(R.color.colorPnrDate) + "\">(" + jaray.getJSONObject(i).getString("sdeparture") + ")" + jaray.getJSONObject(i).getString("source").split("\\(")[0] + " ==> " + "(" + jaray.getJSONObject(i).getString("darrival") + ")" + jaray.getJSONObject(i).getString("destination").split("\\(")[0] + "</font>" +
                                                "</body>";
                                    }
                                    tv1[i].setText(Html.fromHtml(htmlText, null, null));
                                    tv1[i].setId(((i + 1) * 1000) + 1);
                                    tableRow.addView(tv1[i], tableRowParams);
                                    tableLayout.addView(tableRow, tableRowParams);
                                    ll[i].addView(tableLayout, tableLayoutParams);



                                    //table lay out 3
                                    TableLayout.LayoutParams tableLayoutParams2 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    TableLayout tableLayout2 = new TableLayout(getApplicationContext());
                                    tableLayout2.setColumnStretchable(0, true);
                                    tableLayout2.setColumnStretchable(1, true);
                                    TableRow.LayoutParams tableRowParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    TableRow tableRow2 = new TableRow(getApplicationContext());
                                    tableRow2.setPadding(10, 0, 0, 0);

                                    tv4[i] = new TextView(getApplicationContext());
                                    tv4[i].setTextSize(15);
                                    if (Build.VERSION.SDK_INT >= 17)
                                        tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        tv4[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark,getApplicationContext().getTheme()));
                                    }else {
                                        tv4[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    }
                                    tv4[i].setPadding(0, 0, 5, 0);
                                    tv4[i].setId(((i + 1) * 1000) + 2);
                                    try {
                                        int dst2travel = Integer.parseInt(jaray.getJSONObject(i).getString("ddistance")) - Integer.parseInt(jaray.getJSONObject(i).getString("sdistance"));
                                        tv4[i].setText("Distance : " + String.valueOf(dst2travel));
                                    }catch (Exception e)
                                    {
                                        tv4[i].setText("Distance : " + String.valueOf(0));
                                    }
                                    tableRow2.addView(tv4[i], tableRowParams3);


                                    img3[i] = new ImageView(getApplicationContext());
                                    img3[i].setId(i + 1);
                                    img3[i].setImageResource(R.drawable.pnrinfo);
                                    img3[i].setBackground(getResources().getDrawable(R.drawable.button));
                                    registerForContextMenu(img3[i]);
                                    img3[i].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openContextMenu(v);
                                        }
                                    });
                                    TableRow.LayoutParams tableRowParams7 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tableRowParams7.setMargins(0, 0, 40, 0);
                                    tableRow2.addView(img3[i], tableRowParams7);

                                    tableLayout2.addView(tableRow2, tableRowParams3);
                                    ll[i].addView(tableLayout2, tableLayoutParams2);






                                    //table lay out 4
                                    TableLayout.LayoutParams tableLayoutParams3 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    TableLayout tableLayout3 = new TableLayout(getApplicationContext());
                                    tableLayout3.setColumnStretchable (2, true);
                                    //tableLayout3.setColumnStretchable (3, true);
                                    TableRow.LayoutParams tableRowParams4 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    TableRow tableRow3= new TableRow(getApplicationContext());
                                    tableRow3.setPadding(10,0,0,20);

                                    img2[i] = new ImageView(getApplicationContext());
                                    img2[i].setImageResource(R.drawable.pnrchair);
                                    TableRow.LayoutParams tableRowParams5 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                    tableRowParams5.setMargins(0, 0, 4, 0);
                                    tableRow3.addView(img2[i], tableRowParams5);


                                    tv6[i] = new TextView(getApplicationContext());
                                    tv6[i].setTextSize(12);
                                    if (Build.VERSION.SDK_INT >= 17)
                                        tv6[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                    String statuspnr = jaray.getJSONObject(i).getString("fare");
                                    tv6[i].setTypeface(null, Typeface.BOLD);
                                    String classes = jaray.getJSONObject(i).getString("classes");
                                    String[] finalstr = classes.split(",");
                                    String[] finalstr1 = statuspnr.split(",");
                                    StringBuilder putstr = new StringBuilder();
                                    for(int y=1; y<finalstr.length; y++)
                                    {
                                        putstr.append(finalstr[y]+"("+finalstr1[y]+")");
                                        if(y < finalstr.length-1)
                                            putstr.append(" | ");
                                    }
                                    if (putstr != null) {
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            tv6[i].setTextColor(getResources().getColor(R.color.colorPnrCnf,getApplicationContext().getTheme()));
                                        }else {
                                            tv6[i].setTextColor(getResources().getColor(R.color.colorPnrCnf));
                                        }
                                        tv6[i].setText(putstr.toString());
                                    }
                                    tv6[i].setPadding(0, 0, 5, 0);
                                    tv6[i].setId(((i + 1) * 1000) + 3);
                                    tableRow3.addView(tv6[i], tableRowParams4);

                                    tv5[i] = new TextView(getApplicationContext());
                                    String idstr = jaray.getJSONObject(i).getString("tnumber")+"#"+jaray.getJSONObject(i).getString("source")+"#"+jaray.getJSONObject(i).getString("destination")+"#"+jaray.getJSONObject(i).getString("classes")+"#"+jsonobj.getString("doj");
                                    tv5[i].setText(idstr);
                                    tv5[i].setId(((i + 1) * 1000) + 4);
                                    tableRow3.addView(tv5[i], tableRowParams4);


                                    tableLayout3.addView(tableRow3, tableRowParams4);
                                    ll[i].addView(tableLayout3, tableLayoutParams3);



                                    chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                                    chat.addView(ll[i], params);
                                    ll[i].setVisibility(View.INVISIBLE);
                                }
                    /*if(chat.getVisibility() == View.INVISIBLE)
                    {
                        chat.startAnimation(slideL);
                        chat.setVisibility(View.VISIBLE);
                    }
                    chat.startAnimation(slideR);*/
                                for(int i=0; i<jaray.length(); i++)
                                {
                                    if(ll[i].getVisibility()==View.INVISIBLE){

                                        ll[i].startAnimation(slideL);
                                        ll[i].setVisibility(View.VISIBLE);
                                    }
                                    ll[i].startAnimation(slideR);
                                }

                            }
                        }catch (JSONException e){
                            context = getApplicationContext();
                            CharSequence text = "Back-end Server/Network issue. Please try again!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        //Log.d("JSON VALUE",result);
                    }
                    else
                    {
                        showToast("Invalid input / Back-end server responding very slow. Please try again!!");
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
                CharSequence text = "12Back-end Server/Network issue. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*// Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {*/
        /*if (scheduled)
            splashTimer.cancel();
        if(splashTimer!=null)
            splashTimer.purge();*/
            /*// Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.d("UnRegister ", e.getMessage());
        }*/

    }
}
