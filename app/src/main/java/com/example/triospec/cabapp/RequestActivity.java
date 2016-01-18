package com.example.triospec.cabapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;
import android.content.Intent;

/**
 * Created by Cortex Hub on 12/22/2015.
 */
public class RequestActivity extends Activity{
    EditText start;
    EditText end;
    MapsActivity mapsActivity;
    Button mapping;
    MapsActivity.DownloadTask downloadTask;
    public final static String locationKey="com.example.triospec.cabapp.location1_key";
    public final static String location2Key="com.example.triospec.cabapp.location2_key";
    public static String iLoc = "";
    public static String bLoc = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_request);
        Button mapping= (Button)findViewById(R.id.locationMapping);

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mapsActivity.setStartRoute(stPlace);
                mapLocations(v);
            }
        });

    }

    public void mapLocations(View view){
        start = (EditText)findViewById(R.id.startLocation);
        end = (EditText)findViewById(R.id.endLocation);

        String place1 = start.getText().toString();
        String place2 = end.getText().toString();
        iLoc = start.getText().toString();
        bLoc = end.getText().toString();

        if(place2!=null||place2.equals("") && place1!=null||place1.equals("")) {
            Intent ii = new Intent(RequestActivity.this, MapsActivity.class);
            startActivity(ii);
        }/**
        if(place1!=""||place1.equals("")) {
            Intent i = new Intent(RequestActivity.this, MapsActivity.class);
            i.putExtra(locationKey, place1);
            startActivity(i);
        }
        **/

    }

/**
    public String startMapping(int cordNum){
        MapsActivity cord = new MapsActivity();
        EditText start = (EditText)findViewById(R.id.startLocation);
        String location1 = start.getText().toString();


        EditText end = (EditText)findViewById(R.id.endLocation);
        String location2 = end.getText().toString();


        if(location1 != null || location1.equals("")&& (cordNum==1)){

            return location1;
        }

        if(location2 !=null || location2.equals("")&& cordNum == 2){

            return location2;
        }
        return null;

    }
 **/
}
