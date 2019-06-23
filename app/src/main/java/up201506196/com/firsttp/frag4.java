package up201506196.com.firsttp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class frag4 extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    DatabaseHelper db1;
    SQLiteDatabase sqLiteDatabase1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_frag4, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map,mapFragment).commit();
        mapFragment.getMapAsync(this);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerlocation);

        // Spinner click listener


        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Select a service");
        categories.add("Pharmacy");
        categories.add("Hospital");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void onMapReady (GoogleMap googleMap){

        mMap = googleMap;
        mMap.setMinZoomPreference(7);
        mMap.setIndoorEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        LatLng casa = new LatLng(41.17422668042116, -8.603814609119127);
        mMap.addMarker(new MarkerOptions().position(casa).title("Casa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(casa));

        db1 = new DatabaseHelper(getActivity());
        sqLiteDatabase1 = db1.getReadableDatabase();

        Cursor cursor;
        double Lat;
        double Lng;
        String h_title;
        cursor = sqLiteDatabase1.rawQuery("SELECT * FROM "+db1.TABLE_LOC + " WHERE " + db1.COLUMN_LTYPE + "=?", new String[]{"hospital"});
        if (cursor.moveToFirst()) {
            do {
                Lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db1.COLUMN_LLAT)));
                Lng=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db1.COLUMN_LLNG)));
                h_title=cursor.getString(cursor.getColumnIndex(db1.COLUMN_LTITLE));
                LatLng hosp = new LatLng(Lat, Lng);
                mMap.addMarker(new MarkerOptions().position(hosp).title(h_title));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }
}