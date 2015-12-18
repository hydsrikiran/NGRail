package in.ngrail.NGRail;

import android.app.DatePickerDialog;
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
import android.text.method.Touch;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kiran on 11-12-2015.
 */
public class TrainsBetweenTwoStations extends AppCompatActivity{
    private static final long DELAY = 500;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    private String source=null;
    private String destination=null;
    private String dateofjourney=null;
    private String dateval=null;
    int selectedid = 0;
    String Pnrstr = null;
    private static final int MY_DATE_DIALOG_ID = 3;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainsbetween2stations);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                            i = new Intent(TrainsBetweenTwoStations.this, HomeScreenActivity.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            TrainsBetweenTwoStations.this.finish();
                            TrainsBetweenTwoStations.this.startActivity(i);
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
            myToolbar.inflateMenu(R.menu.main);
            AutoCompleteTextView text;
            text=(AutoCompleteTextView)findViewById(R.id.source_station);
            ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this,
                    R.array.station_array, android.R.layout.simple_list_item_1);

            text.setAdapter(adapter3);
            text.setThreshold(1);
            text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            AutoCompleteTextView text1;
            text1=(AutoCompleteTextView)findViewById(R.id.destinstion_station);
            ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this,
                    R.array.station_array, android.R.layout.simple_list_item_1);

            text1.setAdapter(adapter4);
            text1.setThreshold(1);
            text1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
            EditText ed = (EditText)findViewById(R.id.datepick);
            ed.setShowSoftInputOnFocus(false);
            ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        //showDatePicker();
                        try  {
                            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        DatePickerFragment date = new DatePickerFragment();
                        Calendar calender = Calendar.getInstance();
                        Bundle args = new Bundle();
                        args.putInt("year", calender.get(Calendar.YEAR));
                        args.putInt("month", calender.get(Calendar.MONTH));
                        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
                        args.putInt("id", 1);
                        date.setArguments(args);
                        date.setCallBack(ondate);
                        showDialog(MY_DATE_DIALOG_ID);;
                        date.show(getSupportFragmentManager(), "Date Picker");
                    }
                }
            });
            findViewById(R.id.datepick).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //showDatePicker();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    DatePickerFragment date = new DatePickerFragment();
                    Calendar calender = Calendar.getInstance();
                    Bundle args = new Bundle();
                    args.putInt("year", calender.get(Calendar.YEAR));
                    args.putInt("month", calender.get(Calendar.MONTH));
                    args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
                    args.putInt("id", 1);
                    date.setArguments(args);
                    date.setCallBack(ondate);
                    date.show(getSupportFragmentManager(), "Date Picker");
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
                }
            });
        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


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
                source = ((AutoCompleteTextView) findViewById(R.id.source_station)).getText().toString();
                destination = ((AutoCompleteTextView) findViewById(R.id.destinstion_station)).getText().toString();
                dateofjourney = ((EditText) findViewById(R.id.datepick)).getText().toString();
                if (source.length() <= 0 || destination.length() <= 0 || dateofjourney.length() <= 0) {
                    CharSequence text = "*All the fields are Required. Please Enter!!!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                CharSequence text = "Getting trains list from " + source + " to " + destination + " for the date " + dateofjourney;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                loadingLayout.setVisibility(View.GONE);

                loadigText = (TextView) findViewById(R.id.textView111);
                loadigText.setVisibility(View.GONE);

                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                loadigIcon.setVisibility(View.GONE);

                //loadingViewAnim = (AnimationDrawable) loadigIcon.getBackground();

                // This line is to start Asyn Task only when OnCreate Method get completed, So Loading Icon Rotation Animation work properly
                String[] dojsp=dateofjourney.split("-");
                String dojf = dojsp[2]+"-"+dojsp[1]+"-"+dojsp[0];
                loadigIcon.post(new Starter("http://api.ngrail.in/trbwts/source/" + source.split(" ")[0] + "/destination/" + destination.split(" ")[0] + "/doj/" + dojf));
                //new DownloadTask().execute("http://api.ngrail.in/tbtswd/source/"+source.split(" ")[0]+"/destination/"+destination.split(" ")[0]+"/doj/"+dateofjourney.substring(0,dateofjourney.lastIndexOf("-")));
            }
        });

    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            EditText dateset = (EditText)findViewById(R.id.datepick);
            dateval = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
            dateset.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
            //Log.d("AAAA", String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth));
        }
    };



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
                i = new Intent(TrainsBetweenTwoStations.this, HomeScreenActivity.class);
                i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                TrainsBetweenTwoStations.this.finish();
                TrainsBetweenTwoStations.this.startActivity(i);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_status) {

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
            //EditText et1 = (EditText) findViewById(R.id.qqqq);
            //et1.setText("QQQ"+url+"/key/"+phonenum+"/");
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
                CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
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
                    if(jsonobj.getString("responsecode").equals("200")) {
                        Intent i = new Intent(TrainsBetweenTwoStations.this, TrainsBetweenTwoStationsList.class);
                        i.putExtra("anim id in", R.anim.fragment_slide_left_exit);
                        i.putExtra("anim id out", R.anim.fragment_slide_right_enter);
                        TrainsBetweenTwoStations.this.finish();
                        i.putExtra("jsonvalue", result);
                        i.putExtra("dateval", dateval);

                        TrainsBetweenTwoStations.this.startActivity(i);
                        //Log.d("JSON VALUE",result);
                    }
                    else
                    {
                        showToast("Invalid input / Back-end server responding very slow. Please try again!!");
                    }
                }catch (JSONException j)
                {
                    context = getApplicationContext();
                    CharSequence text = "Back-end Server issue. Please try again!"+j.getMessage()+result;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
            catch (Exception e)
            {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
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
    @Override

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        //if (v.getId()==R.id.usernameEdittext) {
            //getMenuInflater().inflate(R.menu.contextmenu, menu);
        //
        if(v.getId()>0) {
            selectedid = v.getId();
            int pnr = (selectedid * 1000) + 3;
            int id1 = (selectedid * 1000) + 4;
            int id2 = (selectedid * 1000) + 5;
            int id3 = (selectedid * 1000) + 6;
            TextView tt = (TextView) findViewById(pnr);
            String pnrval = tt.getText().toString();
            menu.setHeaderTitle("Select Option");
            menu.setHeaderIcon(R.drawable.ngrailsmlogo);
            menu.add(0, id1, 0, "Refresh");
            menu.add(0, id3, 0, "Share Status");
            menu.add(0, id2, 0, "Delete");
        }
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
