package com.aktechzone.propertydeal.Authentications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgotPassInput extends AppCompatActivity {

    private EditText etPhone;
    private Button btnDone;
    private ProgressDialog progressDialog;
    public static PhoneAuthProvider.ForceResendingToken resendingToken;
    public String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationsCallBack;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_input);
        etPhone = findViewById(R.id.etForgotPassInput);
        btnDone = findViewById(R.id.btnForgotInp);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString().trim();
                if (!phone.equals("")) {
                    progressDialog = MyProgressDialog.showDialog(ForgotPassInput.this, "Loading . .");
                    verifyNumber(phone);
                } else {
                    Toast.makeText(ForgotPassInput.this, "Enter number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyNumber(final String phone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.FORGOT_PASS_PHONE_VERIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
//                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONObject dataObject = jsonObject.getJSONObject("user");
                        id = dataObject.getString("id");
                        Log.wtf("phoneLength", phone.length() + "");
                        String phoneA = phone.substring(1);
                        verifyPhoneNumberThroughFirebase("+92" + phoneA);

                    } else {
                        Toast.makeText(ForgotPassInput.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("phone", phone);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void verifyPhoneNumberThroughFirebase(String phone) {
        setVerificationCallBacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                verificationsCallBack);
    }

    private void setVerificationCallBacks() {
        verificationsCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                progressDialog.cancel();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(ForgotPassInput.this, "Error!! use correct format for number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(ForgotPassInput.this, "SMS Exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialog.dismiss();
                progressDialog.cancel();
                phoneVerificationId = s;
                resendingToken = forceResendingToken;
                Toast.makeText(getApplicationContext(), "code sent!", Toast.LENGTH_SHORT).show();
                String phone = etPhone.getText().toString();
                Toast.makeText(ForgotPassInput.this, phone, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPassInput.this, ConfirmationCodeActivity.class);
                intent.putExtra("verificationID", phoneVerificationId);
                intent.putExtra("phone", "+92" + phone);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        };
    }
}
