package in.ngrail.NGERail;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    OnDateSetListener ondateSet;
    //private static final int MY_DATE_DIALOG_ID = 3;
    public DatePickerFragment() {
    }

    public void setCallBack(OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day, id;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        id = args.getInt("id");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //DatePickerDialog da =  new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        DatePickerDialog da =  new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, ondateSet, year, month, day);
        if(id==1)
        {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            //c.add(Calendar.DATE, 0);
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, 125);
            Date newDate;// = c.getTime();
            Date newDate1 = c1.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            mMonth = mMonth+1;
            String dateToConvertMin = mYear+"-"+mMonth+"-"+mDay;
            try {
                newDate = format.parse(dateToConvertMin);
            }catch (ParseException e)
            {
                c.add(Calendar.DATE, -1);
                newDate= c.getTime();
            }
            da.getDatePicker().setMinDate(newDate.getTime());
            da.getDatePicker().setMaxDate(newDate1.getTime());
            da.getDatePicker().setSpinnersShown(true);
            da.getDatePicker().setCalendarViewShown(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                da.setIcon(getResources().getDrawable(R.drawable.ngrailsmlogo, getContext().getTheme()));

            }
            else {
                da.setIcon(getResources().getDrawable(R.drawable.ngrailsmlogo));

            }
            if (Build.VERSION.SDK_INT >= 23) {
                da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,getContext().getTheme()));
            }
            else{
                da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }

        }
        else if(id==2)
        {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -4);
            Date newDate = c.getTime();
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, 0);
            Date newDate1 = c1.getTime();
            da.getDatePicker().setMinDate(newDate.getTime());
            da.getDatePicker().setMaxDate(newDate1.getTime());
            if (Build.VERSION.SDK_INT >= 23) {
                da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,getContext().getTheme()));
            }
            else {
                da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
        return da;
    }
}