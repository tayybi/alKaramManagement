package com.aktechzone.propertydeal.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aktechzone.propertydeal.Authentications.SignIn;
import com.aktechzone.propertydeal.Authentications.SignUp;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {

    ImageView ivGoBack;
    RadioGroup rgOfAddEmployee;
    ProgressDialog progressDialog;
    Button btRegisterEmployee;
    EditText etName, etPhone, etPassword, etEmail, etconfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_add_employee);
        init();
        setlistener();
    }

    private void setlistener() {
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btRegisterEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etconfirmPassword.getText().toString().trim();
                String status = "";
                int id = rgOfAddEmployee.getCheckedRadioButtonId();
                if (id != -1) {
                    RadioButton radioButton = findViewById(id);
                    if (radioButton.getText().toString().equalsIgnoreCase("manager")) {
                        status = "2";
                    } else {
                        status = "3";
                    }
                    if (name.equals("") || email.equals("") || phone.equals("") || password.equals("") || confirmPassword.equals("")) {
                        Toast.makeText(AddEmployeeActivity.this, "enter all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals(confirmPassword)) {
                            progressDialog = MyProgressDialog.showDialog(AddEmployeeActivity.this, "Waiting. . .");
                            sendToServerForVerify(name, email, phone, password, confirmPassword, status);
                        } else {
                            Toast.makeText(AddEmployeeActivity.this, "password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "select person type", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        btRegisterEmployee = findViewById(R.id.register_employee);
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etPassword = findViewById(R.id.password);
        etconfirmPassword = findViewById(R.id.confirm_password);
        ivGoBack = findViewById(R.id.back__arrow);
        rgOfAddEmployee = findViewById(R.id.rgPersonTypeSignup);
    }

    private void sendToServerForVerify(final String uName, final String uEmail, final String uPhone, final String uPassword, final String uConPassword, final String uStatus) {
        StringRequest
                stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.SIGNUP_URI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("response", response);
                    if (status.equals("true")) {
                        if (uStatus.equals("2")) {
                            startActivity(new Intent(AddEmployeeActivity.this, AllMangerActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(AddEmployeeActivity.this, AllAgentActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(AddEmployeeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                progressDialog.cancel();
//              Toast.makeText(SignUp.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                Log.wtf("statusGoingAdd", uStatus);
                params.put("name", uName);
                params.put("email", uEmail);
                params.put("phone", uPhone);
                params.put("password", uPassword);
                params.put("password_confirmation", uConPassword);
                params.put("status", uStatus);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}