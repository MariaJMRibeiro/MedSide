package up201506196.com.firsttp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

    Button b1,b2,b3,b4;
    DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    String date;
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

        mSpinner = (Spinner) view.findViewById(R.id.layout_spinner);
        String[] regists = new String[]{
                "Cholesterol (LDL)",
                "HeartBeat",
                "Blood Pressure",
                "Weight",
        };
        db = new DatabaseHelper(getActivity());
        sqLiteDatabase = db.getReadableDatabase();

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
                final String user = getActivity().getIntent().getExtras().getString("key_email");
                switch(position) {
                    case 0:
                        Cholesterol.setVisibility(View.VISIBLE);
                        Cursor cursor;
                        b1= (Button) view.findViewById(R.id.submit_button);
                        b1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(getActivity(), AddCho.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                            }
                        });

                        int value;
                        String type="cholesterol";
                        GraphView graph = (GraphView) view.findViewById(R.id.graph);

                        LineGraphSeries<DataPoint> series;
                        LineGraphSeries<DataPoint> refvalue;
                        LineGraphSeries<DataPoint> MEDrefvalue;
                        LineGraphSeries<DataPoint> HIGHrefvalue;

                        series = new LineGraphSeries<>();

                        refvalue = new LineGraphSeries<>();
                        refvalue.setColor(Color.GREEN);
                        MEDrefvalue = new LineGraphSeries<>();
                        MEDrefvalue.setColor(Color.YELLOW);
                        HIGHrefvalue = new LineGraphSeries<>();
                        HIGHrefvalue.setColor(Color.RED);

                        graph.addSeries(series);
                        graph.addSeries(refvalue);
                        graph.addSeries(HIGHrefvalue);
                        graph.addSeries(MEDrefvalue);

                        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinY(60);
                        graph.getViewport().setMaxY(190);
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_REGISTS + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{user,type});
                        int x=-1;
                        if (cursor.moveToFirst()) {
                            do {
                                x=x+1;
                                value=Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.COLUMN_RVALUE)));
                                date=cursor.getString(cursor.getColumnIndex(db.COLUMN_RDATE));
                                DataPoint point = new DataPoint(x,value);
                                series.appendData(point, false, 1000);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        DataPoint refvalue1=new DataPoint(0,100);
                        refvalue.appendData(refvalue1,false,1000);
                        DataPoint refvalue2=new DataPoint(x+1,100);
                        refvalue.appendData(refvalue2,false,1000);

                        DataPoint MEDrefvalue1=new DataPoint(0,130);
                        MEDrefvalue.appendData(MEDrefvalue1,false,1000);
                        DataPoint MEDrefvalue2=new DataPoint(x+1,130);
                        MEDrefvalue.appendData(MEDrefvalue2,false,1000);

                        DataPoint HIGHrefvalue1=new DataPoint(0,160);
                        HIGHrefvalue.appendData(HIGHrefvalue1,false,1000);
                        DataPoint HIGHrefvalue2=new DataPoint(x+1,160);
                        HIGHrefvalue.appendData( HIGHrefvalue2,false,1000);

                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setMaxX(x);

                        HeartBeat.setVisibility(View.GONE);
                        BloodPressure.setVisibility(View.GONE);
                        Weight.setVisibility(View.GONE);
                        break;

                    case 1: {
                        Cholesterol.setVisibility(View.GONE);
                        HeartBeat.setVisibility(View.VISIBLE);
                        b2= (Button) view.findViewById(R.id.hsubmit_button);
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(getActivity(), AddHeaB.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                            }
                        });

                        int valuehb;
                        String typehb="heart_beat";
                        GraphView graphhb = (GraphView) view.findViewById(R.id.hgraph);

                        LineGraphSeries<DataPoint> serieshb;
                        LineGraphSeries<DataPoint> MINserieshb;
                        LineGraphSeries<DataPoint> MAXserieshb;

                        serieshb = new LineGraphSeries<>();
                        MINserieshb = new LineGraphSeries<>();
                        MINserieshb.setColor(Color.GREEN);
                        MAXserieshb = new LineGraphSeries<>();
                        MAXserieshb.setColor(Color.GREEN);

                        graphhb.addSeries(serieshb);
                        graphhb.addSeries(MINserieshb);
                        graphhb.addSeries(MAXserieshb);


                        graphhb.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                        graphhb.getViewport().setYAxisBoundsManual(true);
                        graphhb.getViewport().setMinY(40);
                        graphhb.getViewport().setMaxY(140);
                        Cursor cursorhb;
                        int xhb=-1;
                        cursorhb = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_REGISTS + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{user,typehb});
                        if (cursorhb.moveToFirst()) {
                            do {
                                xhb=xhb+1;
                                valuehb=Integer.parseInt(cursorhb.getString(cursorhb.getColumnIndex(db.COLUMN_RVALUE)));
                                date=cursorhb.getString(cursorhb.getColumnIndex(db.COLUMN_RDATE));
                                DataPoint pointhb = new DataPoint(xhb,valuehb);
                                serieshb.appendData(pointhb, false, 1000);
                            } while (cursorhb.moveToNext());
                        }
                        cursorhb.close();

                        DataPoint maxvalue1=new DataPoint(0,100);
                        MAXserieshb.appendData(maxvalue1,false,1000);
                        DataPoint maxvalue2=new DataPoint(xhb+1,100);
                        MAXserieshb.appendData(maxvalue2,false,1000);

                        DataPoint minrefvalue1=new DataPoint(0,60);
                        MINserieshb.appendData(minrefvalue1,false,1000);
                        DataPoint minrefvalue2=new DataPoint(xhb+1,60);
                        MINserieshb.appendData( minrefvalue2,false,1000);

                        graphhb.getViewport().setXAxisBoundsManual(true);
                        graphhb.getViewport().setMaxX(xhb);

                        BloodPressure.setVisibility(View.GONE);
                        Weight.setVisibility(View.GONE);
                        break;
                    }
                    case 2: {
                        Cholesterol.setVisibility(View.GONE);
                        HeartBeat.setVisibility(View.GONE);
                        BloodPressure.setVisibility(View.VISIBLE);
                        b3= (Button) view.findViewById(R.id.bsubmit_button);
                        b3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(getActivity(), AddBP.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                            }
                        });

                        int high_valuebp;
                        int low_valuebp;
                        String high_typebp="high_blood_pressure";
                        String low_typebp="low_blood_pressure";
                        GraphView graphbp = (GraphView) view.findViewById(R.id.bgraph);

                        LineGraphSeries<DataPoint> high_seriesbp;
                        LineGraphSeries<DataPoint> low_seriesbp;
                        LineGraphSeries<DataPoint> MINref;
                        LineGraphSeries<DataPoint> MAXref;

                        high_seriesbp = new LineGraphSeries<>();
                        low_seriesbp = new LineGraphSeries<>();
                        MINref = new LineGraphSeries<>();
                        MINref.setColor(Color.GREEN);
                        MAXref = new LineGraphSeries<>();
                        MAXref.setColor(Color.GREEN);


                        graphbp.addSeries(high_seriesbp);
                        graphbp.addSeries(low_seriesbp);
                        graphbp.addSeries(MINref);
                        graphbp.addSeries(MAXref);

                        graphbp.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                        graphbp.getViewport().setYAxisBoundsManual(true);
                        graphbp.getViewport().setMinY(40);
                        graphbp.getViewport().setMaxY(150);
                        Cursor high_cursorbp;
                        Cursor low_cursorbp;
                        int high_xbp=-1;
                        int low_xbp=-1;
                        high_cursorbp = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_REGISTS + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{user,high_typebp});
                        low_cursorbp = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_REGISTS + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{user,low_typebp});
                        if (high_cursorbp.moveToFirst()) {
                            do {
                                high_xbp=high_xbp+1;
                                high_valuebp=Integer.parseInt(high_cursorbp.getString(high_cursorbp.getColumnIndex(db.COLUMN_RVALUE)));
                                DataPoint high_pointhb = new DataPoint(high_xbp,high_valuebp);
                                high_seriesbp.appendData( high_pointhb, false, 1000);
                            } while (high_cursorbp.moveToNext());
                        }
                        high_cursorbp.close();
                        if ( low_cursorbp.moveToFirst()) {
                            do {
                                low_xbp=low_xbp+1;
                                low_valuebp=Integer.parseInt(low_cursorbp.getString(low_cursorbp.getColumnIndex(db.COLUMN_RVALUE)));
                                DataPoint low_pointhb = new DataPoint(low_xbp,low_valuebp);
                                low_seriesbp.appendData(low_pointhb, false, 1000);
                            } while (low_cursorbp.moveToNext());
                        }
                        low_cursorbp.close();

                        DataPoint maxvalue1=new DataPoint(0,120);
                        MAXref.appendData(maxvalue1,false,1000);
                        DataPoint maxvalue2=new DataPoint(low_xbp+1,120);
                        MAXref.appendData(maxvalue2,false,1000);

                        DataPoint minrefvalue1=new DataPoint(0,80);
                        MINref.appendData(minrefvalue1,false,1000);
                        DataPoint minrefvalue2=new DataPoint(low_xbp+1,80);
                        MINref.appendData( minrefvalue2,false,1000);

                        graphbp.getViewport().setXAxisBoundsManual(true);
                        graphbp.getViewport().setMaxX(low_xbp+1);

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




