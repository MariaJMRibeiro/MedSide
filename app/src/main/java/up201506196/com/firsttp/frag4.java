package up201506196.com.firsttp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import im.delight.android.location.SimpleLocation;


public class frag4 extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    DatabaseHelper db1;
    SQLiteDatabase sqLiteDatabase1;
    DatabaseHelper db0;
    SQLiteDatabase sqLiteDatabase0;
    TextView title;
    private SimpleLocation location;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_frag4, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerlocation);
        title = view.findViewById(R.id.current_loc);

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

        SupportMapFragment mapFrag = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        location = new SimpleLocation(getActivity());
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getActivity());
        }
        double la = location.getLatitude();
        double lo = location.getLongitude();
        final LatLng my = new LatLng(la, lo);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                switch(i){
                    case 0:
                        mMap.clear();
                        title.setVisibility(View.VISIBLE);
                        mMap.addMarker(new MarkerOptions().position(my).title("This is you"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my,14));
                        break;
                    case 1:
                        mMap.clear();
                        title.setVisibility(View.GONE);
                        mMap.addMarker(new MarkerOptions().position(my).title("This is you"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my,14));

                        db0 = new DatabaseHelper(getActivity());
                        sqLiteDatabase0 = db0.getReadableDatabase();

                        Cursor cursor0;
                        double Lat0;
                        double Lng0;
                        String ph_title;
                        cursor0 = sqLiteDatabase0.rawQuery("SELECT * FROM "+db0.TABLE_LOC + " WHERE " + db0.COLUMN_LTYPE + "=?", new String[]{"pharmacy"});
                        if (cursor0.moveToFirst()) {
                            do {
                                Lat0=Double.parseDouble(cursor0.getString(cursor0.getColumnIndex(db0.COLUMN_LLAT)));
                                Lng0=Double.parseDouble(cursor0.getString(cursor0.getColumnIndex(db0.COLUMN_LLNG)));
                                ph_title=cursor0.getString(cursor0.getColumnIndex(db0.COLUMN_LTITLE));
                                LatLng hosp = new LatLng(Lat0, Lng0);
                                mMap.addMarker(new MarkerOptions().position(hosp).title(ph_title));

                            } while (cursor0.moveToNext());
                        }
                        cursor0.close();
                        break;

                    case 2:
                        mMap.clear();
                        title.setVisibility(View.GONE);
                        mMap.addMarker(new MarkerOptions().position(my).title("This is you"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my,14));

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
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

    }

    public void onMapReady (GoogleMap googleMap){

        mMap = googleMap;
        mMap.setIndoorEnabled(true);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
    }



}