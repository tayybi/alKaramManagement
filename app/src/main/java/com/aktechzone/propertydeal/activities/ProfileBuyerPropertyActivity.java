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

public class ProfileBuyerPropertyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView name, date, ref, budjet, area, note, contact, desig, tvCatName;
    ImageView goBoack;
    private Button btnCall, btnMessage, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_property);
        init();
        setListeners();
        goBoack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void setListeners() {
        btnCall.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
    }

    private void init() {
        btnCall = findViewById(R.id.btnCallBuyer);
        btnMessage = findViewById(R.id.btnMessageBuyer);
        btnEmail = findViewById(R.id.btnEmailBuyer);
        goBoack = findViewById(R.id.go_back);
        name = findViewById(R.id.namePro);
        date = findViewById(R.id.datePro);
        ref = findViewById(R.id.refPro);
        budjet = findViewById(R.id.budgetPro);
        area = findViewById(R.id.areaPro);
        note = findViewById(R.id.notePro);
        contact = findViewById(R.id.contactPro);
        desig = findViewById(R.id.designationPro);
        tvCatName = findViewById(R.id.tvCatNameBuyerProfile);
        if (getIntent().getExtras().getString("createdBy").equalsIgnoreCase("agent")) {
            desig.setText("Sale Executive");
        } else {
            desig.setText(getIntent().getExtras().getString("createdBy"));
        }
        name.setText(getIntent().getExtras().getString("name"));
        date.setText(getIntent().getExtras().getString("date"));
        ref.setText(getIntent().getExtras().getString("ref"));
        budjet.setText(getIntent().getExtras().getString("budjet"));
        area.setText(getIntent().getExtras().getString("area"));
        note.setText(getIntent().getExtras().getString("note"));
        contact.setText(getIntent().getExtras().getString("contact"));
        tvCatName.setText(getIntent().getExtras().getString("catName"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCallBuyer:
                Uri number = Uri.parse("tel:" + getIntent().getExtras().getString("contact"));
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                break;
            case R.id.btnMessageBuyer:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + getIntent().getExtras().getString("contact"))));
                break;
            case R.id.btnEmailBuyer:
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("message/rfc822");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
//                try {
//                    startActivity(Intent.createChooser(intent, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(this, "no email found", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
