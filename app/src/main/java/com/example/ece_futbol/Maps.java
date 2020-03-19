package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maps extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private TextView addressText;

    private  static final String STATE_SPINNER = "spinner";
    private int selectedMatch;

    private String[] matchesArray;
    private String[] latArray;
    private String[] longArray;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        addressText = (TextView) findViewById(R.id.address);
        matchesArray = getResources().getStringArray(R.array.matches_array);
        latArray = getResources().getStringArray(R.array.lat_array);
        longArray = getResources().getStringArray(R.array.long_array);

        if (savedInstanceState != null) {
            selectedMatch = savedInstanceState.getInt(STATE_SPINNER, 0);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    public void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.matches_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.matches_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedMatch);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < matchesArray.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latArray[i]),
                            Double.parseDouble(longArray[i])))
                    .title(matchesArray[i]));
        }
        mMap = googleMap;
        initSpinner();
    }

    public void getAddress( double lat, double lng){
        String fullAdd=null;
        try{
            Geocoder geocoder= new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
            if(addresses.size()>0){
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);
                addressText.setText(fullAdd);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),15));
                // if you want only city or pin code use following code //
                   /* String Location = address.getLocality();
                    String zip = address.getPostalCode();
                    String Country = address.getCountryName(); */
            }


        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMatch = position;
        getAddress(Double.parseDouble(latArray[position]), Double.parseDouble(longArray[position]));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SPINNER, selectedMatch);
    }
}
