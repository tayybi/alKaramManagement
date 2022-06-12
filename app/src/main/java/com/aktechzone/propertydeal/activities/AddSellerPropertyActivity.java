package com.aktechzone.propertydeal.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.Modelclass;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddSellerPropertyActivity extends AppCompatActivity {
    ImageView btToBack;
    List<Modelclass> catogriList;
    Button btContinue;
    TextView titleforSeller;
    ProgressDialog progressDialog;
    List<String> catNames;
    EditText etCity, etDate, etPhase, etContact, etRefrence, etPlot, etBlock, etDemand, etName, etNote;
    String date, city, contact, phase, ref, demand, plot, block, name, note;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        init();
        setListene();
    }

    private void setListene() {
        btToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getApplicationContext()).getUserType().equals("admin")) {
                    startActivity(new Intent(AddSellerPropertyActivity.this, DrawerActivity.class));
                    finish();
                }
                if (SharedPrefManager.getInstance(getApplicationContext()).getUserType().equals("agent")) {
                    startActivity(new Intent(AddSellerPropertyActivity.this, DrawerAgentActivity.class));
                    finish();
                }
            }
        });
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city = etCity.getText().toString().trim();
                date = etDate.getText().toString().trim();
                phase = etPhase.getText().toString().trim();
                contact = etContact.getText().toString().trim();
                ref = etRefrence.getText().toString().trim();
                demand = etDemand.getText().toString().trim();
                block = etBlock.getText().toString().trim();
                plot = etPlot.getText().toString().trim();
                name = etName.getText().toString().trim();
                note = etNote.getText().toString().trim();

                if (city.equals("") || date.equals("") || contact.equals("") || demand.equals("") || ref.equals("") || phase.equals("") || plot.equals("") || block.equals("") || name.equals("") || note.equals("")) {
                    Toast.makeText(AddSellerPropertyActivity.this, "enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = MyProgressDialog.showDialog(AddSellerPropertyActivity.this, "Loading...");
                    getCatagoryList();
                }
            }
        });
    }

    public void init() {
        btToBack = findViewById(R.id.back__arrow);
        btContinue = findViewById(R.id.continue_to_seller);
        btContinue.setVisibility(View.VISIBLE);
        titleforSeller = findViewById(R.id.tital_for_seller);
        titleforSeller.setText("Seller");
        etCity = findViewById(R.id.cityS);
        etName = findViewById(R.id.nameS);
        etDate = findViewById(R.id.dateS);
        etDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        etPhase = findViewById(R.id.phaseS);
        etContact = findViewById(R.id.contactS);
        etRefrence = findViewById(R.id.refrenceS);
        etDemand = findViewById(R.id.demandS);
        etBlock = findViewById(R.id.blockS);
        etPlot = findViewById(R.id.plotS);
        etNote = findViewById(R.id.etNoteSeller);
    }

    public void showDialog() {
        catNames = new ArrayList<>();
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_catagories_list);
        for (int i = 0; i < catogriList.size(); i++) {
            catNames.add(catogriList.get(i).getName());
        }
        ListView listView = dialog.findViewById(R.id.catagori_list);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, catNames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.wtf("idPos", i + "");
                Log.wtf("idFree", catogriList.get(i).getId());
                String id = catogriList.get(i).getId();
                progressDialog = MyProgressDialog.showDialog(AddSellerPropertyActivity.this, "Loading . . .");
                sendDataToServer(id);
            }
        });
        dialog.show();
    }

    private void sendDataToServer(final String catId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.ADDSELLER_PROPERTY_URI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        dialog.dismiss();
                        dialog.cancel();
                        finish();
                    } else {
                        Toast.makeText(AddSellerPropertyActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddSellerPropertyActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                Log.wtf("catID", catId);
                params.put("seller_categorie_id", catId);
                params.put("city", city);
                params.put("phase", phase);
                params.put("block", block);
                params.put("plot", plot);
                params.put("demand", demand);
                params.put("refrence", ref);
                params.put("contact", contact);
                params.put("date", date);
                params.put("name", name);
                params.put("note", note);
                params.put("id", SharedPrefManager.getInstance(getApplicationContext()).getId());
                params.put("created_by", SharedPrefManager.getInstance(getApplicationContext()).getId());
                params.put("activity", SharedPrefManager.getInstance(getApplicationContext()).getUserType());
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void getCatagoryList() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.CATAGORI_LIST_SELLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    catogriList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            Modelclass modelclass = new Modelclass();
                            modelclass.setId(jsonObjectCat.getString("id"));
                            modelclass.setName(jsonObjectCat.getString("name"));
                            catogriList.add(modelclass);
                        }
                        Log.wtf("status", catogriList.size() + "");
                        if (catogriList.size() > 0)
                            showDialog();
                    } else {
                        Toast.makeText(AddSellerPropertyActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddSellerPropertyActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
