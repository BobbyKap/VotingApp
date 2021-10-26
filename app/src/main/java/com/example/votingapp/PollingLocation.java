package com.example.votingapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.net.URL;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.votingapp.databinding.ActivityPollingLocationBinding;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class PollingLocation extends FragmentActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ActivityPollingLocationBinding binding;
    LatLng origin = new LatLng(0, 0);
    LatLng dest = new LatLng(0, 0);
    ProgressDialog progressDialog;
    String messageThrough;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        messageThrough = message;

        //Returns
        OriginClass originRunner = new OriginClass();
        DestinationClass destinationRunner = new DestinationClass();
        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((originRunner.execute(messageThrough)).get());
            String nodeLat = node.get("results").get(0).get("geometry").get("location").get("lat").toString();
            String nodeLng = node.get("results").get(0).get("geometry").get("location").get("lng").toString();
            double LatReal = Double.parseDouble(nodeLat);
            double LngReal = Double.parseDouble(nodeLng);
            origin = new LatLng(LatReal, LngReal);
            Log.d("Origin", String.valueOf(origin));
            Log.d("DebugLat", String.valueOf(LatReal));
            Log.d("DebugLng", String.valueOf(LngReal));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            @SuppressLint("WrongThread") final JsonNode node = new ObjectMapper().readTree((destinationRunner.execute(messageThrough)).get());
            if(node.get("election").get("id") != null){
                id = node.get("election").get("id").toString();
                String nodeLat = node.get("pollingLocations").get(0).get("latitude").toString();
                String nodeLng = node.get("pollingLocations").get(0).get("longitude").toString();
                double LatReal = Double.parseDouble(nodeLat);
                double LngReal = Double.parseDouble(nodeLng);
                dest = new LatLng(LatReal, LngReal);
                Log.d("Dest", String.valueOf(dest));
                Log.d("DebugLat", String.valueOf(LatReal));
                Log.d("DebugLng", String.valueOf(LngReal));
            }
            else if (Integer.parseInt(node.get("error").get("code").toString()) == 400) {
                Log.d("ElectionAvailable:", "No Elections are available right now.");
            }
        } catch(JsonProcessingException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            } catch(InterruptedException e){
                e.printStackTrace();
            } catch(ExecutionException e){
                e.printStackTrace();
            }
        binding = ActivityPollingLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        drawPolyLines();

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------


    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void drawPolyLines(){
        progressDialog = new ProgressDialog(PollingLocation.this);
        progressDialog.setMessage("Please Wait, Polyline between two locations is building.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Checks, whether start and end locations are captured
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        Log.d("url", url + "");
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("LinkedIn")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.addMarker(new MarkerOptions()
                .position(dest));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 15));

    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            progressDialog.dismiss();
            Log.d("result", result.toString());
            ArrayList points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&CA&key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
            Log.d("data", data);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------
    public class OriginClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < address.length; i++) {
                sb.append(address[i]);
            }
            String addressReal = sb.toString();
            addressReal = addressReal.replace(" ", "%20");
            String parameters = "address=" + addressReal + "&" + "&CA&key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss";
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/geocode/" + output + "?" + parameters;
            Log.d("url5", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DestinationClass extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... address) {
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < address.length; i++) {
                sb.append(address[i]);
            }
            String addressReal = sb.toString();
            addressReal = addressReal.replace(" ", "%20");
            String parameters = "key=AIzaSyCiyPGnl5Dq2tGyY04_KkbJZVAhAl1Gpss&address=" + addressReal;
            String url = "https://www.googleapis.com/civicinfo/v2/voterinfo?" + parameters;
            Log.d("url6", url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}