package in.ngrail.NGERail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiran on 15-12-2015.
 */
public class TrainRouteActivity extends AppCompatActivity { //implements OnMapReadyCallback, OnMarkerClickListener{
    private GoogleMap mMap;
    private static final long DELAY = 500;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_route);
        try {
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail Train Route");
            setSupportActionBar(myToolbar);
            //myToolbar.setLogo(getDrawable(R.drawable.ngraillogo));
            //myToolbar.setTitle("NGRail");
            myToolbar.setNavigationIcon(R.drawable.leftarrow);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            i = new Intent(TrainRouteActivity.this, HomeScreenActivity.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            TrainRouteActivity.this.finish();
                            TrainRouteActivity.this.startActivity(i);
                            // This makes the new screen slide up as it fades in
                            // while the current screen slides up as it fades out.
                            overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
                        }
                    }, DELAY);
                    scheduled = true;
                }
            });

            //myToolbar.setSubtitle("One Stop Train Enquiry Hub");
            myToolbar.setLogo(R.mipmap.ngrailsmlogo);
            //myToolbar.inflateMenu(R.menu.main);


            final EditText srcst = (EditText)findViewById(R.id.source_station);
            srcst.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (srcst.getText().length() == 5)
                    {
                        LinearLayout ll = (LinearLayout)findViewById(R.id.trainroute);
                        ll.removeAllViewsInLayout();
                        //api.railwayapi.com/route/train/12727/apikey/72436/
                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        loadigIcon.setVisibility(View.GONE);
                        loadigIcon.post(new Starter("http://api.ngrail.in/trainroute/trainnumber/"+srcst.getText()));
                    }
                    else
                    {
                        LinearLayout ll2 = (LinearLayout) findViewById(R.id.headerl);
                        ll2.setVisibility(View.GONE);
                        LinearLayout ll3 = (LinearLayout) findViewById(R.id.trainroute);
                        ll3.removeAllViews();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

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
    }

    class Starter implements Runnable {
        String url=null;
        public Starter(String str){
            url=str;
        }
        public void run() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            final String phonenum = sharedpreferences.getString("name", null);
            //start Asyn Task here
            //Log.d("URL", url + "/key/" + phonenum + "/");
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
                //Log.d("AAA", result);
                if (result.equals("0\n")) {
                    CharSequence text = "Enquiry not possible in Tatkal Time";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                try {
                    JSONObject jsonobj = new JSONObject(result);
                    if (jsonobj.getString("response_code").equals("200")) {

                        JSONArray jsnarr = jsonobj.getJSONArray("route");


                        TextView trheader2 = (TextView)findViewById(R.id.trheader2);

                        trheader2.setText(jsonobj.getJSONObject("train").getString("number")+" ( "+jsonobj.getJSONObject("train").getString("name")+" )");

                        TextView trheader = (TextView)findViewById(R.id.trheader);
                        String days1 = null;
                        JSONArray days = jsonobj.getJSONObject("train").getJSONArray("days");
                        for(int i=0; i< days.length(); i++)
                        {
                            if(days.getJSONObject(i).getString("runs").equals("Y")) {
                                if(days1==null)
                                    days1 = days.getJSONObject(i).getString("day-code");
                                else
                                    days1 = days1.concat(days.getJSONObject(i).getString("day-code"));
                                days1 = days1.concat(" | ");
                            }
                        }
                        if(days1!=null)
                        {
                            days1 = days1.substring(0,days1.length()-2);
                        }

                        trheader.setText("Running Days : "+days1);

                        TextView trheader1 = (TextView)findViewById(R.id.trheader1);

                        JSONArray cls = jsonobj.getJSONObject("train").getJSONArray("classes");

                        String cls1 = null;
                        for(int i=0; i< cls.length(); i++)
                        {
                            if(cls.getJSONObject(i).getString("available").equals("Y")) {
                                if(cls1 == null)
                                    cls1 =cls.getJSONObject(i).getString("class-code");
                                else
                                    cls1 = cls1.concat(cls.getJSONObject(i).getString("class-code"));
                                cls1 = cls1.concat(" | ");
                            }
                        }
                        if(cls1!=null)
                        {
                            cls1 = cls1.substring(0,cls1.length()-2);
                        }
                        trheader1.setText("Classes Available : "+cls1);

                        int totalcnt = jsnarr.length();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin=5;
                        params.rightMargin=5;
                        params.topMargin=5;
                        params.bottomMargin=5;
                        LinearLayout chat = (LinearLayout)findViewById(R.id.trainroute);
                        chat.removeAllViewsInLayout();
                        TextView[] tv1= new TextView[totalcnt];
                        TextView[] tv2= new TextView[totalcnt];
                        TextView[] tv3= new TextView[totalcnt];
                        TextView[] tv4= new TextView[totalcnt];
                        TextView[] tv5= new TextView[totalcnt];
                        LinearLayout[] ll=new LinearLayout[jsnarr.length()];
                        for(int i=0; i<totalcnt; i++)
                        {

                            int rrg = R.color.colorPrimaryDark;
                            int rrg1 = R.color.colorPrimaryDark;
                            ll[i] = new LinearLayout(getApplicationContext());
                            ll[i].setOrientation(LinearLayout.VERTICAL);
                            ll[i].setPadding(3, 3, 3, 3);
                            //ll.setId(((i + 1) * 10000) + 1);
                            ll[i].setBackgroundResource(R.drawable.pnrdiv);
                            TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 	TableLayout.LayoutParams.WRAP_CONTENT);
                            tableLayoutParams.setMargins(0, 0, 0, 10);
                            TableLayout tableLayout = new TableLayout(getApplicationContext());
                            tableLayout.setStretchAllColumns(true);
                            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 	TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                            TableRow tableRow = new TableRow(getApplicationContext());
                            tableRow.setPadding(5, 5, 5, 5);
                            if(i==0)
                            {
                                LinearLayout ll1 = new LinearLayout(getApplicationContext());
                                ll1.setOrientation(LinearLayout.VERTICAL);
                                ll1.setPadding(3, 3, 3, 3);
                                ll1.setBackgroundResource(R.drawable.pnrdiv);
                                TableLayout tableLayout1 = new TableLayout(getApplicationContext());
                                tableLayout1.setStretchAllColumns(true);
                                TableRow.LayoutParams tableRowParams1 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 	TableRow.LayoutParams.WRAP_CONTENT,1.0f);
                                TableRow tableRow1 = new TableRow(getApplicationContext());
                                tableRow1.setPadding(5, 5, 5, 5);
                                tableRow1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                tv1[i] = new TextView(getApplicationContext());
                                tv1[i].setTextSize(15);
                                tv1[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv1[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv1[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv1[i].setText("Station");
                                tv1[i].setMinWidth(100);
                                tableRow1.addView(tv1[i], tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);



                                tv2[i] = new TextView(getApplicationContext());
                                tv2[i].setTextSize(15);
                                tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv2[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv2[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv2[i].setText("Arrival");
                                tv2[i].setMinWidth(100);
                                tableRow1.addView(tv2[i], tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);


                                tv3[i] = new TextView(getApplicationContext());
                                tv3[i].setTextSize(15);
                                tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv3[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv3[i].setText("Departure");
                                tv3[i].setMinWidth(130);
                                tableRow1.addView(tv3[i],tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);


                                tv4[i] = new TextView(getApplicationContext());
                                tv4[i].setTextSize(15);
                                tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv4[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv4[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv4[i].setText("Distance");
                                tv4[i].setMinWidth(100);
                                //tv4[i].setId(((i + 1) * 1000) + 4);
                                tableRow1.addView(tv4[i], tableRowParams);

                                tv5[i] = new TextView(getApplicationContext());
                                tv5[i].setTextSize(15);
                                tv5[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv5[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv5[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv5[i].setText("Day");
                                tv5[i].setMinWidth(100);
                                //tv4[i].setId(((i + 1) * 1000) + 4);
                                tableRow1.addView(tv5[i], tableRowParams);

                                tableLayout1.addView(tableRow1, tableRowParams);
                                ll1.addView(tableLayout1, tableLayoutParams);
                                chat.addView(ll1, params);
                            }
                            tv1[i] = new TextView(getApplicationContext());
                            tv1[i].setTextSize(15);
                            tv1[i].setMinWidth(100);
                            tv1[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv1[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv1[i].setTextColor(getResources().getColor(rrg1));
                            tv1[i].setText(jsnarr.getJSONObject(i).getString("code"));
                            //tv1[i].setId(((i + 1) * 1000) + 1);
                            tableRow.addView(tv1[i], tableRowParams);
                            //tableLayout.addView(tableRow, tableRowParams);
                            //ll.addView(tableLayout, tableLayoutParams);



                            tv2[i] = new TextView(getApplicationContext());
                            tv2[i].setTextSize(15);
                            tv2[i].setMinWidth(100);
                            tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv2[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv2[i].setTextColor(getResources().getColor(rrg1));
                            tv2[i].setText(jsnarr.getJSONObject(i).getString("scharr"));
                            //tv2[i].setId(((i + 1) * 1000) + 2);
                            tableRow.addView(tv2[i], tableRowParams);
                            //tableLayout.addView(tableRow, tableRowParams);
                            //ll.addView(tableLayout, tableLayoutParams);


                            tv3[i] = new TextView(getApplicationContext());
                            tv3[i].setTextSize(15);
                            tv3[i].setMinWidth(100);
                            tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv3[i].setTextColor(getResources().getColor(rrg1));
                            tv3[i].setText(jsnarr.getJSONObject(i).getString("schdep"));
                            //tv3[i].setId(((i + 1) * 1000) + 3);
                            tableRow.addView(tv3[i],tableRowParams);
                            //tableLayout.addView(tableRow, tableRowParams);
                            //ll.addView(tableLayout, tableLayoutParams);


                            tv4[i] = new TextView(getApplicationContext());
                            tv4[i].setTextSize(15);
                            tv4[i].setMinWidth(100);
                            tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv4[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv4[i].setTextColor(getResources().getColor(rrg));
                            tv4[i].setText(jsnarr.getJSONObject(i).getString("distance"));
                            //tv4[i].setId(((i + 1) * 1000) + 4);
                            tableRow.addView(tv4[i], tableRowParams);

                            tv5[i] = new TextView(getApplicationContext());
                            tv5[i].setTextSize(15);
                            tv5[i].setMinWidth(100);
                            tv5[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv5[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv5[i].setTextColor(getResources().getColor(rrg));
                            tv5[i].setText(jsnarr.getJSONObject(i).getString("day"));
                            //tv4[i].setId(((i + 1) * 1000) + 4);
                            tableRow.addView(tv5[i], tableRowParams);
                            tableLayout.addView(tableRow, tableRowParams);
                            ll[i].addView(tableLayout, tableLayoutParams);

                            chat.addView(ll[i], params);
                            ll[i].setVisibility(View.INVISIBLE);
                        }
                        Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                        Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                        LinearLayout ll2 = (LinearLayout) findViewById(R.id.headerl);
                        if(ll2.getVisibility()==View.GONE){

                            ll2.startAnimation(slideL);
                            ll2.setVisibility(View.VISIBLE);
                        }
                        ll2.startAnimation(slideR);
                        for(int i=0; i<jsnarr.length(); i++)
                        {
                            if(ll[i].getVisibility()==View.INVISIBLE){

                                ll[i].startAnimation(slideL);
                                ll[i].setVisibility(View.VISIBLE);
                            }
                            ll[i].startAnimation(slideR);
                        }
                        //Log.d("JSON VALUE", result);
                    } else {
                        showToast("Invalid input / Back-end server responding very slow. Please try again!!");
                    }
                } catch (JSONException j) {
                    context = getApplicationContext();
                    CharSequence text = "Back-end Server/Network issue. Please try again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            } catch (Exception e) {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server/Network issue. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // your code.
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
                i = new Intent(TrainRouteActivity.this, HomeScreenActivity.class);
                i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                TrainRouteActivity.this.finish();
                TrainRouteActivity.this.startActivity(i);
                // This makes the new screen slide up as it fades in
                // while the current screen slides up as it fades out.
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
            }
        }, DELAY);
        scheduled = true;
        return;
    }
    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }
}
