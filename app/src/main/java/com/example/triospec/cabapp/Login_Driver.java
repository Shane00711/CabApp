package com.example.triospec.cabapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Cortex Hub on 12/20/2015.
 */
public class Login_Driver extends Activity{
    Button loginButton;
    EditText name,pass;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__driver);

        loginButton = (Button)findViewById(R.id.btnLogin);
        name =(EditText)findViewById(R.id.username);
        pass =(EditText)findViewById(R.id.password);

        TextView users = (TextView)findViewById(R.id.register_users);

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RegisterActivtiy.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(name.getText().toString().equals("")&& pass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Gaining Access.....",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login_Driver.this, HomeScreen.class);
                    startActivity(i);
                    //mapping.start();
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong Details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


  /**  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login__driver, menu);
        return true;
    }**/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
