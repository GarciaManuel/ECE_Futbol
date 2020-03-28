package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Maps extends AppCompatActivity implements OnMapReadyCallback, LocationListener, AdapterView.OnItemSelectedListener {

    private TextView addressText;

    private static final String STATE_SPINNER = "spinner";
    private int selectedMatch;

    private List<String> matchesArray;
    private List<String> latArray;
    private List<String> longArray;

    private GoogleMap mMap;

    private LocationManager locationManager;
    private String provider;

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION = 123;

    // <----------- Permissions functions ----------->
    private void askPermissions() {
        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                REQUEST_LOCATION
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        matchesArray = new ArrayList<>();
        latArray = new ArrayList<>();
        longArray = new ArrayList<>();

        addressText = (TextView) findViewById(R.id.address);
        matchesArray.add(getResources().getString(R.string.yourLocation));
        latArray.add("0");
        longArray.add("0");
        latArray.addAll(Arrays.asList(getResources().getStringArray(R.array.lat_array)));
        longArray.addAll(Arrays.asList(getResources().getStringArray(R.array.long_array)));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            matchesArray.addAll(extras.getStringArrayList("matches"));
        }

        if (savedInstanceState != null) {
            selectedMatch = savedInstanceState.getInt(STATE_SPINNER, 0);
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            // oups couldn't find you message
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    public void takePhoto(View view) {
        Intent photosActivity = new Intent(Maps.this, Photos.class);
        startActivity(photosActivity);
    }



    public void getAddress(double lat, double lng) {
        String fullAdd = null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);
                addressText.setText(fullAdd);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                // if you want only city or pin code use following code //
                   /* String Location = address.getLocality();
                    String zip = address.getPostalCode();
                    String Country = address.getCountryName(); */
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // <----------- Spinner functions ----------->
    public void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.matches_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, matchesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedMatch);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedMatch = position;
        getAddress(Double.parseDouble(latArray.get(position)), Double.parseDouble(longArray.get(position)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SPINNER, selectedMatch);
    }

    // <----------- Map functions ----------->
    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < matchesArray.size(); i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latArray.get(i)),
                            Double.parseDouble(longArray.get(i))))
                    .title(matchesArray.get(i)));
        }
        mMap = googleMap;
        initSpinner();
    }

    @Override
    public void onLocationChanged(Location location) {
        latArray.set(0, Double.toString(location.getLatitude()));
        longArray.set(0, Double.toString(location.getLongitude()));
//        latitudeField.setText(String.valueOf(lat));
//        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
