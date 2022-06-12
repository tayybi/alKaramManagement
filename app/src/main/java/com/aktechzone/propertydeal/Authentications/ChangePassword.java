package com.aktechzone.propertydeal.Authentications;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SharedPrefManager;
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

public class ChangePassword extends AppCompatActivity {

    private EditText etNewPass, etConPass;
    private Button btnDone;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        setListener();
    }

    private void setListener() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPass.getText().toString().trim();
                String conPassword = etConPass.getText().toString().trim();
                if (!newPassword.equals("") && !conPassword.equals("")) {
                    if (newPassword.equals(conPassword)) {
                        progressDialog = MyProgressDialog.showDialog(ChangePassword.this, "Loading . .");
                        changePassword(newPassword);
                    } else {
                        Toast.makeText(ChangePassword.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(final String password) {
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
                        Toast.makeText(ChangePassword.this, "You have reset your password.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("id", SharedPrefManager.getInstance(getApplicationContext()).getId());
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void init() {
        etNewPass = findViewById(R.id.etNewPassCP);
        etConPass = findViewById(R.id.etConfirmPassCP);
        btnDone = findViewById(R.id.btnDoneCP);
    }
}
