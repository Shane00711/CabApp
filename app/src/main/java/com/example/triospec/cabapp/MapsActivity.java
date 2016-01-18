package com.example.triospec.cabapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.util.Log;
import android.text.TextWatcher;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;

import static java.lang.Math.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private SensorManager sensorManager;
    private Sensor sensor;
    Button mapping;
    private double mAzimuth;
    private double mTilt;
    float[] mRotationMatrix = new float[16];
    float[] mValues = new float[3];
    private GoogleMap mMap;
    List<Overlay> mapOverlays;
    GeoPoint point1, point2;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    // GMapV2GetRouteDirection v2GetRouteDirection;
    LatLng fromPosition;
    LatLng toPosition;
    GoogleMap mGoogleMap;
    public MarkerOptions markerOptions;
    Location location;
    RequestActivity requestActivity;
    TextView tvDistanceDuration;

    DownloadTask placesDownloadTask;
    DownloadTask placeDetailDownloadTask;
    ConnectTask placeParserTask;
    ConnectTask placeDetailsParserTask;
    AutoCompleteTextView atvPlaces;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;



    GoogleMap mapp;
    ArrayList<LatLng> markerPoints;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    //Getting location from Request Activity




        //Getting a refernece to the AutoCompleteTextView
        atvPlaces = (AutoCompleteTextView)findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        //Adding textchange listener
        atvPlaces.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Creating a DownloadTask to download Google Places match
                placeDetailDownloadTask =new DownloadTask(PLACES);

                //Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                //Start downloading Google places
                //This causes to execute doInBackground() of DownloadTask class
                new ConnectTask(PLACES).execute(url);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Setting an item click Listener for the AutoCompleteTextView dropdown list
        atvPlaces.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
                ListView lv = (ListView)arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String,String>)adapter.getItem(index);

                //Creating a DonwloadTask to Download places details of the selected place
                placeDetailDownloadTask = new DownloadTask(PLACES_DETAILS);

                //Getting url to the google places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                //Start downloading places deatails
                //This causes to execute doInBackground() of DownloadTask class
               // placeDetailDownloadTask.execute(url);
                new ConnectTask(PLACES_DETAILS).execute(url);
            }
        });
        tvDistanceDuration = (TextView)findViewById(R.id.tv_distance_time);
        Button btnDraw = (Button) findViewById(R.id.locationMapping);
        Button mapping = (Button) findViewById(R.id.showRoute);
       // sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
       // Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
       // sensorManager.registerListener(this, sensor, 1600);

        mapp = mapFragment.getMap();

        markerPoints = new ArrayList<LatLng>();

       // sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);







        mapp.setOnMapClickListener(new OnMapClickListener() {
            public void onMapClick(LatLng point) {
                if (markerPoints.size() >= 10) {
                    return;
                }

                markerPoints.add(point);

                MarkerOptions options = new MarkerOptions();

                options.position(point);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                mapp.addMarker(options);

               // new ReverseGeocodingTask(getBaseContext()).execute(point);
            }
        });

        mapp.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                mapp.clear();
                markerPoints.clear();
            }
        });

        mapping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markerPoints.size() >= 2) {
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();

                    downloadTask.execute(url);
                }
            }
        });

        // Enabling MyLocation in Google Map
        //  mGoogleMap.setMyLocationEnabled(true);
        //mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        // mGoogleMap.getUiSettings().setCompassEnabled(true);
        // mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        // mGoogleMap.setTrafficEnabled(true);
        // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        // markerOptions = new MarkerOptions();
        // fromPosition = new LatLng(11.663837, 78.147297);
        // toPosition = new LatLng(11.723512, 78.466287);
        // GetRouteTask getRoute = new GetRouteTask();
        // getRoute.execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private String getAutoCompleteUrl(String place){
        String key = "AIzaSyD9TO2KYOyHWEAGLnKSwSn_cauTJLTZsUo";

        //place to be searched
        String input = "input="+place;

        //placetype to be searched
        String types = "types=geocode";

        //Sensor enabled
        String sensor = "sensor=false";

        //Building parameter for web services
        String parameters = input+"&"+types+"&"+sensor+"&"+key;

        //Output format
        String output = "json";

        //Building the url for the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

        return url;
    }

    private String getPlaceDetailsUrl(String ref){
        String key = "AIzaSyD9TO2KYOyHWEAGLnKSwSn_cauTJLTZsUo";

        //reference place
        String reference = "reference="+ref;

        //Sensor enabled
        String sensor = "sensor=false";

        //Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+key;

        //Output format
        String output = "json";

        //Building the url for the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;

        return url;
    }

    public void setStartRoute(){

        //Test if a can create my own locations
       // EditText starting = (EditText)findViewById(R.id.startLocation);
       String places = "Durban";//requestActivity.iLoc;
        List<Address>addressList = null;
        if(places!=null||places.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try{
                addressList = geocoder.getFromLocationName(places,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Address address = addressList.get(0);

        LatLng point = new LatLng(address.getLatitude(),address.getLongitude());

        markerPoints.add(point);
        MarkerOptions options = new MarkerOptions();
        options.position(point);
        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        mapp.addMarker(options);
    }

    public void setEndRoute(){
        String places = "Cape Town";//requestActivity.bLoc;
        List<Address>addressList = null;
        if(places!=null||places.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try{
                addressList = geocoder.getFromLocationName(places,1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Address address = addressList.get(0);
        LatLng point1 = new LatLng(address.getLatitude(),address.getLongitude());

        markerPoints.add(point1);
        MarkerOptions options1 = new MarkerOptions();
        options1.position(point1);
        if (markerPoints.size() == 2) {
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        mapp.addMarker(options1);
    }

    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_orgin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String waypoints = "";
        for (int i = 2; i < markerPoints.size(); i++) {
            LatLng point = (LatLng) markerPoints.get(i);
            if (i == 2)
                waypoints = "waypoints";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        String parameters = str_orgin + "&" + str_dest + "&" + sensor + "&" + waypoints;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public String downloadUrl(String strUrl) throws IOException {
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
        } catch (Exception e) {
            Log.d("Excetion while download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    /*
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    */

    public class DownloadTask extends AsyncTask<String, Void, String> {
        private int downloadType = 0;
        //Constructor
        public DownloadTask(int type){
            this.downloadType = type;
        }
        public DownloadTask(){

        }
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType){
                case PLACES:
                    //Creating ParserTask for parsering google Places
                    placeParserTask = new ConnectTask(PLACES);

                    placeParserTask.execute(result);

                    break;
                case PLACES_DETAILS:
                    placeDetailsParserTask = new ConnectTask(PLACES_DETAILS);

                    placeDetailsParserTask.execute(result);
            }
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String[] jsonData) {

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


        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all points in i-th routes
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { //Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { //Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                // Adding all the points in the routes to LineOption
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            tvDistanceDuration.setText("Distance: " + distance + " Duration: " + duration);
            // Drawing polyline in the google map for the i-th route
            mapp.addPolyline(lineOptions);
/*
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
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
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            mapp.addPolyline(lineOptions);
        }
  */
        }
    }

    private class ConnectTask extends AsyncTask<String,Integer,List<HashMap<String,String>>>{
        int parserType = 0;
        public ConnectTask(int type){
            this.parserType =type;
        }
        @Override
        protected List<HashMap<String,String>>doInBackground(String[]jsonData){
            JSONObject jObject;
            List<HashMap<String,String>>list = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                switch (parserType){
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJSONParser = new PlaceDetailsJSONParser();
                        list = placeDetailsJSONParser.parse(jObject);
                }
            } catch (Exception e){
                Log.d("Exception",e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,String>>result){
            switch (parserType){
                case PLACES:
                    String[]from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};

                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    atvPlaces.setAdapter(adapter);
                    break;
                case PLACES_DETAILS :
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    double longitude = Double.parseDouble(hm.get("lng"));

                    // Getting reference to the SupportMapFragment of the activity_main.xml
                    SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                    // Getting GoogleMap from SupportMapFragment
                   // mMap = fm.getMap();

                    LatLng point = new LatLng(latitude, longitude);

                    CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                    CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);

                    // Showing the user input location in the Google Map
                    mMap.moveCamera(cameraPosition);
                    mMap.animateCamera(cameraZoom);

                    MarkerOptions options = new MarkerOptions();
                    options.position(point);
                    options.title("Position");
                    options.snippet("Latitude:"+latitude+",Longitude:"+longitude);

                    // Adding the marker in the Google Map
                    mMap.addMarker(options);

                    break;
            }
            }
        }

    /**
     * @Override protected void onStop() {
     * super.onStop();
     * finish();
     * }
     **/

    public void onSearch(View view) {

        EditText loction_tf = (EditText) findViewById(R.id.TFAddress);
        String location = loction_tf.getText().toString();
        List<Address> andressList = null;
        if (location != null || location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                andressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = andressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng sydney = new LatLng(-32.983333, 17.8666670000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
        mMap.setBuildingsEnabled(true);

        setStartRoute();
        setEndRoute();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapp.setMyLocationEnabled(true);
        mMap.getUiSettings().isMyLocationButtonEnabled();
        }
    //}
    static float clamp(float x, float low, float high){
        return x < low ? low : (x> high ? high : x);
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng,Void,String>{
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(LatLng[]params){
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address>addresses = null;
            String addressText="";

            try{
                addresses = geocoder.getFromLocation(latitude,longitude,1);
            } catch (IOException e){
                e.printStackTrace();
            }

            if(addresses !=null && addresses.size()>0){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",address.getMaxAddressLineIndex()>0?address.getAddressLine(0):"",address.getLocality(),address.getCountryName());
            }
            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText){
            markerOptions.title(addressText);
            mMap.addMarker(markerOptions);
        }
    }





    // RequestActivity cord = new RequestActivity();


        // Add a marker in Sydney and move the camera

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // mMap.setMyLocationEnabled(true);



}
