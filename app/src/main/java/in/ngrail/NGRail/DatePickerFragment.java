package in.ngrail.NGRail;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kiran on 03-11-2015.
 */
public class DatePickerFragment extends DialogFragment {
    OnDateSetListener ondateSet;
    private static final int MY_DATE_DIALOG_ID = 3;
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
        DatePickerDialog da =  new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        if(id==1)
        {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 0);
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DATE, 125);
            Date newDate = c.getTime();
            Date newDate1 = c1.getTime();
            da.getDatePicker().setMinDate(newDate.getTime());
            da.getDatePicker().setMaxDate(newDate1.getTime());
            da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
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
            da.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        return da;
    }
}