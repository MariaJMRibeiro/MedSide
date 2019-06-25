package up201506196.com.firsttp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class frag2 extends Fragment {

    Button b1,b2,b3,b4,b5,b6,b7,b8;
    DatabaseHelper db;
    EditText e1;
    SQLiteDatabase sqLiteDatabase;
    String date;
    private Spinner mSpinner;
    ArrayList<String> this_weight;
    ArrayList<String> this_date;
    ArrayList<Integer> this_id;
    MedListAdapter listAdapter ;
    int weight_id;

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
        String[] records = new String[]{
                "Cholesterol (LDL)",
                "Heart Rate",
                "Blood Pressure",
                "Weight",
        };
        db = new DatabaseHelper(getActivity());
        sqLiteDatabase = db.getReadableDatabase();

        final List<String> RecordsList = new ArrayList<>(Arrays.asList(records));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,RecordsList){
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

        final int user = getActivity().getIntent().getIntExtra("key_email",0);


        //region implements case 0

        b1= (Button) view.findViewById(R.id.submit_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddCho.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });
        final String type="cholesterol";
        b5=view.findViewById(R.id.reset_button);
        b5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.deleteRecord(type,user);
                Intent i=new Intent(getActivity(), InitialPage.class);
                i.putExtra("key_email", user);
                i.putExtra("toOpen", 1);
                i.putExtra("toGraph", 0);
                startActivity(i);
                Toast.makeText(getActivity(), "Cholesterol values reset", Toast.LENGTH_SHORT).show();
            }
        });

        int value;
        Cursor cursor;
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series;
        LineGraphSeries<DataPoint> refvalue;
        LineGraphSeries<DataPoint> MEDrefvalue;
        LineGraphSeries<DataPoint> HIGHrefvalue;

        series = new LineGraphSeries<>();
        series.setTitle("LDL");

        refvalue = new LineGraphSeries<>();
        refvalue.setTitle("Optimal");
        refvalue.setColor(Color.GREEN);
        MEDrefvalue = new LineGraphSeries<>();
        MEDrefvalue.setTitle("Medium");
        MEDrefvalue.setColor(Color.YELLOW);
        HIGHrefvalue = new LineGraphSeries<>();
        HIGHrefvalue.setTitle("High");
        HIGHrefvalue.setColor(Color.RED);

        graph.addSeries(series);
        graph.addSeries(refvalue);
        graph.addSeries(HIGHrefvalue);
        graph.addSeries(MEDrefvalue);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(Color.argb(00,00,00,00));

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(60);
        graph.getViewport().setMaxY(220);
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_RECORD + " WHERE " + db.COLUMN_MUSER + " = ? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user),type});
        int x=0;
        if (cursor.moveToFirst()) {
            do {
                value=Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.COLUMN_RVALUE)));
                date=cursor.getString(cursor.getColumnIndex(db.COLUMN_RDATE));
                DataPoint point = new DataPoint(x,value);
                series.appendData(point, false, 1000);
                x=x+1;
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
        graph.getViewport().setMaxX(x+1);

        //endregion

        //region implements case 1
        final String typehb="heart_beat";
        b2= (Button) view.findViewById(R.id.hsubmit_button);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddHeaB.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });
        b6=view.findViewById(R.id.hreset_button);
        b6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.deleteRecord(typehb,user);
                Intent i=new Intent(getActivity(), InitialPage.class);
                i.putExtra("key_email", user);
                i.putExtra("toOpen", 1);
                i.putExtra("toGraph", 1);
                startActivity(i);
                Toast.makeText(getActivity(), "Heart Rate values reset", Toast.LENGTH_SHORT).show();
            }
        });

        int valuehb;
        GraphView graphhb = (GraphView) view.findViewById(R.id.hgraph);

        LineGraphSeries<DataPoint> serieshb;
        LineGraphSeries<DataPoint> MINserieshb;
        LineGraphSeries<DataPoint> MAXserieshb;

        serieshb = new LineGraphSeries<>();
        serieshb.setTitle("Heart Rate");
        MAXserieshb = new LineGraphSeries<>();
        MAXserieshb.setTitle("Max");
        MAXserieshb.setColor(Color.RED);
        MINserieshb = new LineGraphSeries<>();
        MINserieshb.setTitle("Min");
        MINserieshb.setColor(Color.GREEN);


        graphhb.addSeries(serieshb);
        graphhb.addSeries(MINserieshb);
        graphhb.addSeries(MAXserieshb);

        graphhb.getLegendRenderer().setVisible(true);
        graphhb.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphhb.getLegendRenderer().setBackgroundColor(Color.argb(00,00,00,00));
        graphhb.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphhb.getViewport().setYAxisBoundsManual(true);
        graphhb.getViewport().setMinY(40);
        graphhb.getViewport().setMaxY(140);
        Cursor cursorhb;
        int xhb=0;
        cursorhb = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_RECORD + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user),typehb});
        if (cursorhb.moveToFirst()) {
            do {
                valuehb=Integer.parseInt(cursorhb.getString(cursorhb.getColumnIndex(db.COLUMN_RVALUE)));
                date=cursorhb.getString(cursorhb.getColumnIndex(db.COLUMN_RDATE));
                DataPoint pointhb = new DataPoint(xhb,valuehb);
                serieshb.appendData(pointhb, false, 1000);
                xhb=xhb+1;
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
        graphhb.getViewport().setMaxX(xhb+1);
        //endregion

        //region implements case 2

        final String high_typebp="high_blood_pressure";
        final String low_typebp="low_blood_pressure";
        b3= (Button) view.findViewById(R.id.bsubmit_button);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddBP.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

        b7=view.findViewById(R.id.breset_button);
        b7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.deleteRecord(high_typebp,user);
                db.deleteRecord(low_typebp,user);
                Intent i=new Intent(getActivity(), InitialPage.class);
                i.putExtra("key_email", user);
                i.putExtra("toOpen", 1);
                i.putExtra("toGraph", 2);
                startActivity(i);
                Toast.makeText(getActivity(), "Blood Pressure values reset", Toast.LENGTH_SHORT).show();
            }
        });

        int high_valuebp;
        int low_valuebp;
        GraphView graphbp = (GraphView) view.findViewById(R.id.bgraph);

        LineGraphSeries<DataPoint> high_seriesbp;
        LineGraphSeries<DataPoint> low_seriesbp;
        LineGraphSeries<DataPoint> MINref;
        LineGraphSeries<DataPoint> MAXref;

        high_seriesbp = new LineGraphSeries<>();
        high_seriesbp.setTitle("Systolic");
        high_seriesbp.setColor(Color.BLACK);
        low_seriesbp = new LineGraphSeries<>();
        low_seriesbp.setTitle("Diastolic");
        MINref = new LineGraphSeries<>();
        MINref.setTitle("Dias. Ref.");
        MINref.setColor(Color.GREEN);
        MAXref = new LineGraphSeries<>();
        MAXref.setTitle("Sys. Ref");
        MAXref.setColor(Color.RED);


        graphbp.addSeries(high_seriesbp);
        graphbp.addSeries(low_seriesbp);
        graphbp.addSeries(MINref);
        graphbp.addSeries(MAXref);

        graphbp.getLegendRenderer().setVisible(true);
        graphbp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphbp.getLegendRenderer().setBackgroundColor(Color.argb(00,00,00,00));
        graphbp.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphbp.getViewport().setYAxisBoundsManual(true);
        graphbp.getViewport().setMinY(40);
        graphbp.getViewport().setMaxY(150);
        Cursor high_cursorbp;
        Cursor low_cursorbp;
        int high_xbp=0;
        int low_xbp=0;
        high_cursorbp = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_RECORD + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user),high_typebp});
        low_cursorbp = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_RECORD + " WHERE " + db.COLUMN_MUSER + " =? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user),low_typebp});
        if (high_cursorbp.moveToFirst()) {
            do {
                high_valuebp=Integer.parseInt(high_cursorbp.getString(high_cursorbp.getColumnIndex(db.COLUMN_RVALUE)));
                DataPoint high_pointhb = new DataPoint(high_xbp,high_valuebp);
                high_seriesbp.appendData( high_pointhb, false, 1000);
                high_xbp=high_xbp+1;
            } while (high_cursorbp.moveToNext());
        }
        high_cursorbp.close();
        if ( low_cursorbp.moveToFirst()) {
            do {
                low_valuebp=Integer.parseInt(low_cursorbp.getString(low_cursorbp.getColumnIndex(db.COLUMN_RVALUE)));
                DataPoint low_pointhb = new DataPoint(low_xbp,low_valuebp);
                low_seriesbp.appendData(low_pointhb, false, 1000);
                low_xbp=low_xbp+1;
            } while (low_cursorbp.moveToNext());
        }
        low_cursorbp.close();

        DataPoint maxvalue11=new DataPoint(0,120);
        MAXref.appendData(maxvalue11,false,1000);
        DataPoint maxvalue22=new DataPoint(low_xbp+1,120);
        MAXref.appendData(maxvalue22,false,1000);

        DataPoint minrefvalue11=new DataPoint(0,80);
        MINref.appendData(minrefvalue11,false,1000);
        DataPoint minrefvalue22=new DataPoint(low_xbp+1,80);
        MINref.appendData( minrefvalue22,false,1000);

        graphbp.getViewport().setXAxisBoundsManual(true);
        graphbp.getViewport().setMaxX(low_xbp+1);

        //endregion

        int index = getActivity().getIntent().getIntExtra("toGraph", 0);
        mSpinner.setSelection(index);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position) {
                    case 0:
                        Cholesterol.setVisibility(View.VISIBLE);
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
                        final TextView results=view.findViewById(R.id.set_bmi_text);
                        final LinearLayout bmi_default=view.findViewById(R.id.imageViewdefault);
                        final LinearLayout bmi_1=view.findViewById(R.id.imageView1);
                        final LinearLayout bmi_2=view.findViewById(R.id.imageView2);
                        final LinearLayout bmi_3=view.findViewById(R.id.imageView3);
                        final LinearLayout bmi_4=view.findViewById(R.id.imageView4);
                        final LinearLayout bmi_5=view.findViewById(R.id.imageView5);
                        results.setVisibility(View.GONE);
                        bmi_default.setVisibility(View.VISIBLE);
                        bmi_1.setVisibility(View.GONE);
                        bmi_2.setVisibility(View.GONE);
                        bmi_3.setVisibility(View.GONE);
                        bmi_4.setVisibility(View.GONE);
                        bmi_5.setVisibility(View.GONE);


                        b8=view.findViewById(R.id.show_weights);
                        b8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.list_weight, null);
                                alertDialog.setView(convertView);
                                ListView lv = (ListView) convertView.findViewById(R.id.list_weight);

                                this_weight = new ArrayList<String>();
                                this_date = new ArrayList<String>();
                                this_id = new ArrayList<>();

                                String typew="weight";
                                Cursor weight_cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+db.TABLE_RECORD + " WHERE " + db.COLUMN_RUSER + " = ? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user),typew});

                                this_date.clear();
                                this_weight.clear();
                                this_id.clear();


                                if (weight_cursor.moveToFirst()) {
                                    do {
                                        this_id.add(weight_cursor.getInt(weight_cursor.getColumnIndex(db.COLUMN_RID)));
                                        this_date.add(getDateFromatedString(weight_cursor.getString(weight_cursor.getColumnIndex(db.COLUMN_RDATE))));
                                        this_weight.add(weight_cursor.getString(weight_cursor.getColumnIndex(db.COLUMN_RVALUE)));

                                    } while (weight_cursor.moveToNext());
                                }

                                if(this_id.isEmpty()){
                                    TextView caution = convertView.findViewById(R.id.weight_caution);
                                    caution.setVisibility(View.VISIBLE);
                                }
                                weight_cursor.close();

                                listAdapter = new MedListAdapter(getActivity(),
                                        this_date,
                                        this_weight
                                );

                                lv.setAdapter(listAdapter);
                                alertDialog.show();
                                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                                        weight_id=this_id.get(position);
                                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
                                        alertDialog2.setTitle("Weight Record: "+ this_weight.get(position)+ " kg" );
                                        alertDialog2.setMessage("Do you want to delete this record?");
                                        alertDialog2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.deleteWeight(weight_id);
                                                this_date.remove(position);
                                                this_weight.remove(position);
                                                listAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        alertDialog2.show();
                                        return true;
                                    }
                                });
                            }
                        });

                        b4=view.findViewById(R.id.submit_button_bmi);
                        b4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                e1=view.findViewById(R.id.weight_value);
                                final String s1=e1.getText().toString();
                                if (TextUtils.isEmpty(s1)){
                                    Toast.makeText(getActivity(), "Insert your current weight", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    int i1=Integer.parseInt(s1);
                                    String typew="weight";
                                    Record record = new Record(i1, user, typew);
                                    db.addRecord(record);
                                    results.setVisibility(View.VISIBLE);
                                    String text;
                                    int height=db.getHeight(user);
                                    double mheight=(double) height/(double) 100;
                                    double IMC=i1/(mheight*mheight);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    if (IMC<18.5) {
                                        text="Caution, your BMI is "+ df.format(IMC) +". You are Underweight.";
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_2.setVisibility(View.GONE);
                                        bmi_3.setVisibility(View.GONE);
                                        bmi_4.setVisibility(View.GONE);
                                        bmi_5.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.VISIBLE);
                                    }else if (IMC>18.4 && IMC<25) {
                                        text="Congrats, your BMI is "+df.format(IMC)+". You weight is normal.";
                                        bmi_2.setVisibility(View.VISIBLE);
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.GONE);
                                        bmi_3.setVisibility(View.GONE);
                                        bmi_4.setVisibility(View.GONE);
                                        bmi_5.setVisibility(View.GONE);
                                    }else if (IMC>24.9 && IMC<30) {
                                        text="Caution, your BMI is "+df.format(IMC)+". You are Overweight (Pre Obesity).";
                                        bmi_3.setVisibility(View.VISIBLE);
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.GONE);
                                        bmi_2.setVisibility(View.GONE);
                                        bmi_4.setVisibility(View.GONE);
                                        bmi_5.setVisibility(View.GONE);
                                    }else if (IMC>29.9 && IMC<35) {
                                        text="Caution, your BMI is "+df.format(IMC)+". You are in Class I Obesity.";
                                        bmi_4.setVisibility(View.VISIBLE);
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.GONE);
                                        bmi_2.setVisibility(View.GONE);
                                        bmi_3.setVisibility(View.GONE);
                                        bmi_5.setVisibility(View.GONE);
                                    }else if (IMC>34.9 && IMC<40) {
                                        text="Caution, your BMI is "+df.format(IMC)+". You are in Class II Obesity.";
                                        bmi_5.setVisibility(View.VISIBLE);
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.GONE);
                                        bmi_2.setVisibility(View.GONE);
                                        bmi_3.setVisibility(View.GONE);
                                        bmi_4.setVisibility(View.GONE);
                                    }else{
                                        text="Caution, your BMI is "+df.format(IMC)+". You are in Class III Obesity.";
                                        bmi_5.setVisibility(View.VISIBLE);
                                        bmi_default.setVisibility(View.GONE);
                                        bmi_1.setVisibility(View.GONE);
                                        bmi_2.setVisibility(View.GONE);
                                        bmi_3.setVisibility(View.GONE);
                                        bmi_4.setVisibility(View.GONE);
                                    }
                                    results.setText(text);

                                }
                            }
                        });
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


    public static String getDateFromatedString(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return convetDateFormat.format(date);
    }
}




