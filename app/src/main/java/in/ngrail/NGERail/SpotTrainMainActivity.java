package in.ngrail.NGERail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SpotTrainMainActivity extends AppCompatActivity{
    //static final LatLng TutorialsPoint = new LatLng(21 , 57);
    //private static final long DELAY = 500;
    //private boolean scheduled = false;
    //private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    private String source=null;
    private String destination=null;
    private String dateofjourney=null;
    //private String dateval=null;
    public JSONObject routejson=null;
    //int selectedid = 0;
    //String Pnrstr = null;
    SharedPreferences sharedpreferences;
    //private GoogleMap mMap;
    private String  refreshstat=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spottrain_main);
        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail Spot Train");
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
                            SpotTrainMainActivity.this.finish();
                            i = new Intent(SpotTrainMainActivity.this, HomeScreenActivity.class);
                            //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);

                            SpotTrainMainActivity.this.startActivity(i);
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
                        //api.railwayapi.com/route/train/12727/apikey/72436/
                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        if (Build.VERSION.SDK_INT >= 23) {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getApplicationContext().getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                        else {
                            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                        loadigIcon.setVisibility(View.GONE);
                        loadigIcon.post(new Starter("http://api.ngrail.in/trainroute/trainnumber/"+srcst.getText()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            /*srcst.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return false;
                }
            });*/
        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server/Network issue. Please try again!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        /*try {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap == null) {
                mMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Marker TP = mMap.addMarker(new MarkerOptions().
                    position(TutorialsPoint).title("TutorialsPoint"));
        }catch (Exception e)
        {
            Log.d("Exception",e.getMessage());
        }*/

        //Search button event
        Button Searchtrain = (Button) findViewById(R.id.Searchtrain);
        Searchtrain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                    Context context = getApplicationContext();
                    CharSequence text = "Back-end Server issue. Please try again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                Context context = getApplicationContext();
                source = ((EditText) findViewById(R.id.source_station)).getText().toString();


                Spinner spo = (Spinner) findViewById(R.id.spinner);
                destination = String.valueOf(spo.getSelectedItem()).split("\\(")[1];
                destination = destination.substring(0,destination.length()-1);
                int jrday=1;
                try {
                    JSONArray arr= routejson.getJSONArray("route");
                    for (int i = 0; i < arr.length(); i++) {
                        //Log.d("PPP",arr.getJSONObject(i).getString("code")+"!!!"+destination+"~~"+arr.getJSONObject(i).getInt("day"));
                        if(arr.getJSONObject(i).getString("code").equals(destination))
                        {
                            jrday = arr.getJSONObject(i).getInt("day");
                            break;
                        }

                    }
                }catch (JSONException e)
                {
                    //Log.d("ASA",e.getMessage());
                }

                Spinner spo1 = (Spinner) findViewById(R.id.spinner1);
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                //Date date = new Date();
                Calendar cal = Calendar.getInstance();
                if(spo1.getSelectedItemId()==0)
                {
                    int y = -jrday;
                    //cal.setTime(date);
                    cal.add(Calendar.DATE,y);
                }
                else if(spo1.getSelectedItemId()==1)
                {
                    int y = -jrday+1;
                    //cal.setTime(date);
                    cal.add(Calendar.DATE,y);
                }
                else
                {
                    int y = -jrday+2;
                    //cal.setTime(date);
                    cal.add(Calendar.DATE,y);
                }
                dateofjourney = sf.format(cal.getTime());
                //Log.d("AAAA",source+"~~~"+destination+"~~"+dateofjourney+"~~~"+spo1.getSelectedItemId());
                if (source.length() <= 0 || destination.length() <= 0 || dateofjourney.length() <= 0) {
                    CharSequence text = "*All the fields are Required. Please Enter!!!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                CharSequence text = "Getting trains status for " + source + " from " + destination + " for the date " + dateofjourney;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                loadingLayout.setVisibility(View.GONE);

                loadigText = (TextView) findViewById(R.id.textView111);
                loadigText.setVisibility(View.GONE);

                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                if (Build.VERSION.SDK_INT >= 23) {
                    loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getApplicationContext().getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                else {
                    loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
                loadigIcon.setVisibility(View.GONE);
                refreshstat = "http://api.ngrail.in/trainlivestat1/trainnum/" + source + "/doj/" + dateofjourney;
                loadigIcon.post(new Starter_live("http://api.ngrail.in/trainlivestat1/trainnum/" + source + "/doj/" + dateofjourney));

                //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();

                // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                //String[] dojsp=dateofjourney.split("-");
                //String dojf = dojsp[2]+"-"+dojsp[1]+"-"+dojsp[0];

                //loadigIcon.post(new Starter_live("http://api.ngrail.in/trainlivestat1/trainnum/" + source + "/doj/" + dateofjourney));
                //new DownloadTask().execute("http://api.ngrail.in/tbtswd/source/"+source.split(" ")[0]+"/destination/"+destination.split(" ")[0]+"/doj/"+dateofjourney.substring(0,dateofjourney.lastIndexOf("-")));
            }
        });

    }

    class Starter_live implements Runnable {
        String url=null;
        public Starter_live(String str){
            url=str;
        }
        public void run() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            final String phonenum = sharedpreferences.getString("name", null);
            //EditText et1 = (EditText) findViewById(R.id.qqqq);
            //et1.setText("QQQ"+url+"/key/"+phonenum+"/");
            //start Asyn Task here
            //Log.d("URL",url+"/key/"+phonenum+"/");
            new DownloadTask_live().execute(url+"/key/"+phonenum+"/");
        }
    }

    private class DownloadTask_live extends AsyncTask<String, Void, String> {

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
                //Log.d("AAA",result);
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
                    if(jsonobj.getInt("response_code")==200) {
                        JSONArray jarr = jsonobj.getJSONArray("route");
                        //StringBuilder valstr = new StringBuilder();
                        StringBuilder ggg = new StringBuilder();
                        String prevstop;
                        String nextstop;
                        int t = 0;
                        int prevdist = Integer.parseInt(jarr.getJSONObject(jarr.length() - 1).getString("distance"));
                        int needdest = 0;
                        for (int j = jarr.length()-1; j >=0; j--) {
                            if(jarr.getJSONObject(j).getString("code").equals(destination))
                            {
                                needdest = Integer.parseInt(jarr.getJSONObject(j).getString("distance"));
                                break;
                            }
                        }
                        //Log.d("ASASS","~~"+needdest+"!!!"+destination);
                        for (int j = jarr.length()-1; j >=0; j--) {
                            int distobetravel = needdest - Integer.parseInt(jarr.getJSONObject(j).getString("distance"));
                            //Log.d("ASASS","~~"+needdest+"~~~"+Integer.parseInt(jarr.getJSONObject(j).getString("distance"))+"~~~"+distobetravel);
                            /*valstr.append(jarr.getJSONObject(j).getString("lat") );
                            valstr.append(",");
                            valstr.append(jarr.getJSONObject(j).getString("lng") );
                            valstr.append(",");
                            valstr.append(jarr.getJSONObject(j).getString("name") );
                            valstr.append(",");
                            valstr.append(jarr.getJSONObject(j).getString("sarr") );
                            valstr.append("/" );
                            valstr.append(jarr.getJSONObject(j).getString("sdep") );
                            valstr.append("-" );
                            valstr.append(jarr.getJSONObject(j).getString("aarr") );
                            valstr.append("/" );
                            valstr.append(jarr.getJSONObject(j).getString("adep") );
                            valstr.append(",");
                            valstr.append(jarr.getJSONObject(j).getString("name") );
                            valstr.append("(" );
                            valstr.append(jarr.getJSONObject(j).getString("code") );
                            valstr.append(")\n");
                            valstr.append(jarr.getJSONObject(j).getString("sarr") );
                            valstr.append("/" );
                            valstr.append(jarr.getJSONObject(j).getString("sdep") );
                            valstr.append("\t");
                            valstr.append(jarr.getJSONObject(j).getString("aarr") );
                            valstr.append("/" );
                            valstr.append(jarr.getJSONObject(j).getString("adep") );
                            valstr.append("\n");
                            valstr.append(jarr.getJSONObject(j).getString("status") );
                            valstr.append("\t Distance to travel : " );
                            valstr.append(distobetravel );
                            valstr.append("\n");
                            valstr.append("Current Position : " );
                            valstr.append(jsonobj.getString("currpos"));
                            valstr.append(",");*/

                            if (jarr.getJSONObject(j).getInt("isreached") == 1 && t == 0) {
                                prevstop = jarr.getJSONObject(j).getString("name") + "(" + jarr.getJSONObject(j).getString("code") + ")";
                                /*else
                                    prevstop = "At Source station.";*/
                                if(j!=jarr.length()-1)
                                    nextstop = jarr.getJSONObject(j+1).getString("name") + "(" + jarr.getJSONObject(j+1).getString("code") + ")";
                                else
                                    nextstop = "reached destination";
                                ggg.append(jsonobj.getString("train"));
                                ggg.append("\t");
                                ggg.append(jarr.getJSONObject(0).getString("name"));
                                ggg.append("==>");
                                ggg.append(jarr.getJSONObject(jarr.length()-1).getString("name"));
                                ggg.append("\n");
                                ggg.append("Current Position : ");
                                ggg.append(jsonobj.getString("currpos") );
                                ggg.append("\n");
                                ggg.append("Previous: " );
                                ggg.append(prevstop );
                                ggg.append("\t");
                                ggg.append("Next : " );
                                ggg.append(nextstop );
                                ggg.append("\n");
                                if(distobetravel<0) {
                                    ggg.append("Distance a head : " );
                                    ggg.append(distobetravel*-1 );
                                    ggg.append("\n");
                                }
                                else {
                                    ggg.append("Distance to travel : " );
                                    ggg.append(distobetravel );
                                    ggg.append("\n");
                                }
                                t = 1;
                            }
                            /*if(t==0) {
                                valstr.append(jarr.getJSONObject(j).getInt("isreached"));
                                valstr.append(",");
                            }
                            else {
                                valstr.append("1" );
                                valstr.append(",");
                            }
                            valstr.append("#");*/
                            prevdist = distobetravel;
                        }

                        if (t == 0) {
                            nextstop = prevstop = "Train is at source station";
                            ggg.append(jsonobj.getString("train"));
                            ggg.append("\t");
                            ggg.append(jarr.getJSONObject(0).getString("name"));
                            ggg.append("==>");
                            ggg.append(jarr.getJSONObject(jarr.length()-1).getString("name"));
                            ggg.append("\n");
                            ggg.append("Current Position : " );
                            ggg.append(jsonobj.getString("currpos") );
                            ggg.append("\n");
                            ggg.append("Previous: " );
                            ggg.append(prevstop );
                            ggg.append("\t");
                            ggg.append("Next : " );
                            ggg.append(nextstop );
                            ggg.append("\n");
                            ggg.append("Distance to travel : " );
                            ggg.append(prevdist );
                            ggg.append("\n");
                        }
                        TextView tv = (TextView)findViewById(R.id.currentstatus);
                        if(tv!=null)
                        {
                            tv.setText(ggg.toString());
                        }
                    }else {
                        context = getApplicationContext();
                        CharSequence text = "There is problem with backend server. Please wait!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                }catch (JSONException j)
                {
                    loadingLayout.setVisibility(View.GONE);
                    loadigText.setVisibility(View.GONE);
                    loadigIcon.setVisibility(View.GONE);
                    context = getApplicationContext();
                    CharSequence text = "There is problem with backend server. Please try again!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    //Log.d("J Exception : ",j.getMessage().toString());
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
                SpotTrainMainActivity.this.finish();
                i = new Intent(SpotTrainMainActivity.this, HomeScreenActivity.class);
                //i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                //i.putExtra("anim id out", R.anim.fragment_slide_left_exit);

                SpotTrainMainActivity.this.startActivity(i);
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

        int id = item.getItemId();

        if (id == R.id.action_status) {
            loadigIcon.setVisibility(View.GONE);
            Context context = getApplicationContext();
            CharSequence text = "Getting trains status for " + source + " from " + destination + " for the date " + dateofjourney;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
            loadingLayout.setVisibility(View.GONE);

            loadigText = (TextView) findViewById(R.id.textView111);
            loadigText.setVisibility(View.GONE);

            loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
            if (Build.VERSION.SDK_INT >= 23) {
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac,getTheme()), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else {
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            refreshstat = "http://api.ngrail.in/trainlivestat1/trainnum/" + source + "/doj/" + dateofjourney;
            loadigIcon.post(new Starter_live(refreshstat));
            return true;
        }

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
            //start Asyn Task here
            //Log.d("URL",url+"/key/"+phonenum+"/");
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
                //Log.d("AAA",result);
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
                    if(jsonobj.getString("response_code").equals("200")) {
                        routejson = jsonobj;
                        JSONArray arr = jsonobj.getJSONArray("route");
                        String[] paths = new String[arr.length()];
                        for(int i=0; i<arr.length(); i++)
                        {
                            paths[i]=arr.getJSONObject(i).getString("fullname")+" ("+arr.getJSONObject(i).getString("code")+")";
                        }
                        final Spinner sp = (Spinner)findViewById(R.id.spinner);
                        ArrayAdapter<String>adapter = new ArrayAdapter<>(SpotTrainMainActivity.this,
                                R.layout.spinner_item,paths);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp.setAdapter(adapter);
                        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //sp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                try {
                                    //JSONArray arr = routejson.getJSONArray("route");
                                    destination = parent.getItemAtPosition(position).toString().split("\\(")[1];
                                    destination = destination.substring(0, destination.length() - 1);
                                }catch (Exception e)
                                {
                                    //Log.d("PPP",e.getMessage());
                                }
                                //Log.d("SELECTED", parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        String [] paths1={"Yesterday","Today","Tomorrow"};
                        final Spinner sp1 = (Spinner)findViewById(R.id.spinner1);
                        ArrayAdapter<String>adapter1 = new ArrayAdapter<>(SpotTrainMainActivity.this,
                                R.layout.spinner_item,paths1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp1.setAdapter(adapter1);
                        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //sp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                //Log.d("SELECTED",parent.getItemAtPosition(position).toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        /*Intent i = new Intent(SpotTrainMainActivity.this, TrainsBetweenTwoStationsList.class);
                        i.putExtra("anim id in", R.anim.fragment_slide_left_exit);
                        i.putExtra("anim id out", R.anim.fragment_slide_right_enter);
                        SpotTrainMainActivity.this.finish();
                        i.putExtra("jsonvalue", result);
                        i.putExtra("dateval", dateval);

                        SpotTrainMainActivity.this.startActivity(i);*/
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

        //menu.add(0, id2, 0, "Check For Duplicate"+pnr);
    }

    @Override

    public boolean onContextItemSelected(MenuItem item) {

        return true;
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
        try {
        if (scheduled)
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
