package com.aktechzone.propertydeal.Authentications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SharedPrefManager;
import com.aktechzone.propertydeal.SingletonVolley;
import com.aktechzone.propertydeal.activities.DrawerActivity;
import com.aktechzone.propertydeal.activities.DrawerAgentActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    TextView dontHaveAccount, forgotPassword, logIn;
    EditText eMail, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        setListeners();
    }

    private void setListeners() {

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                String email = eMail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (email.equals("") || pass.equals("")) {
                    Toast.makeText(SignIn.this, "Wrong UserName/Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = MyProgressDialog.showDialog(SignIn.this, "Loading . .");
                    if (email.equals("1")) {
                        intent = new Intent(SignIn.this, DrawerActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    } else if (email.equals("3")) {
                        intent = new Intent(SignIn.this, DrawerAgentActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    } else if (email.equals("2")) {
                        intent = new Intent(SignIn.this, DrawerActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    } else {
                        Toast.makeText(SignIn.this, "Not verified", Toast.LENGTH_SHORT).show();
                    }
                    // sendToServerForVerify(email, pass);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, ForgotPassInput.class));
            }
        });

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosignup = new Intent(SignIn.this, SignUp.class);
                startActivity(gotosignup);
            }
        });
    }

    private void sendToServerForVerify(final String userEmail, final String userPassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONObject objectUserDetail = jsonObject.getJSONObject("user");
                        String id = objectUserDetail.getString("id");
                        String name = objectUserDetail.getString("name");
                        String email = objectUserDetail.getString("email");
                        String phone = objectUserDetail.getString("phone");
                        String apiToken = objectUserDetail.getString("api_token");
                        String statusInner = objectUserDetail.getString("status");
                        Log.wtf("api_token_login", apiToken);
                        Log.wtf("innerStatus", statusInner);
                        String type = "";
                        Intent intent = null;
                        if (statusInner.equals("1")) {
                            type = "admin";
                            intent = new Intent(SignIn.this, DrawerActivity.class);
                        } else if (statusInner.equals("3")) {
                            type = "agent";
                            intent = new Intent(SignIn.this, DrawerAgentActivity.class);
                        } else if (statusInner.equals("2")) {
                            type = "manager";
                            intent = new Intent(SignIn.this, DrawerActivity.class);
                        } else {
                            Toast.makeText(SignIn.this, "Not verified", Toast.LENGTH_SHORT).show();
                        }
                        if (intent != null) {
                            SharedPrefManager.getInstance(getApplicationContext()).userProfile(name, email, phone, apiToken, type, id);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(SignIn.this, "Wrong UserName/Password!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignIn.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", userPassword);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void init() {
        eMail = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logIn);
        forgotPassword = findViewById(R.id.forgot_password);
        dontHaveAccount = findViewById(R.id.dont_have_account);
    }
}

