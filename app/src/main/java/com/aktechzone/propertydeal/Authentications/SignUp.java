package com.aktechzone.propertydeal.Authentications;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class SignUp extends Activity {

    Button btRegister;
    RadioGroup rgOfSignUp;
    ImageView ivBackArrow;
    EditText etName, etPhone, etPassword, etEmail, etconfirmPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_add_employee);
        init();
        setlistener();
    } ////on create

    private void setlistener() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etconfirmPassword.getText().toString().trim();
                String status = "";
                int id = rgOfSignUp.getCheckedRadioButtonId();
                if (id != -1) {
                    RadioButton radioButton = findViewById(id);
                    if (radioButton.getText().toString().equalsIgnoreCase("Manager")) {
                        status = "0";
                    } else {
                        status = "4";
                    }
                    if (name.equals("") || email.equals("") || phone.equals("") || password.equals("") || confirmPassword.equals("")) {
                        Toast.makeText(SignUp.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals(confirmPassword)) {
                            progressDialog = MyProgressDialog.showDialog(SignUp.this,"Loading . .");
                            sendToServerForVerify(name, email, phone, password, confirmPassword, status);
                        } else {
                            Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(SignUp.this, "Select person type", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        btRegister = findViewById(R.id.register_employee);
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etPassword = findViewById(R.id.password);
        etconfirmPassword = findViewById(R.id.confirm_password);
        ivBackArrow = findViewById(R.id.back__arrow);
        rgOfSignUp = findViewById(R.id.rgPersonTypeSignup);
    }

    private void sendToServerForVerify(final String uName, final String uEmail, final String uPhone, final String uPassword, final String uConPassword, final String status) {
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
                    Log.wtf("response",response);
                    if (status.equals("true")) {
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
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
                params.put("name", uName);
                params.put("email", uEmail);
                params.put("phone", uPhone);
                params.put("password", uPassword);
                params.put("password_confirmation", uConPassword);
                params.put("status", status);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

} ///main
