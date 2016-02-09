package com.example.triospec.cabapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;
import android.content.Intent;

import java.sql.Date;
import java.sql.Timestamp;

import triospec.cabapp.activity.Login_Driver;
import triospec.cabapp.triospec.cabapp.backgroundtask.DriverResponseTask;
import triospec.cabapp.triospec.cabapp.backgroundtask.LoginTask;
import triospec.cabapp.triospec.cabapp.backgroundtask.RequestTask;

/**
 * Created by Cortex Hub on 12/22/2015.
 */
public class RequestActivity extends Activity{
    EditText start;
    EditText end;

    EditText price;
    String requestId="";
    String driverID ="";
    String driverName = "";
    String carModel ="";
    String distance = "";
    String timeStamp ="";
    String picture="";
    public static String iLoc = "";
    public static String bLoc = "";
    LoginTask loginTask;
    Login_Driver login_driver;
    RequestTask requestTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_request);
        Button mapping= (Button)findViewById(R.id.locationMapping);
        start = (EditText)findViewById(R.id.startLocation);
        end = (EditText)findViewById(R.id.endLocation);
        price = (EditText)findViewById(R.id.driverPrice);
        String loc1 = requestTask.data.get(1).toString().trim();
        start.setText(loc1.toString().trim());
        String loc2 = requestTask.data.get(2).toString().trim();
        end.setText(loc2.toString().trim());

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mapsActivity.setStartRoute(stPlace);
                mapLocations(v);
            }
        });
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    }

    public void mapLocations(View view){


        String place1 = start.getText().toString();
        String place2 = end.getText().toString();
        iLoc = start.getText().toString();
        bLoc = end.getText().toString();

        if(place2!=null||place2.equals("") && place1!=null||place1.equals("")) {
            Intent ii = new Intent(RequestActivity.this, MapsActivity.class);
            startActivity(ii);
        }else {
            Toast.makeText(getApplicationContext(),"One Of the coordinates is missing",Toast.LENGTH_LONG).show();
        }
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
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
}
    public void sendRequest(View v){
        String cabFare = price.getText().toString().trim();
        driverID = loginTask.data.get(0).toString().trim();
        driverName = login_driver.login_name.toString().trim();
        requestId = requestTask.data.get(0).toString().trim();
        String distance = requestTask.data.get(3);
      // Timestamp tsTemp = new java.sql.Timestamp(new Date().getTime());
      //  String times =  tsTemp.toString();
      //  times = times.substring(0, 19);

        DriverResponseTask driverResponseTask = new DriverResponseTask();
        driverResponseTask.execute(requestId,driverID,cabFare,driverName,distance);//params: RrequestId, DriverID, Price, DriverName, CarModel, Distance,TimeStamp,Picture
    }
    public void declineRequest(View v){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapsActivity ma = new MapsActivity();
        //fragmentTransaction.add(R.id.frame_container,ma);


    }
}
