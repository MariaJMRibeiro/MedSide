package up201506196.com.firsttp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ArrayList<Integer> this_id;
    String my_date;
    ArrayList<String> this_date;
    AppListAdapter listAdapter ;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper db;
    int app_id;

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
                final String date= year + "-" + (month+1) + "-" + dayOfMonth;
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
                this_id= new ArrayList<>();

                Cursor app_cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_APP + " WHERE " + db.COLUMN_AUSER + "=?"+" order by " + db.COLUMN_ADATE, new String[]{String.valueOf(user)});

                this_date.clear();
                this_title.clear();
                this_description.clear();
                this_id.clear();

                if (app_cursor.moveToFirst()) {
                    do {
                        my_date=app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ADATE));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(my_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (System.currentTimeMillis() <= strDate.getTime()) {
                            this_date.add(my_date);
                            this_title.add(app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ATITLE)));
                            this_description.add(app_cursor.getString(app_cursor.getColumnIndex(db.COLUMN_ADESCRIPTION)));
                            this_id.add(app_cursor.getInt(app_cursor.getColumnIndex(db.COLUMN_AID)));
                        }
                    } while (app_cursor.moveToNext());
                }

                if (this_title.isEmpty()){
                    TextView caution = convertView.findViewById(R.id.app_caution);
                    caution.setVisibility(View.VISIBLE);
                }

                app_cursor.close();
                listAdapter = new AppListAdapter(getActivity(),
                        this_title,
                        this_description,
                        this_date
                );

                lv.setAdapter(listAdapter);
                alertDialog.show();

                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                        app_id=this_id.get(position);
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
                        alertDialog2.setTitle("Appointment "+ this_title.get(position) );
                        alertDialog2.setMessage("Do you want to delete this appointment?");
                        alertDialog2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteApp(app_id);
                                this_title.remove(position);
                                this_description.remove(position);
                                this_date.remove(position);
                                listAdapter.notifyDataSetChanged();
                            }
                        });
                        alertDialog2.show();
                        return true;
                    }
                });

            }
        });

    }

}
