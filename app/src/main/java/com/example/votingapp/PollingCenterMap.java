package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.votingapp.directionhelpers.FetchURL;
import com.example.votingapp.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PollingCenterMap extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyLine;
    String Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling_center_map);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        Address = message;

        place1 = new MarkerOptions().position(new LatLng((26.365520), (-80.165810))).title("Current Location");
        place2 = new MarkerOptions().position(new LatLng((26.384470), (-80.144287))).title("Polling Center");

        String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
        new FetchURL(PollingCenterMap.this).execute(url, "driving");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(place1);
        map.addMarker(place2);
    }

    private String getUrl(LatLng orgin, LatLng dest, String directionMode) {
        String str_orgin = "orgin=" + orgin.latitude + "," + orgin.longitude;
        String str_dest = "dest=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String paramaters = str_orgin + "&" + str_dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions" + output + "?" + paramaters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private String getUrlAddress(String Address) {
        Address = Address.replace(" ", "+");
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +Address+ "&key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyLine != null){
            currentPolyLine.remove();
        }
        currentPolyLine = map.addPolyline((PolylineOptions) values[0]);
    }
}