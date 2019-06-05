package up201506196.com.firsttp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class frag2 extends Fragment {

    Button b1;
    DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String date;
    int x=0;
    private Spinner mSpinner;

    public frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag2, container, false);

        return view;
    }


    public void onViewCreated(final View view, Bundle savedInstanceState) {
        b1= (Button) view.findViewById(R.id.submit_button);
        mSpinner = (Spinner) view.findViewById(R.id.layout_spinner);
        String[] regists = new String[]{
                "Cholesterol",
                "HeartBeat",
                "Blood Pressure",
                "Weight",
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(regists));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,plantsList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(spinnerArrayAdapter);
        final LinearLayout Cholesterol = (LinearLayout) view.findViewById(R.id.layout_cholesterol);
        final LinearLayout HeartBeat = (LinearLayout) view.findViewById(R.id.layout_hear_beat);
        final LinearLayout BloodPressure = (LinearLayout) view.findViewById(R.id.layout_blood_pressure);
        final LinearLayout Weight = (LinearLayout) view.findViewById(R.id.layout_weight);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch(position) {
                    case 0:
                        Cholesterol.setVisibility(View.VISIBLE);
                        db = new DatabaseHelper(getActivity());
                        b1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user = getActivity().getIntent().getExtras().getString("key_email");
                                Intent i=new Intent(getActivity(), AddCho.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                            }
                        });

                        int value;
                        String type="cholesterol";
                        String user = getActivity().getIntent().getExtras().getString("key_email");
                        sqLiteDatabase = db.getReadableDatabase();
                        GraphView graph = (GraphView) view.findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series;
                        series = new LineGraphSeries<>();
                        graph.addSeries(series);
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_REGISTS + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{user,type});
                        if (cursor.moveToFirst()) {
                            do {
                                value=Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.COLUMN_RVALUE)));
                                date=cursor.getString(cursor.getColumnIndex(db.COLUMN_RDATE));
                                x=x+1;
                                DataPoint point = new DataPoint(x,value);
                                series.appendData(point, false, 1000);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();

                        HeartBeat.setVisibility(View.GONE);
                        BloodPressure.setVisibility(View.GONE);
                        Weight.setVisibility(View.GONE);
                        break;

                    case 1: {
                        Cholesterol.setVisibility(View.GONE);
                        HeartBeat.setVisibility(View.VISIBLE);
                        BloodPressure.setVisibility(View.GONE);
                        Weight.setVisibility(View.GONE);
                        break;
                    }
                    case 2: {
                        Cholesterol.setVisibility(View.GONE);
                        HeartBeat.setVisibility(View.GONE);
                        BloodPressure.setVisibility(View.VISIBLE);
                        Weight.setVisibility(View.GONE);
                        break;
                    }
                    case 3: {
                        Cholesterol.setVisibility(View.GONE);
                        HeartBeat.setVisibility(View.GONE);
                        BloodPressure.setVisibility(View.GONE);
                        Weight.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });


    }
}




