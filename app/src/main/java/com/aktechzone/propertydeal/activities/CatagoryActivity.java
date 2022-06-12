package com.aktechzone.propertydeal.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CatagoryActivity extends Activity {
    Button btCancel, btnOk;
    ImageView ivGoBack;
    RadioGroup radioGroup;
    EditText editTextCat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_add_new_catagories);
        init();
        setListener();
    }

    private void setListener() {
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = editTextCat.getText().toString().trim();
                int id = radioGroup.getCheckedRadioButtonId();
                if (id != -1) {
                    RadioButton radioButton = findViewById(id);
                    String type = radioButton.getText().toString();
                    String url = null;
                    if (type.equals("Buyer")) {
                        url = ServiceUrls.ADD_BUYER_CAT;
                    } else if (type.equals("Seller")) {
                        url = ServiceUrls.ADD_SELLER_CAT;
                    }
                    progressDialog = MyProgressDialog.showDialog(CatagoryActivity.this,"Loading . .");
                    sendDataToServer(url, catName);
                } else {
                    Toast.makeText(getApplicationContext(), "select radiobutton type", Toast.LENGTH_SHORT).show();
                }
                if (catName.equals("")) {
                    Toast.makeText(getApplicationContext(), "enter category name", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

    private void sendDataToServer(String url, final String catName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", catName);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void init() {
        radioGroup = findViewById(R.id.rgPersonTypeAddCat);
        btCancel = findViewById(R.id.btnAddCatCancel);
        ivGoBack = findViewById(R.id.go_back);
        editTextCat = findViewById(R.id.etAddCat);
        btnOk = findViewById(R.id.btnAddCatOk);
    }
}
