package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeoMap extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_map);
        setMapData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        TextView latTextViev = findViewById(R.id.latGeoMap);
        TextView lonTextViev = findViewById(R.id.lonGeoMap);
        double lat = getIntent().getDoubleExtra("lat", 0);
        double lon = getIntent().getDoubleExtra("lon", 0);
        latTextViev.setText(String.valueOf(lat));
        lonTextViev.setText(String.valueOf(lon));

        LatLng sydney = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void setMapData(){
        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String country = getIntent().getStringExtra("country");


        TextView idTextViev = findViewById(R.id.idGeoMap);
        TextView nameTextViev = findViewById(R.id.nameGeoMap);
        TextView countryTextViev = findViewById(R.id.countryGeoMap);


        idTextViev.setText(id);
        nameTextViev.setText(name);
        countryTextViev.setText(country);


    }



}