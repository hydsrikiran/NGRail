package in.ngrail.NGERail;

/**
 * Created by kiran on 20-12-2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySMSApp extends BroadcastReceiver {

    public static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String MyPREFERENCES = "NGRailPrefs" ;
    public static final String Mail = "email";
    public static final String Phone = "name";
    SharedPreferences sharedpreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(ACTION)){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage message : messages){

                    String strMessageFrom = message.getDisplayOriginatingAddress();
                    String strMessageBody = message.getDisplayMessageBody();

                    final String text = strMessageBody;
                    sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    String phonenum = sharedpreferences.getString("name", null);
                    String email = sharedpreferences.getString("email", null);
                    if(email!=null && phonenum!=null && email!="NA" && phonenum!="NA" && strMessageBody.toLowerCase().indexOf("pnr")>=0 && strMessageBody.toLowerCase().indexOf("doj")>=0)
                    {
                        //Toast.makeText(context, "SMS Message received from:" +strMessageFrom, Toast.LENGTH_LONG).show();
                        //Toast.makeText(context, "SMS Message content" + strMessageBody, Toast.LENGTH_LONG).show();
                        String pnrstr = strMessageBody.substring(strMessageBody.toLowerCase().indexOf("PNR".toLowerCase()) + 3);
                        Pattern pattern = Pattern.compile("^\\D*(\\d)");
                        Matcher matcher = pattern.matcher(pnrstr);
                        matcher.find();
                        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent notificationIntent = new Intent(context, PnrActivity.class);
                        notificationIntent.putExtra("notpnr",pnrstr.substring(matcher.start(1),matcher.start(1)+10));
                        PendingIntent pending = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        //notif.notify(0, notify);

                        String notify1 = "New Pnr "+pnrstr.substring(matcher.start(1),matcher.start(1)+10)+" found. Click here to add to NGRail app.";
                        Notification.Builder builder = new Notification.Builder(context);
                        builder.setSmallIcon(R.drawable.ngrailsmlogo)
                                .setContentTitle("NGRail - One Stop Train Enquiry Hub")
                                .setColor(context.getResources().getColor(R.color.colorPrimary))
                                .setContentInfo("NGRail")
                                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                .setLights(Color.RED, 3000, 3000)
                                .setContentText(notify1)
                                .setContentIntent(pending);

                        Notification notify = builder.getNotification();
                        notif.notify(R.drawable.pnrinfo, notify);
                    }

                }
            }
        }
    }

}
