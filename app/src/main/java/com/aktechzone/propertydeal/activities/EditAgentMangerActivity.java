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
import android.widget.TextView;
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

public class EditAgentMangerActivity extends AppCompatActivity {

    ImageView ivGoBack;
    RadioGroup rgOfAddEmployee;
    Button btRegisterEmployee;
    EditText etName, etPhone, etPassword, etEmail, etconfirmPassword;
    TextView heading;
    String idOfEmployee, flageType;
    ProgressDialog progressDialog;

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

                if (!password.equals("") && !confirmPassword.equals("")) {
                    if (password.equals(confirmPassword)) {
                        progressDialog = MyProgressDialog.showDialog(EditAgentMangerActivity.this, "Loading . .");
                        sendToServerForVerify(name, email, phone, password, confirmPassword, idOfEmployee);
                    } else {
                        Toast.makeText(EditAgentMangerActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog = MyProgressDialog.showDialog(EditAgentMangerActivity.this, "Loading . .");
                    sendToServerForVerify(name, email, phone, password, confirmPassword, idOfEmployee);
                }
            }
        });
    }

    private void init() {
        btRegisterEmployee = findViewById(R.id.register_employee);
        btRegisterEmployee.setText("Update");
        rgOfAddEmployee = findViewById(R.id.rgPersonTypeSignup);
        rgOfAddEmployee.setVisibility(View.GONE);
        heading = findViewById(R.id.headingOfeditmanger);
        heading.setText("Edit Detail");
        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etPassword = findViewById(R.id.password);
        etconfirmPassword = findViewById(R.id.confirm_password);
        ivGoBack = findViewById(R.id.back__arrow);

        flageType = getIntent().getExtras().getString("FLAGETYPE");
        idOfEmployee = getIntent().getExtras().getString("ID");
        etName.setText(getIntent().getExtras().getString("NAME"));
        etEmail.setText(getIntent().getExtras().getString("EMAIL"));
        etPhone.setText(getIntent().getExtras().getString("PHONE"));

    }

    private void sendToServerForVerify(final String uName, final String uEmail, final String uPhone, final String uPassword, final String uConPassword, final String idofemp) {
        StringRequest
                stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.EDIT_AGENT_MANAGER, new Response.Listener<String>() {
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
                        finish();
                    } else {
                        Toast.makeText(EditAgentMangerActivity.this, "Connection", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditAgentMangerActivity.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                if (!uName.equals(""))
                    params.put("name", uName);
                if (!uEmail.equals(""))
                    params.put("email", uEmail);
                if (!uPhone.equals(""))
                    params.put("phone", uPhone);
                if (!uPassword.equals(""))
                    params.put("password", uPassword);
                if (!uConPassword.equals(""))
                    params.put("password_confirmation", uConPassword);
                params.put("id", idofemp);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}