package triospec.cabapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.triospec.cabapp.MainScreenActivity;
import com.example.triospec.cabapp.R;

import triospec.cabapp.triospec.cabapp.backgroundtask.DriverDetailTask;
import triospec.cabapp.triospec.cabapp.backgroundtask.LoginTask;

/**
 * Created by Cortex Hub on 12/20/2015.
 */
public class Login_Driver extends Activity {
    private TextView RegisterButton;
    private Button loginButton;
    public EditText name,pass;
    private TextView users;
    public String login_name, login_pass;
    LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__driver);

        loginButton = (Button)findViewById(R.id.btnLogin);
        RegisterButton = (TextView)findViewById(R.id.register_users);
        name =(EditText)findViewById(R.id.username);
        pass =(EditText)findViewById(R.id.password);
        users = (TextView)findViewById(R.id.register_users);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Driver.this,RegisterActivtiy.class);
                startActivity(i);
            }
        });

    }
    public void userLogin(View v ) {
        login_name = name.getText().toString();
        login_pass = pass.getText().toString();

        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(login_name, login_pass);
    }

}

