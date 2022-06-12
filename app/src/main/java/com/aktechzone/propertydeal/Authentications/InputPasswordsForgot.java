package com.aktechzone.propertydeal.Authentications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class InputPasswordsForgot extends AppCompatActivity {

    ProgressDialog progressDialog;
    private EditText etPassword, etConfirmPassword;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_passwords_forgot);
        init();
        setListener();
    }

    private void setListener() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = etPassword.getText().toString().trim();
                String confirmPass = etConfirmPassword.getText().toString().trim();
                if (!pass.equals("") && !confirmPass.equals("")) {
                    if (pass.equals(confirmPass)) {
                        progressDialog = MyProgressDialog.showDialog(InputPasswordsForgot.this, "Loading . .");
                        sendNewPassword(pass);
                    } else {
                        Toast.makeText(InputPasswordsForgot.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InputPasswordsForgot.this, "Fill both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNewPassword(final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.UPDATE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        Toast.makeText(InputPasswordsForgot.this, "You have reset your password.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InputPasswordsForgot.this, SignIn.class));
                        finish();
                    } else {
                        Toast.makeText(InputPasswordsForgot.this, message, Toast.LENGTH_SHORT).show();
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
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("id", getIntent().getExtras().getString("id"));
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void init() {
        etPassword = findViewById(R.id.etForgotPassEnter);
        etConfirmPassword = findViewById(R.id.etForgotConPassEnter);
        btnOk = findViewById(R.id.btnDoneForgotInpPass);
    }
}
