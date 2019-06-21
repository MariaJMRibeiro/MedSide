package up201506196.com.firsttp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.database.Cursor;
import java.util.ArrayList;


public class frag1 extends Fragment {


    DatabaseHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    MedListAdapter listAdapter ;
    ListView med_list;

    ArrayList<String> name;
    ArrayList<String> quantity;

    public frag1() {
        // Required empty public constructor
    }

    Button b1,b2;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_frag1, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        b1 = (Button) view.findViewById(R.id.button_add);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getActivity().getIntent().getIntExtra("key_email",0);
                Intent i= new Intent(getActivity(),Add_Medication.class);
                i.putExtra("key_email", user);
                startActivity(i);

            }
        });
        b2 = (Button) view.findViewById(R.id.button_reset);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getActivity().getIntent().getIntExtra("key_email",0);
                Intent i= new Intent(getActivity(),Delete_Medication.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });
        med_list = (ListView) view.findViewById(R.id.list_med);

        name = new ArrayList<String>();

        quantity = new ArrayList<String>();

        sqLiteHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        int user = getActivity().getIntent().getIntExtra("key_email",0);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+sqLiteHelper.TABLE_MEDICATION + " WHERE " + sqLiteHelper.COLUMN_MUSER + " = ? " ,new String[] {String.valueOf(user)});

        name.clear();
        quantity.clear();

        if (cursor.moveToFirst()) {
            do {

                name.add(cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_MNAME)));

                quantity.add(cursor.getString(cursor.getColumnIndex(sqLiteHelper.COLUMN_MQUANTITY)));



            } while (cursor.moveToNext());
        }

        listAdapter = new MedListAdapter(getActivity(),
                name,
                quantity
        );

        med_list.setAdapter(listAdapter);

        cursor.close();
    }


}






