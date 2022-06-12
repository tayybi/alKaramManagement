package com.aktechzone.propertydeal.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class EditSellerActivity extends AppCompatActivity {
    Button btUpdate;
    ImageView ivBackArrow;
    TextView titleforSeller;
    String idForEditSeller;
    EditText etCity,etName,etDate,etPhase,etBlock,etPlot,etDemand,etRefrence,etContact;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        init();
        setListener();
    }

    private void init() {

        btUpdate=findViewById(R.id.continueToUpdate);
        ivBackArrow=findViewById(R.id.back__arrow);
        titleforSeller=findViewById(R.id.tital_for_seller);
        titleforSeller.setText("Seller");

        etName=findViewById(R.id.nameS);
        etBlock=findViewById(R.id.blockS);
        etCity=findViewById(R.id.cityS);
        etDate=findViewById(R.id.dateS);
        etPhase=findViewById(R.id.phaseS);
        etPlot=findViewById(R.id.plotS);
        etRefrence=findViewById(R.id.refrenceS);
        etDemand=findViewById(R.id.demandS);
        etContact=findViewById(R.id.contactS);

        idForEditSeller=getIntent().getExtras().getString("id");
        String city=getIntent().getExtras().getString("city");
        String name=getIntent().getExtras().getString("name");
        String phase=getIntent().getExtras().getString("phase");
        String block=getIntent().getExtras().getString("block");
        String plot=getIntent().getExtras().getString("plot");
        String demand=getIntent().getExtras().getString("demand");
        String refrence=getIntent().getExtras().getString("refrence");
        String contact=getIntent().getExtras().getString("contact");
        String date=getIntent().getExtras().getString("date");

        etRefrence.setText(refrence);
        etDemand.setText(demand);
        etPlot.setText(plot);
        etDate.setText(date);
        etBlock.setText(block);
        etPhase.setText(phase);
        etCity.setText(city);
        etName.setText(name);
        etContact.setText(contact);
        btUpdate.setVisibility(View.VISIBLE);
    }

    private void setListener() {
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eref=etRefrence.getText().toString().trim();
                String edemand=etDemand.getText().toString().trim();
                String eplot=etPlot.getText().toString().trim();
                String edate=etDate.getText().toString().trim();
                String eblock=etBlock.getText().toString().trim();
                String ephase=etPhase.getText().toString().trim();
                String ecity=etCity.getText().toString().trim();
                String ename=etName.getText().toString().trim();
                String econtact=etContact.getText().toString().trim();
                if(eref.equals("")||edemand.equals("")||eplot.equals("")||edate.equals("")||eblock.equals("")||ephase.equals("")||ecity.equals("")||ename.equals("")) {
                    Toast.makeText(EditSellerActivity.this,"All field required",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog = MyProgressDialog.showDialog(EditSellerActivity.this,"Loading . .");
                    setDataToServer(idForEditSeller,eref,edemand,eplot,edate,eblock,ephase,ecity,ename,econtact);
                }


            }
        });
    }

    private void setDataToServer(final String uId, final String uRef ,final String uDemand , final String uPlot, final String uDate, final String uBlock, final String uPhase, final String uCity, final String uName, final String uContact) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.UPDATE_SELLER_PROPERTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("response",response);
                    finish();
                } catch (JSONException e) {
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
                params.put("refrence",uRef );
                params.put("date", uDate);
                params.put("contact", uContact);
                params.put("budject", uDemand);
                params.put("area", uCity);
                params.put("area", uPhase);
                params.put("area", uPlot);
                params.put("area", uBlock);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}