package in.ngrail.NGRail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiran on 11-12-2015.
 */
public class AdvSeatAvail extends AppCompatActivity{
    private static final long DELAY = 500;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    public String dateval = null;
    public String jsonvalue = "";
    int selectedid = 0;
    String Pnrstr = null;
    private String source_fina = null;
    private String dest_fina = null;
    private String train_fina = null;
    private String doj_fina = null;
    private String class_fina = null;
    private String quota_fina = "GN";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advseatavail);
        Bundle bundle = getIntent().getExtras();
        jsonvalue = bundle.getString("jsonvalue");
        String jsonval = bundle.getString("jsonval");
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
                            i = new Intent(AdvSeatAvail.this, TrainsBetweenTwoStationsList.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            i.putExtra("jsonvalue", jsonvalue);
                            i.putExtra("dateval", dateval);
                            AdvSeatAvail.this.finish();
                            AdvSeatAvail.this.startActivity(i);
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
            myToolbar.inflateMenu(R.menu.quota);

            try{
                JSONObject jsn = new JSONObject(jsonval);
                TextView trheader = (TextView)findViewById(R.id.trheader);
                trheader.setText(jsn.getString("source")+" > "+jsn.getString("destination"));
                try {
                    String splits = jsn.getString("source").split("\\(")[1];
                    source_fina = splits.substring(0,splits.length()-1);
                }catch (Exception e)
                {
                    ;
                }
                try {
                    String splits = jsn.getString("destination").split("\\(")[1];
                    dest_fina = splits.substring(0,splits.length()-1);
                }catch (Exception e)
                {
                    ;
                }
                TextView trheader1 = (TextView)findViewById(R.id.trheader1);
                trheader1.setText("Date Of Journey : "+jsn.getString("doj"));
                TextView trheader3 = (TextView)findViewById(R.id.trheader3);
                trheader3.setText("Class : "+jsn.getString("class").split(",")[0]+" "+"Gen Fare : "+jsn.getString("orgfare")+" Tatkal Fare : "+jsn.getString("tatfare"));
                doj_fina = jsn.getString("doj");
                class_fina = jsn.getString("class").split(",")[0];
                TextView trheader2 = (TextView)findViewById(R.id.trheader2);
                trheader2.setText(jsn.getString("tnumber")+" ( "+jsn.getString("tname")+" ) "+jsn.getString("type"));
                train_fina = jsn.getString("tnumber");
                int tatfare = Integer.parseInt(jsn.getString("tatfare"))-Integer.parseInt(jsn.getString("orgfare"));
                JSONArray jsnarr = jsn.getJSONArray("avail");
                int totalcnt = jsnarr.length();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin=5;
                params.rightMargin=5;
                params.topMargin=5;
                params.bottomMargin=5;
                Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                LinearLayout chat = (LinearLayout)findViewById(R.id.pnrstatusnum);
                TextView[] tv1= new TextView[totalcnt];
                TextView[] tv2= new TextView[totalcnt];
                TextView[] tv3= new TextView[totalcnt];
                TextView[] tv4= new TextView[totalcnt];
                LinearLayout[] ll=new LinearLayout[jsnarr.length()];
                for(int i=0; i<totalcnt; i++)
                {

                    int rrg=R.color.colorPnrWl;
                    int rrg1=R.color.colorPrimaryDark;
                    if(tatfare >= Integer.parseInt(jsnarr.getJSONObject(i).getString("farediff")))
                        rrg=R.color.colorPnrCnf;
                    ll[i] = new LinearLayout(getApplicationContext());
                    ll[i].setOrientation(LinearLayout.VERTICAL);
                    ll[i].setPadding(3, 3, 3, 3);
                    ll[i].setId(((i + 1) * 10000) + 1);
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
                        ll1.setId(((i + 1) * 10000) + 1);
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
                        tv1[i].setText("Boarding");
                        tv1[i].setMinWidth(100);
                        tableRow1.addView(tv1[i], tableRowParams);
                        //tableLayout.addView(tableRow, tableRowParams);
                        //ll.addView(tableLayout, tableLayoutParams);



                        tv2[i] = new TextView(getApplicationContext());
                        tv2[i].setTextSize(15);
                        tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv2[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                        tv2[i].setTextColor(getResources().getColor(R.color.colorAccent));
                        tv2[i].setMinWidth(100);
                        tv2[i].setText("Aliting");
                        tableRow1.addView(tv2[i], tableRowParams);
                        //tableLayout.addView(tableRow, tableRowParams);
                        //ll.addView(tableLayout, tableLayoutParams);


                        tv3[i] = new TextView(getApplicationContext());
                        tv3[i].setTextSize(15);
                        tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                        tv3[i].setTextColor(getResources().getColor(R.color.colorAccent));
                        tv3[i].setMinWidth(130);
                        tv3[i].setText("Status");
                        tableRow1.addView(tv3[i],tableRowParams);
                        //tableLayout.addView(tableRow, tableRowParams);
                        //ll.addView(tableLayout, tableLayoutParams);


                        tv4[i] = new TextView(getApplicationContext());
                        tv4[i].setTextSize(15);
                        tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv4[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                        tv4[i].setTextColor(getResources().getColor(R.color.colorAccent));
                        tv4[i].setMinWidth(100);
                        tv4[i].setText("Fare Diff");
                        //tv4[i].setId(((i + 1) * 1000) + 4);
                        tableRow1.addView(tv4[i], tableRowParams);

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
                    tv1[i].setText(jsnarr.getJSONObject(i).getString("from"));
                    //tv1[i].setId(((i + 1) * 1000) + 1);
                    tableRow.addView(tv1[i], tableRowParams);
                    tableRow.setMinimumWidth(100);
                    tableRow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //tableLayout.addView(tableRow, tableRowParams);
                    //ll.addView(tableLayout, tableLayoutParams);



                    tv2[i] = new TextView(getApplicationContext());
                    tv2[i].setTextSize(15);
                    tv2[i].setMinWidth(100);
                    tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv2[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    tv2[i].setTextColor(getResources().getColor(rrg1));
                    tv2[i].setText(jsnarr.getJSONObject(i).getString("to"));
                    //tv2[i].setId(((i + 1) * 1000) + 2);
                    tableRow.addView(tv2[i], tableRowParams);
                    tableRow.setMinimumWidth(100);
                    tableRow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //tableLayout.addView(tableRow, tableRowParams);
                    //ll.addView(tableLayout, tableLayoutParams);


                    tv3[i] = new TextView(getApplicationContext());
                    tv3[i].setTextSize(15);
                    tv3[i].setMinWidth(130);
                    tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    tv3[i].setTextColor(getResources().getColor(rrg1));
                    tv3[i].setText(jsnarr.getJSONObject(i).getString("status"));
                    //tv3[i].setId(((i + 1) * 1000) + 3);
                    tableRow.addView(tv3[i], tableRowParams);
                    tableRow.setMinimumWidth(300);
                    tableRow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //tableLayout.addView(tableRow, tableRowParams);
                    //ll.addView(tableLayout, tableLayoutParams);


                    tv4[i] = new TextView(getApplicationContext());
                    tv4[i].setTextSize(15);
                    tv4[i].setMinWidth(100);
                    tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv4[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                    tv4[i].setTextColor(getResources().getColor(rrg));
                    tv4[i].setText(jsnarr.getJSONObject(i).getString("farediff"));
                    //tv4[i].setId(((i + 1) * 1000) + 4);
                    tableRow.addView(tv4[i], tableRowParams);
                    tableRow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tableRow.setMinimumWidth(100);
                    tableLayout.addView(tableRow, tableRowParams);
                    ll[i].addView(tableLayout, tableLayoutParams);

                    chat.addView(ll[i], params);
                    ll[i].setVisibility(View.INVISIBLE);
                }
                for(int i=0; i<jsnarr.length(); i++)
                {
                    if(ll[i].getVisibility()==View.INVISIBLE){

                        ll[i].startAnimation(slideL);
                        ll[i].setVisibility(View.VISIBLE);
                    }
                    ll[i].startAnimation(slideR);
                }
            }catch (JSONException e)
            {
                showToast("Back-End server issue Please try again!!"+e.getMessage());
            }
            catch (Exception e)
            {
                showToast("Back-End server issue Please try again!!"+e.getMessage());
            }
        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
            int duration = Toast.LENGTH_LONG;
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
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(doj_fina));
                    c.add(Calendar.DATE, -1);  // number of days to add
                    doj_fina = sdf.format(c.getTime());  // dt is now the new date

                    loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                    loadingLayout.setVisibility(View.GONE);

                    loadigText = (TextView) findViewById(R.id.textView111);
                    loadigText.setVisibility(View.GONE);

                    loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                    loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                    if (quota_fina.equals("GN")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
                    }
                    else if (quota_fina.equals("LB")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
                    }
                    else if (quota_fina.equals("LD")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
                    }
                    else if (quota_fina.equals("SS")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchss/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
                    }
                    //loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                    loadigIcon.setVisibility(View.GONE);

                    //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();

                    // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                    //loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));

                }catch (ParseException e)
                {
                    showToast("Date format Wrong. Please Contact Admin!!");
                }catch (Exception e)
                {
                    showToast(e.getMessage());
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
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(doj_fina));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    doj_fina = sdf.format(c.getTime());  // dt is now the new date


                    loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                    loadingLayout.setVisibility(View.GONE);

                    loadigText = (TextView) findViewById(R.id.textView111);
                    loadigText.setVisibility(View.GONE);

                    loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                    loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                    //loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                    if (quota_fina.equals("GN")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/" + source_fina + "/destination/" + dest_fina + "/doj/" + doj_fina + "/train/" + train_fina + "/class/" + class_fina));
                    } else if (quota_fina.equals("LB")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/" + source_fina + "/destination/" + dest_fina + "/doj/" + doj_fina + "/train/" + train_fina + "/class/" + class_fina));
                    } else if (quota_fina.equals("LD")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/" + source_fina + "/destination/" + dest_fina + "/doj/" + doj_fina + "/train/" + train_fina + "/class/" + class_fina));
                    } else if (quota_fina.equals("SS")) {
                        loadigIcon.post(new Starter("http://api.ngrail.in/advsearchss/source/" + source_fina + "/destination/" + dest_fina + "/doj/" + doj_fina + "/train/" + train_fina + "/class/" + class_fina));
                    }
                    loadigIcon.setVisibility(View.GONE);

                    //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();

                    // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                    //loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));

                } catch (ParseException e) {
                    showToast("Date format Wrong. Please Contact Admin!!");
                } catch (Exception e) {
                    showToast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // your code.
        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i;
                i = new Intent(AdvSeatAvail.this, TrainsBetweenTwoStationsList.class);
                i.putExtra("jsonvalue", jsonvalue);
                i.putExtra("dateval", dateval);
                i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                AdvSeatAvail.this.finish();
                AdvSeatAvail.this.startActivity(i);
                // This makes the new screen slide up as it fades in
                // while the current screen slides up as it fades out.
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
            }
        }, DELAY);
        scheduled = true;
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quota, menu);
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        //if (v.getId()==R.id.usernameEdittext) {
        //getMenuInflater().inflate(R.menu.contextmenu, menu);
        //
        /*if(v.getId()>0) {
            menu.setHeaderTitle("Select Option");
            menu.setHeaderIcon(R.drawable.ngrailsmlogo);
            menu.add(0, 0, 0, "GN");
            menu.add(0, 1, 0, "LD");
            menu.add(0, 2, 0, "SS");
            menu.add(0, 3, 0, "LB");
        }*/
        //menu.add(0, id2, 0, "Check For Duplicate"+pnr);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
        loadingLayout.setVisibility(View.GONE);

        loadigText = (TextView) findViewById(R.id.textView111);
        loadigText.setVisibility(View.GONE);

        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
        loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
        loadigIcon.setVisibility(View.GONE);
        //String[] dojsp=doj_fina.split("-");
        //String dojf = dojsp[2]+"-"+dojsp[1]+"-"+dojsp[0];
        if (id == R.id.gn) {
            quota_fina = "GN";
            loadigIcon.post(new Starter("http://api.ngrail.in/advsearch/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
            return true;
        }
        else if (id == R.id.lb) {
            quota_fina = "LB";
            loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
            return true;
        }
        else if (id == R.id.ld) {
            quota_fina="LD";
            loadigIcon.post(new Starter("http://api.ngrail.in/advsearchlb/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
            return true;
        }
        else if (id == R.id.ss) {
            quota_fina = "SS";
            loadigIcon.post(new Starter("http://api.ngrail.in/advsearchss/source/"+source_fina+"/destination/"+dest_fina+"/doj/"+doj_fina+"/train/"+train_fina+"/class/"+class_fina));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

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
                CharSequence text = "Back-end Server issue. Please try again!" + e.getMessage();
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
                if (result.equals("0\n")) {
                    CharSequence text = "Enquiry not possible in Tatkal Time";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                } else {
                    try{
                        JSONObject jsn = new JSONObject(result);
                        TextView trheader = (TextView)findViewById(R.id.trheader);
                        trheader.setText(jsn.getString("source")+" > "+jsn.getString("destination"));
                        try {
                            String splits = jsn.getString("source").split("\\(")[1];
                            source_fina = splits.substring(0,splits.length()-1);
                        }catch (Exception e)
                        {
                            ;
                        }
                        try {
                            String splits = jsn.getString("destination").split("\\(")[1];
                            dest_fina = splits.substring(0,splits.length()-1);
                        }catch (Exception e)
                        {
                            ;
                        }
                        TextView trheader1 = (TextView)findViewById(R.id.trheader1);
                        trheader1.setText("Date Of Journey : "+jsn.getString("doj"));
                        TextView trheader3 = (TextView)findViewById(R.id.trheader3);
                        trheader3.setText("Class : "+jsn.getString("class").split(",")[0]+" "+"Gen Fare : "+jsn.getString("orgfare")+" Tatkal Fare : "+jsn.getString("tatfare"));
                        doj_fina = jsn.getString("doj");
                        class_fina = jsn.getString("class").split(",")[0];
                        TextView trheader2 = (TextView)findViewById(R.id.trheader2);
                        trheader2.setText(jsn.getString("tnumber")+" ( "+jsn.getString("tname")+" ) "+jsn.getString("type"));
                        train_fina = jsn.getString("tnumber");
                        int tatfare = Integer.parseInt(jsn.getString("tatfare"))-Integer.parseInt(jsn.getString("orgfare"));
                        JSONArray jsnarr = jsn.getJSONArray("avail");
                        int totalcnt = jsnarr.length();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin=5;
                        params.rightMargin=5;
                        params.topMargin=5;
                        params.bottomMargin=5;
                        LinearLayout chat = (LinearLayout)findViewById(R.id.pnrstatusnum);
                        chat.removeAllViewsInLayout();
                        TextView[] tv1= new TextView[totalcnt];
                        TextView[] tv2= new TextView[totalcnt];
                        TextView[] tv3= new TextView[totalcnt];
                        TextView[] tv4= new TextView[totalcnt];
						LinearLayout[] ll=new LinearLayout[jsnarr.length()];
                        for(int i=0; i<totalcnt; i++)
                        {

                            int rrg = R.color.colorPnrWl;
                            int rrg1 = R.color.colorPrimaryDark;
                            if(tatfare >= Integer.parseInt(jsnarr.getJSONObject(i).getString("farediff")))
                                rrg=R.color.colorPnrCnf;
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
								ll1.setId(((i + 1) * 10000) + 1);
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
                                tv1[i].setText("Boarding");
                                tv1[i].setMinWidth(100);
                                tableRow1.addView(tv1[i], tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);



                                tv2[i] = new TextView(getApplicationContext());
                                tv2[i].setTextSize(15);
                                tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv2[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv2[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv2[i].setText("Aliting");
                                tv2[i].setMinWidth(100);
                                tableRow1.addView(tv2[i], tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);


                                tv3[i] = new TextView(getApplicationContext());
                                tv3[i].setTextSize(15);
                                tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv3[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv3[i].setText("Status");
                                tv3[i].setMinWidth(130);
                                tableRow1.addView(tv3[i],tableRowParams);
                                //tableLayout.addView(tableRow, tableRowParams);
                                //ll.addView(tableLayout, tableLayoutParams);


                                tv4[i] = new TextView(getApplicationContext());
                                tv4[i].setTextSize(15);
                                tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tv4[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                                tv4[i].setTextColor(getResources().getColor(R.color.colorAccent));
                                tv4[i].setText("Fare Diff");
                                tv4[i].setMinWidth(100);
                                //tv4[i].setId(((i + 1) * 1000) + 4);
                                tableRow1.addView(tv4[i], tableRowParams);

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
                            tv1[i].setText(jsnarr.getJSONObject(i).getString("from"));
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
                            tv2[i].setText(jsnarr.getJSONObject(i).getString("to"));
                            //tv2[i].setId(((i + 1) * 1000) + 2);
                            tableRow.addView(tv2[i], tableRowParams);
                            //tableLayout.addView(tableRow, tableRowParams);
                            //ll.addView(tableLayout, tableLayoutParams);


                            tv3[i] = new TextView(getApplicationContext());
                            tv3[i].setTextSize(15);
                            tv3[i].setMinWidth(130);
                            tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv3[i].setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                            tv3[i].setTextColor(getResources().getColor(rrg1));
                            tv3[i].setText(jsnarr.getJSONObject(i).getString("status"));
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
                            tv4[i].setText(jsnarr.getJSONObject(i).getString("farediff"));
                            //tv4[i].setId(((i + 1) * 1000) + 4);
                            tableRow.addView(tv4[i], tableRowParams);
                            tableLayout.addView(tableRow, tableRowParams);
                            ll[i].addView(tableLayout, tableLayoutParams);

                            chat.addView(ll[i], params);
							ll[i].setVisibility(View.INVISIBLE);
						}
                        Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                        Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
						for(int i=0; i<jsnarr.length(); i++)
						{
							if(ll[i].getVisibility()==View.INVISIBLE){

								ll[i].startAnimation(slideL);
								ll[i].setVisibility(View.VISIBLE);
							}
							ll[i].startAnimation(slideR);
						}
                    }catch (JSONException e)
                    {
                        showToast("Back-End server issue Please try again!!"+e.getMessage());
                    }
                    catch (Exception e)
                    {
                        showToast("Back-End server issue Please try again!!"+e.getMessage());
                    }
                    //Log.d("ASAAS", jsonvalue + String.valueOf(dateval));
                }

            } catch (Exception e) {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server issue. Please try again!" + e.getMessage();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
