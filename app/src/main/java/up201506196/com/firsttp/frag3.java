package up201506196.com.firsttp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class frag3 extends Fragment {

    CalendarView calender;
    TextView currentDate;
    Button b1,b2;
    ArrayList<String> this_title;
    ArrayList<String> this_description;
    ArrayList<String> this_date;
    AppListAdapter listAdapter ;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper db;

    public frag3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View View=inflater.inflate(R.layout.fragment_frag3, container, false);
        return View;
    }

    public void onViewCreated(final View view, Bundle saveInstanceState){
        final int user = getActivity().getIntent().getIntExtra("key_email",0);
        calender=(CalendarView)view.findViewById(R.id.calender);
        currentDate=(TextView)view.findViewById(R.id.current_date);
        b1=(Button)view.findViewById(R.id.add_appointment);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Please select a date", Toast.LENGTH_SHORT).show();
            }
        });
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String date= dayOfMonth + "/" + (month+1) + "/" + year;
                currentDate.setText(date);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(getActivity(),Set_Appointment.class);
                        i.putExtra("date",date);
                        i.putExtra("key_email",user);
                        startActivity(i);
                    }
                });
            }

        });
        b2=view.findViewById(R.id.all_appointment);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.list_app, null);
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.list_app);
                db = new DatabaseHelper(getActivity());
                sqLiteDatabase = db.getReadableDatabase();

                this_title = new ArrayList<>();
                this_description = new ArrayList<>();
                this_date = new ArrayList<>();

                Cursor app_cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_APP + " WHERE " + db.COLUMN_AUSER + "=?" , new String[]{String.valueOf(user)});

                this_date.clear();
                this_title.clear();
                this_description.clear();

                if (app_cursor.moveToFirst()) {
                    do {

                        this_date.add(app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ADATE)));
                        this_title.add(app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ATITLE)));
                        this_description.add(app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ADESCRIPTION)));

                    } while (app_cursor.moveToNext());
                }
                else{
                    TextView caution = convertView.findViewById(R.id.app_caution);
                    caution.setVisibility(View.VISIBLE);
                }


                listAdapter = new AppListAdapter(getActivity(),
                        this_title,
                        this_description,
                        this_date
                );

                lv.setAdapter(listAdapter);
                app_cursor.close();

                alertDialog.show();
            }
        });

    }

}
