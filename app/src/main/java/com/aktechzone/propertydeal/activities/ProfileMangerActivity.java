package com.aktechzone.propertydeal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.R;

public class ProfileMangerActivity extends AppCompatActivity {
    TextView name, email, phone, desig;
    private Button btnCall, btnMessage, btnEmail;
    ImageView goBoack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manger);
        init();
        setListener();
    }

    private void setListener() {
        goBoack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + getIntent().getExtras().getString("PHONE"));
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getIntent().getExtras().getString("EMAIL")});
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProfileMangerActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + getIntent().getExtras().getString("PHONE"))));
            }
        });
    }

    private void init() {
        goBoack = findViewById(R.id.go_back);
        name = findViewById(R.id.nameUPro);
        phone = findViewById(R.id.phoneUPro);
        email = findViewById(R.id.emailUpro);
        desig = findViewById(R.id.desigUpro);
        name.setText(getIntent().getExtras().getString("NAME"));
        phone.setText(getIntent().getExtras().getString("EMAIL"));
        email.setText(getIntent().getExtras().getString("PHONE"));
        if (getIntent().getExtras().getString("FLAGETYPE").equalsIgnoreCase("manager")){
            desig.setText("Manager");
        }else{
            desig.setText("Sale Executive");
        }

        btnCall = findViewById(R.id.callUpro);
        btnMessage = findViewById(R.id.smsUpro);
        btnEmail = findViewById(R.id.btemailUpro);
    }
}