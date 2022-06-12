package com.aktechzone.propertydeal.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditBuyerActivity extends AppCompatActivity {

    Button btUpdate;
    ImageView ivBackArrow;
    TextView tvHeading;
    String buyerId;
    EditText etName, etDate, etBudjet, etContact, etRefrence, etArea, etNote;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        init();
        setListrener();
    }

    private void init() {
        btUpdate = findViewById(R.id.continueToupdate);
        btUpdate.setVisibility(View.VISIBLE);
        ivBackArrow = findViewById(R.id.back__arrow);
        tvHeading = findViewById(R.id.heading_of_buyer);
        tvHeading.setText("Buyer");
        etArea = findViewById(R.id.areaB);
        etBudjet = findViewById(R.id.budjetB);
        etDate = findViewById(R.id.dateB);
        etContact = findViewById(R.id.contactB);
        etName = findViewById(R.id.nameB);
        etNote = findViewById(R.id.noteB);
        etRefrence = findViewById(R.id.refrenceB);

        buyerId = getIntent().getExtras().getString("id");
        String date = getIntent().getExtras().getString("date");
        String name = getIntent().getExtras().getString("name");
        String contact = getIntent().getExtras().getString("contact");
        String budjet = getIntent().getExtras().getString("budjet");
        String refrence = getIntent().getExtras().getString("ref");
        String area = getIntent().getExtras().getString("area");
        String note = getIntent().getExtras().getString("note");

        etRefrence.setText(refrence);
        etName.setText(name);
        etNote.setText(note);
        etDate.setText(date);
        etContact.setText(contact);
        etBudjet.setText(budjet);
        etArea.setText(area);
        btUpdate.setVisibility(View.VISIBLE);

    }

    private void setListrener() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eref = etRefrence.getText().toString().trim();
                String ename = etName.getText().toString().trim();
                String enote = etNote.getText().toString().trim();
                String edate = etDate.getText().toString().trim();
                String econtact = etContact.getText().toString().trim();
                String ebudjet = etBudjet.getText().toString().trim();
                String earea = etArea.getText().toString().trim();
                if (eref.equals("") || ename.equals("") || enote.equals("") || edate.equals("") || econtact.equals("") || ebudjet.equals("") || earea.equals("")) {
                    Toast.makeText(EditBuyerActivity.this, "All field required", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = MyProgressDialog.showDialog(EditBuyerActivity.this, "Loading . .");
                    setDataToServer(buyerId, ename, eref, enote, edate, econtact, ebudjet, earea);
                }
            }
        });
    }

    private void setDataToServer(final String uId, final String uName, final String uRef, final String uNote, final String uDate, final String uContact, final String ubudget, final String uArea) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.UPDATE_BUYER_PROPERTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("response", response);
                    finish();
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
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", uId);
                params.put("name", uName);
                params.put("refrence", uRef);
                params.put("note", uNote);
                params.put("date", uDate);
                params.put("contact", uContact);
                params.put("budject", ubudget);
                params.put("area", uArea);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
