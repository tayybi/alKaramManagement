package com.aktechzone.propertydeal.Authentications;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmationCodeActivity extends AppCompatActivity {

    private EditText e1, e2, e3, e4, e5, e6;
    private TextView tvResendCode, tvShowPhone;
    private Button goNext;
    private FirebaseAuth fbAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationsCallBack;
    String phoneVerificationId;
    public static PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_code);
        fbAuth = FirebaseAuth.getInstance();
        phoneVerificationId = getIntent().getExtras().getString("verificationID");
        init();
        setListener();
        setListenersForRequestFocus();
    }

    private void init() {
        e1 = findViewById(R.id.editText1);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        e4 = findViewById(R.id.editText4);
        e5 = findViewById(R.id.editText5);
        e6 = findViewById(R.id.editText6);
        tvResendCode = findViewById(R.id.tvResendCodeNew);
        tvShowPhone = findViewById(R.id.tvShowPhoneOnConfirmation);
        tvShowPhone.setText(getIntent().getExtras().getString("phone"));
        goNext = findViewById(R.id.ivNextFromConfirmation);
    }

    private void setListener() {
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = e1.getText().toString().trim() +
                        e2.getText().toString().trim() +
                        e3.getText().toString().trim() +
                        e4.getText().toString().trim() +
                        e5.getText().toString().trim() +
                        e6.getText().toString().trim();
                if (!(code.length() < 6)) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(ConfirmationCodeActivity.this, "enter correct code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVerificationCallBacks();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(getIntent().getExtras().getString("phone"), 60, TimeUnit.SECONDS, ConfirmationCodeActivity.this, verificationsCallBack, resendingToken);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        fbAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ConfirmationCodeActivity.this, "verified successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmationCodeActivity.this, InputPasswordsForgot.class);
                    intent.putExtra("id", getIntent().getExtras().getString("id"));
                    startActivity(intent);
                    finish();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(ConfirmationCodeActivity.this, "invalid code!", Toast.LENGTH_SHORT).show();
                    Log.wtf("tag", "Entered Code Invalid!");
                } else {
                    Log.wtf("tag", "signInWithPhoneAuthCredential");
                }
            }
        });
    }

    private void setVerificationCallBacks() {
        verificationsCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.wtf("input_confirmation", "invalid credential!");
                    Toast.makeText(ConfirmationCodeActivity.this, "invalid credential!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.wtf("input_confirmation", "SMS exceeded!");
                    Toast.makeText(ConfirmationCodeActivity.this, "SMS Exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                tvResendCode.setTextColor(Color.GRAY);
                phoneVerificationId = s;
                resendingToken = forceResendingToken;
                Toast.makeText(ConfirmationCodeActivity.this, "code sent to your mobile number . .", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setListenersForRequestFocus() {
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e2.requestFocus();
                e2.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                e6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = e1.getText().toString().trim() +
                        e2.getText().toString().trim() +
                        e3.getText().toString().trim() +
                        e4.getText().toString().trim() +
                        e5.getText().toString().trim() +
                        e6.getText().toString().trim();
                if (!(code.length() < 6)) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(ConfirmationCodeActivity.this, "enter correct code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
