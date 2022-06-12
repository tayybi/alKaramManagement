package com.aktechzone.propertydeal.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.R;

public class ProfileSellerPropertyActivity extends AppCompatActivity {

    TextView date, name, contact, city, phase, block, plot, demand, refrence, desig, tvCatName, tvNote;
    private Button btnCall, btnMessage, btnEmail;
    ImageView goBoack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_seller_property);
        init();
        setListeners();
    }

    private void setListeners() {
        goBoack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + getIntent().getExtras().getString("contact"));
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("message/rfc822");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
//                try {
//                    startActivity(Intent.createChooser(intent, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(ProfileSellerPropertyActivity.this, "no email found", Toast.LENGTH_SHORT).show();
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + getIntent().getExtras().getString("contact"))));
            }
        });

    }

    private void init() {
        btnCall = findViewById(R.id.btnCallBuyer);
        btnMessage = findViewById(R.id.btnMessageBuyer);
        btnEmail = findViewById(R.id.btnEmailBuyer);
        goBoack = findViewById(R.id.go_back);
        date = findViewById(R.id.datePro);
        name = findViewById(R.id.namePro);
        contact = findViewById(R.id.contactPro);
        refrence = findViewById(R.id.refPro);
        demand = findViewById(R.id.budgetPro);
        city = findViewById(R.id.cityPro);
        block = findViewById(R.id.blockPro);
        phase = findViewById(R.id.phasePro);
        plot = findViewById(R.id.plotPro);
        desig = findViewById(R.id.designationPro);
        tvCatName = findViewById(R.id.tvCatNameSeller);
        tvNote = findViewById(R.id.tvNoteSeller);
        if (getIntent().getExtras().getString("createdBy").equalsIgnoreCase("agent")) {
            desig.setText("Sale Executive");
        } else {
            desig.setText(getIntent().getExtras().getString("createdBy"));
        }
        name.setText(getIntent().getExtras().getString("name"));
        date.setText(getIntent().getExtras().getString("date"));
        refrence.setText(getIntent().getExtras().getString("refrence"));
        demand.setText(getIntent().getExtras().getString("demand"));
        city.setText(getIntent().getExtras().getString("city"));
        block.setText(getIntent().getExtras().getString("block"));
        contact.setText(getIntent().getExtras().getString("contact"));
        phase.setText(getIntent().getExtras().getString("phase"));
        plot.setText(getIntent().getExtras().getString("plot"));
        tvCatName.setText(getIntent().getExtras().getString("catName"));
        tvNote.setText(getIntent().getExtras().getString("note"));
    }

}
