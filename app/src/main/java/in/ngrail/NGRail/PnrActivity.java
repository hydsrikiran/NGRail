package in.ngrail.NGRail;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

/**
 * Created by kiran on 11-12-2015.
 */
public class PnrActivity extends AppCompatActivity{
    private static final long DELAY = 500;
    private boolean scheduled = false;
    private Timer splashTimer;
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    private TextView loadigText = null;
    private ProgressBar loadigIcon = null;
    private LinearLayout loadingLayout = null;
    int selectedid = 0;
    String Pnrstr = null;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnrstatus_main);
        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            myToolbar.setTitle("NGRail PNR Status");
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
                            i = new Intent(PnrActivity.this, HomeScreenActivity.class);
                            i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                            i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                            PnrActivity.this.finish();
                            PnrActivity.this.startActivity(i);
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
            final LinearLayout lm = (LinearLayout) findViewById(R.id.pnrstatusnum);
            loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
            loadingLayout.setVisibility(View.GONE);

            loadigText = (TextView) findViewById(R.id.textView111);
            loadigText.setVisibility(View.GONE);

            loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
            loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
            loadigIcon.setVisibility(View.GONE);
            loadigIcon.post(new Starter("http://api.ngrail.in/reqpnrdetail/pnr/NA"));
        }catch (Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
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
                i = new Intent(PnrActivity.this, HomeScreenActivity.class);
                i.putExtra("anim id in", R.anim.fragment_slide_right_enter);
                i.putExtra("anim id out", R.anim.fragment_slide_left_exit);
                PnrActivity.this.finish();
                PnrActivity.this.startActivity(i);
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
            final Button adpnr = (Button) findViewById(R.id.addpnr);
            LayoutInflater layoutInflater
                    = (LayoutInflater) getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.addpnr, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);
                if(popupWindow.isShowing())
                {
                    popupWindow.dismiss();
                }
            popupWindow.setFocusable(true);
            popupWindow.update();
            popupWindow.setOutsideTouchable(false);
            final EditText pnedit = (EditText)popupView.findViewById(R.id.image);
                Button btnDismiss = (Button)popupView.findViewById(R.id.dialogButtonOK);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        String pneditt=null;
                        if(pnedit!=null) {
                            pneditt = pnedit.getText().toString();
                        }
                        if(pneditt.length()!=10)
                        {
                            showToast("Enter valid Pnr."+pneditt.length());
                            return;
                        }
                        Pnrstr = pneditt;
                        loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                        loadingLayout.setVisibility(View.GONE);

                        loadigText = (TextView) findViewById(R.id.textView111);
                        loadigText.setVisibility(View.GONE);

                        loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                        loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                        loadigIcon.setVisibility(View.GONE);
                        loadigIcon.post(new Starter_pnr(Pnrstr, "A"));
                        popupWindow.dismiss();

                    }
                });


                Button btnDismiss1 = (Button)popupView.findViewById(R.id.dialogButtonExit);
                btnDismiss1.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                //popupWindow.showAsDropDown(adpnr, Gravity.CENTER, Gravity.CENTER);
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.relt);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 30);
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
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                try{
                    JSONObject jsonobj = new JSONObject(result);
                    if(jsonobj.getString("responsecode").equals("200"))
                    {
                        int totlcount = jsonobj.getInt("pnrcount");
                        JSONArray jaray = jsonobj.getJSONArray("pnrdetails");
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin=5;
                        params.rightMargin=5;
                        params.topMargin=5;
                        params.bottomMargin=5;
                        ImageView[] img1= new ImageView[totlcount];
                        ImageView[] img2= new ImageView[totlcount];
                        ImageView[] img3= new ImageView[totlcount];
                        TextView[] tv1= new TextView[totlcount];
                        TextView[] tv2= new TextView[totlcount];
                        TextView[] tv3= new TextView[totlcount];
                        TextView[] tv4= new TextView[totlcount];
                        TextView[] tv5= new TextView[totlcount];
                        TextView[] tv6= new TextView[totlcount];
                        TextView[] tv7= new TextView[totlcount];
                        Button adp = (Button) findViewById(R.id.addpnr);
                        adp.setText(String.valueOf(jaray.length()));
                        LinearLayout[] ll = new LinearLayout[jaray.length()];
                        for(int i=0; i<jaray.length(); i++)
                        {
                            ll[i] = new LinearLayout(getApplicationContext());
                            ll[i].setOrientation(LinearLayout.VERTICAL);
                            ll[i].setPadding(3, 3, 3, 3);
                            ll[i].setId(((i + 1) * 10000) + 1);
                            ll[i].setBackgroundResource(R.drawable.pnrdiv);
                            //table lay out 1
                            TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableLayoutParams.setMargins(0, 0, 0, 10);
                            TableLayout tableLayout = new TableLayout(getApplicationContext());
                            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableRow tableRow = new TableRow(getApplicationContext());
                            tableRow.setPadding(5,5,5,5);

                                img1[i] = new ImageView(getApplicationContext());
                                img1[i].setImageResource(R.drawable.pnrtrain);
                                tableRow.addView(img1[i], tableRowParams);

                                tv1[i] = new TextView(getApplicationContext());
                                tv1[i].setTextSize(20);
                                tv1[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tv1[i].setTypeface(null, Typeface.BOLD);
                                tv1[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                tv1[i].setText("Trip To " + jaray.getJSONObject(i).getString("destination"));
                                tv1[i].setId(((i + 1) * 1000) + 9);
                            tableRow.addView(tv1[i],tableRowParams);
                            tableLayout.addView(tableRow, tableRowParams);
                            ll[i].addView(tableLayout, tableLayoutParams);




                            //table lay out 2
                            TableLayout.LayoutParams tableLayoutParams1 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableLayoutParams1.setMargins(0, 0, 0, 5);
                            TableLayout tableLayout1 = new TableLayout(getApplicationContext());
                            tableLayout1.setStretchAllColumns (true);
                            TableRow.LayoutParams tableRowParams1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableRow tableRow1 = new TableRow(getApplicationContext());
                            tableRow1.setPadding(10,0,0,0);

                            tv2[i] = new TextView(getApplicationContext());
                            tv2[i].setTextSize(15);
                            tv2[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            tv2[i].setTextColor(getResources().getColor(R.color.colorPnrDate));
                            tv2[i].setId(((i + 1) * 1000) + 8);
                            tv2[i].setText(jaray.getJSONObject(i).getString("doj"));
                            tableRow1.addView(tv2[i], tableRowParams1);

                            TableRow.LayoutParams tableRowParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableRowParams2.setMargins(0, 0, 40, 0);
                            tv3[i] = new TextView(getApplicationContext());
                            tv3[i].setTextSize(15);
                            tv3[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                            tv3[i].setTypeface(null, Typeface.BOLD);
                            tv3[i].setText(jaray.getJSONObject(i).getString("pnr"));
                            tv3[i].setId(((i+1) * 1000) + 3);
                            tv3[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                            tableRow1.addView(tv3[i],tableRowParams2);

                            tableLayout1.addView(tableRow1, tableRowParams1);
                            ll[i].addView(tableLayout1, tableLayoutParams1);


                            //table lay out 3
                            TableLayout.LayoutParams tableLayoutParams2 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableLayout tableLayout2 = new TableLayout(getApplicationContext());
                            tableLayout2.setColumnStretchable (2, true);
                            tableLayout2.setColumnStretchable (1, true);
                            TableRow.LayoutParams tableRowParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableRow tableRow2 = new TableRow(getApplicationContext());
                            tableRow2.setPadding(10,0,0,0);

                            tv4[i] = new TextView(getApplicationContext());
                            tv4[i].setTextSize(15);
                            tv4[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            tv4[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv4[i].setPadding(0, 0, 5, 0);
                            tv4[i].setId(((i + 1) * 1000) + 7);
                            tv4[i].setText(jaray.getJSONObject(i).getString("trainnum"));
                            tableRow2.addView(tv4[i], tableRowParams3);


                            tv5[i] = new TextView(getApplicationContext());
                            tv5[i].setTextSize(15);
                            tv5[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            tv5[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv5[i].setPadding(0, 0, 5, 0);
                            tv5[i].setText(jaray.getJSONObject(i).getString("trainname"));

                            tableRow2.addView(tv5[i], tableRowParams3);

                            img3[i] = new ImageView(getApplicationContext());
                            img3[i].setId(i + 1);
                            img3[i].setImageResource(R.drawable.pnrinfo);
                            registerForContextMenu(img3[i]);
                            img3[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openContextMenu(v);
                                }
                            });
                            TableRow.LayoutParams tableRowParams7 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableRowParams7.setMargins(0, 0, 40, 0);
                            tableRow2.addView(img3[i], tableRowParams7);

                            tableLayout2.addView(tableRow2, tableRowParams3);
                            ll[i].addView(tableLayout2, tableLayoutParams2);



                            //table lay out 4
                            TableLayout.LayoutParams tableLayoutParams3 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableLayout tableLayout3 = new TableLayout(getApplicationContext());
                            tableLayout3.setColumnStretchable (2, true);
                            //tableLayout3.setColumnStretchable (3, true);
                            TableRow.LayoutParams tableRowParams4 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            TableRow tableRow3= new TableRow(getApplicationContext());
                            tableRow3.setPadding(10,0,0,0);

                            img2[i] = new ImageView(getApplicationContext());
                            img2[i].setImageResource(R.drawable.pnrchair);
                            TableRow.LayoutParams tableRowParams5 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableRowParams5.setMargins(0, 0, 4, 0);
                            tableRow3.addView(img2[i], tableRowParams5);

                            tv6[i] = new TextView(getApplicationContext());
                            tv6[i].setTextSize(15);
                            tv6[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            String statuspnr = jaray.getJSONObject(i).getString("status");
                            tv6[i].setTypeface(null, Typeface.BOLD);
                            if (statuspnr != null) {
                                if (statuspnr.indexOf("W") != -1) {
                                    tv6[i].setTextColor(getResources().getColor(R.color.colorPnrWl));
                                } else if (statuspnr.indexOf("RAC") != -1) {
                                    tv6[i].setTextColor(getResources().getColor(R.color.colorPnrRac));
                                } else {
                                    tv6[i].setTextColor(getResources().getColor(R.color.colorPnrCnf));
                                }
                                tv6[i].setText(jaray.getJSONObject(i).getString("status"));
                            }

                            tv6[i].setPadding(0, 0, 5, 0);

                            tv6[i].setId(((i + 1) * 1000) + 1);
                            tableRow3.addView(tv6[i], tableRowParams4);


                            tv7[i] = new TextView(getApplicationContext());
                            tv7[i].setTextSize(10);
                            tv7[i].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                            tv7[i].setTextColor(getResources().getColor(R.color.colorPnrDate));
                            tv7[i].setPadding(0, 0, 5, 0);
                            tv7[i].setId(((i + 1) * 1000) + 2);
                            tv7[i].setTypeface(null, Typeface.BOLD);
                            tv7[i].setText("lst Upd : " + jaray.getJSONObject(i).getString("lastupdate").split(" ")[1]);

                            TableRow.LayoutParams tableRowParams6 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tableRowParams6.setMargins(0, 0, 20, 20);
                            tableRow3.addView(tv7[i], tableRowParams6);

                            tableLayout3.addView(tableRow3, tableRowParams4);

                            ll[i].addView(tableLayout3, tableLayoutParams3);
                            LinearLayout chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                            chat.addView(ll[i], params);
                            ll[i].setVisibility(View.INVISIBLE);
                        }
                        Animation slideL = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                        Animation slideR = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                        int y = 1000/jaray.length();
                        for(int i=0; i<jaray.length(); i++)
                        {
                            if(ll[i].getVisibility()==View.INVISIBLE){

                                ll[i].startAnimation(slideL);
                                ll[i].setVisibility(View.VISIBLE);
                            }
                            ll[i].startAnimation(slideR);
                        }
                    }
                    else
                    {
                        showToast("Invalid input / Back-end server responding very slow. Please try again!!");
                    }
                }catch (JSONException j)
                {
                    context = getApplicationContext();
                    CharSequence text = "Back-end Server issue. Please try again!"+j.getMessage();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
            catch (Exception e)
            {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
                int duration = Toast.LENGTH_LONG;
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
        int deside = 0;
        if((item.getItemId()-4)%10==0)
        {
            deside = 4;
        }
        else if((item.getItemId()-5)%10==0)
        {
            deside = 5;
        }
        else if((item.getItemId()-6)%10==0)
        {
            deside = 6;
        }
        if( deside == 4 ) {
            TextView tv = (TextView) findViewById(item.getItemId() - 1);
            if (tv != null) {
                Pnrstr = tv.getText().toString();
                loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                loadingLayout.setVisibility(View.GONE);

                loadigText = (TextView) findViewById(R.id.textView111);
                loadigText.setVisibility(View.GONE);

                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                loadigIcon.setVisibility(View.GONE);
                loadigIcon.post(new Starter_pnr(Pnrstr,"S"));
            }
        }
        else if(deside == 5 ) {
            final TextView tv = (TextView) findViewById(item.getItemId() - 2);
            if (tv != null) {

                new AlertDialog.Builder (this)
                        .setTitle("Warning!!")
                        .setMessage ("Do you want to Delete?")
                        .setIcon(getResources().getDrawable(R.drawable.ngrailsmlogo))
                        .setPositiveButton ("Yes",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {
                                Pnrstr = tv.getText().toString();

                                loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                                loadingLayout.setVisibility(View.GONE);

                                loadigText = (TextView) findViewById(R.id.textView111);
                                loadigText.setVisibility(View.GONE);

                                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                                loadigIcon.setVisibility(View.GONE);
                                loadigIcon.post(new Starter_pnr(Pnrstr, "D"));
                            }
                        })
                        .setNegativeButton ("No",null)
                        .show();

                //builder.Create().Show ();
                /*Pnrstr = tv.getText().toString();

                loadingLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
                loadingLayout.setVisibility(View.GONE);

                loadigText = (TextView) findViewById(R.id.textView111);
                loadigText.setVisibility(View.GONE);

                loadigIcon = (ProgressBar) findViewById(R.id.imageView111);
                loadigIcon.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPnrRac), android.graphics.PorterDuff.Mode.MULTIPLY);
                loadigIcon.setVisibility(View.GONE);
                loadigIcon.post(new Starter_pnr(Pnrstr, "D"));*/
            }
        }
        else if(deside == 6 ) {

            TextView tv = (TextView) findViewById(item.getItemId() - 3);
            TextView tv1 = (TextView) findViewById(item.getItemId() - 5);
            TextView tv2 = (TextView) findViewById(item.getItemId() +1);
            TextView tv3 = (TextView) findViewById(item.getItemId() +2);
            TextView tv4 = (TextView) findViewById(item.getItemId() +3);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Pnr : " + tv.getText().toString() + " \n"+tv4.getText().toString()+" \nStatus : " + tv1.getText().toString() + " \nTrain Number :" + tv2.getText().toString() + " \nDoj : " + tv3.getText().toString() + "\n--\nShared from NGRail-One Stop Train Enquiry Hub");
            startActivity(Intent.createChooser(sharingIntent, "Share Status using"));
        }
        return true;
    }
    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }

    class Starter_pnr implements Runnable {
        String url=null;
        String act = null;
        public Starter_pnr(String str, String action){
            url=str;
            act=action;
        }
        public void run() {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            final String phonenum = sharedpreferences.getString("name", null);
            //start Asyn Task here
            new DownloadTask_Pnr().execute(url+"/key/"+phonenum+"/action/"+act+"/");
        }
    }

    private class DownloadTask_Pnr extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                RailwayAPI railapi = new RailwayAPI();
                return railapi.getPnrStatus(urls[0]);
            } catch (Exception e) {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server issue. Please try again!";
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
            loadingLayout.setVisibility(View.GONE);
            loadigText.setVisibility(View.GONE);
            loadigIcon.setVisibility(View.GONE);
            if(result.equals("0"))
            {
                Context context = getApplicationContext();
                CharSequence text = "Enquiry not possible in Tatkal Time";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }
            try {
                JSONObject jsonobj = new JSONObject(result);
                if (jsonobj.getInt("responsecode") != 200) {
                    Context context = getApplicationContext();
                    CharSequence text = jsonobj.getString("error");
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    if(jsonobj.getString("error").equals("D"))
                    {
                        int reatId = ((selectedid)*10000)+1;
                        showToast(Pnrstr+" Deleted");
                        LinearLayout ll = (LinearLayout)findViewById(reatId);
                        LinearLayout chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                        chat.removeView(ll);
                        return;
                    }
                    else if(jsonobj.getString("error").equals("A"))
                    {
                        try{
                            jsonobj = new JSONObject(result);
                            if(jsonobj.getString("responsecode").equals("200"))
                            {
                                Button adp = (Button)findViewById(R.id.addpnr);
                                int i = Integer.parseInt(adp.getText().toString());


                                String addserpnr = jsonobj.getString("pnr");
                                for(int j=1; j<=i ; j++)
                                {
                                    TextView ll = (TextView)findViewById((i*1000)+3);
                                    if(ll!=null && ll.getText().equals(addserpnr))
                                    {
                                        int pnr = (selectedid * 1000) + 3;
                                        int status = (selectedid * 1000) + 1;
                                        int lastupd = (selectedid * 1000) + 2;
                                        TextView pnrid = (TextView) findViewById(pnr);
                                        TextView statusid = (TextView) findViewById(status);
                                        TextView lastupdid = (TextView) findViewById(lastupd);
                                        String statusval = jsonobj.getString("status");
                                        if (statusval != null) {
                                            if (statusval.indexOf("W") != -1) {
                                                statusid.setTextColor(getResources().getColor(R.color.colorPnrWl));
                                            } else if (statusval.indexOf("RAC") != -1) {
                                                statusid.setTextColor(getResources().getColor(R.color.colorPnrRac));
                                            } else {
                                                statusid.setTextColor(getResources().getColor(R.color.colorPnrCnf));
                                            }
                                            statusid.setText(statusval);
                                        }
                                        lastupdid.setText("lst Upd : " + jsonobj.getString("lastupdate").split(" ")[1]);
                                        /*Context context = getApplicationContext();
                                        CharSequence text = "Synchronize completed for " + pnr;
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();*/
                                        showToast("Pnr Status is : " + statusval);
                                        return;
                                    }
                                }
                                adp.setText(String.valueOf(i+1));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.leftMargin=5;
                                params.rightMargin=5;
                                params.topMargin=5;
                                params.bottomMargin=5;
                                ImageView img1;
                                ImageView img2;
                                ImageView img3;
                                TextView tv1;
                                TextView tv2;
                                TextView tv3;
                                TextView tv4;
                                TextView tv5;
                                TextView tv6;
                                TextView tv7;



                                LinearLayout ll = new LinearLayout(getApplicationContext());
                                ll.setOrientation(LinearLayout.VERTICAL);
                                ll.setPadding(3, 3, 3, 3);
                                ll.setId(((i+1)*10000)+1);
                                ll.setBackgroundResource(R.drawable.pnrdiv);
                                //table lay out 1
                                TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableLayoutParams.setMargins(0, 0, 0, 10);
                                TableLayout tableLayout = new TableLayout(getApplicationContext());
                                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableRow tableRow = new TableRow(getApplicationContext());
                                tableRow.setPadding(5,5,5,5);

                                img1 = new ImageView(getApplicationContext());
                                img1.setImageResource(R.drawable.pnrtrain);
                                tableRow.addView(img1, tableRowParams);

                                tv1 = new TextView(getApplicationContext());
                                tv1.setTextSize(20);
                                tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tv1.setTypeface(null, Typeface.BOLD);
                                tv1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                tv1.setText("Trip To " + jsonobj.getString("destination"));
                                tv1.setId(((i + 1) * 1000) + 9);
                                tableRow.addView(tv1,tableRowParams);
                                tableLayout.addView(tableRow, tableRowParams);
                                ll.addView(tableLayout, tableLayoutParams);




                                //table lay out 2
                                TableLayout.LayoutParams tableLayoutParams1 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableLayoutParams1.setMargins(0, 0, 0, 5);
                                TableLayout tableLayout1 = new TableLayout(getApplicationContext());
                                tableLayout1.setStretchAllColumns (true);
                                TableRow.LayoutParams tableRowParams1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableRow tableRow1 = new TableRow(getApplicationContext());
                                tableRow1.setPadding(10,0,0,0);

                                tv2 = new TextView(getApplicationContext());
                                tv2.setTextSize(15);
                                tv2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tv2.setTextColor(getResources().getColor(R.color.colorPnrDate));
                                tv2.setText(jsonobj.getString("doj"));
                                tv2.setId(((i + 1) * 1000) + 8);
                                tableRow1.addView(tv2, tableRowParams1);

                                TableRow.LayoutParams tableRowParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableRowParams2.setMargins(0, 0, 40, 0);
                                tv3 = new TextView(getApplicationContext());
                                tv3.setTextSize(15);
                                tv3.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                tv3.setTypeface(null, Typeface.BOLD);
                                tv3.setText(jsonobj.getString("pnr"));
                                tv3.setId(((i + 1) * 1000) + 3);
                                tv3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                tableRow1.addView(tv3,tableRowParams2);

                                tableLayout1.addView(tableRow1, tableRowParams1);
                                ll.addView(tableLayout1, tableLayoutParams1);


                                //table lay out 3
                                TableLayout.LayoutParams tableLayoutParams2 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableLayout tableLayout2 = new TableLayout(getApplicationContext());
                                tableLayout2.setColumnStretchable (2, true);
                                tableLayout2.setColumnStretchable (1, true);
                                TableRow.LayoutParams tableRowParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableRow tableRow2 = new TableRow(getApplicationContext());
                                tableRow2.setPadding(10,0,0,0);

                                tv4 = new TextView(getApplicationContext());
                                tv4.setTextSize(15);
                                tv4.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tv4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                tv4.setPadding(0, 0, 5, 0);
                                tv4.setId(((i + 1) * 1000) + 7);
                                tv4.setText(jsonobj.getString("trainnum"));
                                tableRow2.addView(tv4, tableRowParams3);


                                tv5 = new TextView(getApplicationContext());
                                tv5.setTextSize(15);
                                tv5.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tv5.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                tv5.setPadding(0, 0, 5, 0);
                                tv5.setText(jsonobj.getString("trainname"));

                                tableRow2.addView(tv5, tableRowParams3);

                                img3 = new ImageView(getApplicationContext());
                                img3.setId(i + 1);
                                img3.setImageResource(R.drawable.pnrinfo);
                                registerForContextMenu(img3);
                                img3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openContextMenu(v);
                                    }
                                });
                                TableRow.LayoutParams tableRowParams7 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableRowParams7.setMargins(0, 0, 40, 0);
                                tableRow2.addView(img3, tableRowParams7);

                                tableLayout2.addView(tableRow2, tableRowParams3);
                                ll.addView(tableLayout2, tableLayoutParams2);



                                //table lay out 4
                                TableLayout.LayoutParams tableLayoutParams3 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableLayout tableLayout3 = new TableLayout(getApplicationContext());
                                tableLayout3.setColumnStretchable (2, true);
                                //tableLayout3.setColumnStretchable (3, true);
                                TableRow.LayoutParams tableRowParams4 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                TableRow tableRow3= new TableRow(getApplicationContext());
                                tableRow3.setPadding(10,0,0,0);

                                img2 = new ImageView(getApplicationContext());
                                img2.setImageResource(R.drawable.pnrchair);
                                TableRow.LayoutParams tableRowParams5 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableRowParams5.setMargins(0, 0, 4, 0);
                                tableRow3.addView(img2, tableRowParams5);
                                tv6 = new TextView(getApplicationContext());
                                tv6.setTextSize(15);
                                tv6.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                String statuspnr = jsonobj.getString("status");
                                tv6.setTypeface(null, Typeface.BOLD);
                                if (statuspnr != null) {
                                    if (statuspnr.indexOf("W") != -1) {
                                        tv6.setTextColor(getResources().getColor(R.color.colorPnrWl));
                                    } else if (statuspnr.indexOf("RAC") != -1) {
                                        tv6.setTextColor(getResources().getColor(R.color.colorPnrRac));
                                    } else {
                                        tv6.setTextColor(getResources().getColor(R.color.colorPnrCnf));
                                    }
                                    tv6.setText(jsonobj.getString("status"));
                                }

                                tv6.setPadding(0, 0, 5, 0);

                                tv6.setId(((i + 1) * 1000) + 1);
                                tableRow3.addView(tv6, tableRowParams4);


                                tv7 = new TextView(getApplicationContext());
                                tv7.setTextSize(10);
                                tv7.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                                tv7.setTextColor(getResources().getColor(R.color.colorPnrDate));
                                tv7.setPadding(0, 0, 5, 0);
                                tv7.setId(((i + 1) * 1000) + 2);
                                tv7.setTypeface(null, Typeface.BOLD);
                                tv7.setText("lst Upd : " + jsonobj.getString("lastupdate").split(" ")[1]);
                                TableRow.LayoutParams tableRowParams6 = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 	ViewGroup.LayoutParams.WRAP_CONTENT);
                                tableRowParams6.setMargins(0, 0, 20, 20);
                                tableRow3.addView(tv7,tableRowParams6);

                                tableLayout3.addView(tableRow3, tableRowParams4);

                                ll.addView(tableLayout3, tableLayoutParams3);
                                LinearLayout chat = (LinearLayout) findViewById(R.id.pnrstatusnum);
                                chat.addView(ll,params);
                                showToast("Pnr Status is : " + statuspnr);
                            }
                            else
                            {
                                showToast("Invalid input / Back-end server responding very slow. Please try again!!");
                            }
                        }catch (JSONException j)
                        {
                            Context context = getApplicationContext();
                            CharSequence text = "Back-end Server issue. Please try again!"+j.getMessage();
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                    else {
                        int pnr = (selectedid * 1000) + 3;
                        int status = (selectedid * 1000) + 1;
                        int lastupd = (selectedid * 1000) + 2;
                        TextView pnrid = (TextView) findViewById(pnr);
                        TextView statusid = (TextView) findViewById(status);
                        TextView lastupdid = (TextView) findViewById(lastupd);
                        String statusval = jsonobj.getString("status");
                        if (statusval != null) {
                            if (statusval.indexOf("W") != -1) {
                                statusid.setTextColor(getResources().getColor(R.color.colorPnrWl));
                            } else if (statusval.indexOf("RAC") != -1) {
                                statusid.setTextColor(getResources().getColor(R.color.colorPnrRac));
                            } else {
                                statusid.setTextColor(getResources().getColor(R.color.colorPnrCnf));
                            }
                            statusid.setText(statusval);
                        }
                        lastupdid.setText("lst Upd : " + jsonobj.getString("lastupdate").split(" ")[1]);
                        Context context = getApplicationContext();
                        CharSequence text = "Synchronize completed for " + pnrid.getText().toString();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        showToast("Pnr Status is : " + statusval);
                    }

                    NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

                    //notif.notify(0, notify);
                    String notify1 = jsonobj.getString("pnr")+" ("+jsonobj.getString("doj")+")";
                    String notify2 = jsonobj.getString("trainnum")+" "+jsonobj.getString("status");
                    Notification.Builder builder = new Notification.Builder(PnrActivity.this);
                    builder.setSmallIcon(R.drawable.ngrailsmlogo)
                            .setContentTitle(notify1)
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setContentInfo("NGRail")
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setLights(Color.RED, 3000, 3000)
                            .setContentText(notify2)
                            .setContentIntent(pending);

                    Notification notify = builder.getNotification();
                    notif.notify(R.drawable.pnrinfo, notify);
                }

                //sendSMSMessage(text.toString());

            }
            catch (Exception e)
            {
                Context context = getApplicationContext();
                CharSequence text = "Back-end Server issue. Please try again!"+e.getMessage();
                int duration = Toast.LENGTH_LONG;
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
