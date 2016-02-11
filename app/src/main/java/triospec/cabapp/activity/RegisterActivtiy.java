package triospec.cabapp.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.triospec.cabapp.R;

import java.util.IllegalFormatCodePointException;

import triospec.cabapp.triospec.cabapp.backgroundtask.RegisterTask;


public class RegisterActivtiy extends Activity {
    private static final String TAG = RegisterActivtiy.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText surname;
    private EditText cellphone;
    private EditText address;
    private EditText licenseNo;

    private EditText inputPassword;
    private EditText conPassword;
    private ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activtiy);

        inputFullName = (EditText)findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        inputPassword = (EditText) findViewById(R.id.password);
        cellphone = (EditText)findViewById(R.id.cellphone);
        address = (EditText)findViewById(R.id.address);
        licenseNo = (EditText)findViewById(R.id.license);
        conPassword = (EditText)findViewById(R.id.confirmpassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Login_Driver.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void userReg(View view){
        String name = inputFullName.getText().toString().trim();
        String sname = surname.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String cell = cellphone.getText().toString().trim();
        String licenseNum = licenseNo.getText().toString().trim();
        String home = address.getText().toString().trim();
        String ID = name.substring(0,4)+licenseNum.substring(0,4);
        String conPass = conPassword.getText().toString().trim();
        /**
        if(conPass!=password){
            conPassword.setError("Your Passwords don't match! Please re-enter.....");
            conPassword.setFocusable(true);
            conPassword.requestFocus();
        }
        **/
        RegisterTask registerTask = new RegisterTask(this);
        registerTask.execute(name,sname,password,cell,licenseNum,home,ID);
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url

    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivtiy.this,
                                Login_Driver.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
     **/
}
