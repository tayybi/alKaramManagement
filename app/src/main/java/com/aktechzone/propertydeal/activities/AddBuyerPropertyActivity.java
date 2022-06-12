package com.aktechzone.propertydeal.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class AddBuyerPropertyActivity extends AppCompatActivity {
    ImageView btToBack;
    EditText etName, etDate, etBudjet, etContact, etRefrence, etArea, etNote;
    Button btContinueu;
    ProgressDialog progressDialog;
    TextView tvTitle;
    List<String> catName;
    String date, name, contact, budjet, refrence, area, note;
    Dialog dialog;
    private List<Modelclass> catogriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        init();
        setListener();
    }

    private void setListener() {
        btToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btContinueu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = etDate.getText().toString().trim();
                name = etName.getText().toString().trim();
                contact = etContact.getText().toString().trim();
                budjet = etBudjet.getText().toString().trim();
                refrence = etRefrence.getText().toString().trim();
                area = etArea.getText().toString().trim();
                note = etNote.getText().toString().trim();
                if (date.equals("") || name.equals("") || contact.equals("") || budjet.equals("") || refrence.equals("") || area.equals("") || note.equals("")) {
                    Toast.makeText(AddBuyerPropertyActivity.this, "enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = MyProgressDialog.showDialog(AddBuyerPropertyActivity.this, "Loading . . .");
                    getCatagoryList();
                }
            }
        });
    }

    private void sendDataToServer(final String catId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.ADDBUYER_PROPERTY_URI, new Response.Listener<String>() {
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
                        Toast.makeText(AddBuyerPropertyActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddBuyerPropertyActivity.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error " + error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                Log.wtf("catID", catId);
                params.put("buyer_categorie_id", catId);
                params.put("date", date);
                params.put("name", name);
                params.put("contact", contact);
                params.put("budject", budjet);
                params.put("refrence", refrence);
                params.put("area", area);
                params.put("note", note);
                params.put("id", SharedPrefManager.getInstance(getApplicationContext()).getId());
                params.put("activity", SharedPrefManager.getInstance(getApplicationContext()).getUserType());
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void init() {
        btToBack = findViewById(R.id.back__arrow);
        etArea = findViewById(R.id.areaB);
        etBudjet = findViewById(R.id.budjetB);
        etDate = findViewById(R.id.dateB);
        etDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        etContact = findViewById(R.id.contactB);
        etName = findViewById(R.id.nameB);
        etNote = findViewById(R.id.noteB);
        etRefrence = findViewById(R.id.refrenceB);
        btContinueu = findViewById(R.id.continue_to);
        btContinueu.setVisibility(View.VISIBLE);
        tvTitle = findViewById(R.id.heading_of_buyer);
        tvTitle.setText("Buyer");
    }

    public void showDialog() {
        catName = new ArrayList<>();
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_catagories_list);
        ListView listView = dialog.findViewById(R.id.catagori_list);

        for (int i = 0; i < catogriList.size(); i++) {
            catName.add(catogriList.get(i).getName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, catName);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = catogriList.get(i).getId();
                Log.wtf("id", id + "");
                Log.wtf("idPos", i + "");
                progressDialog = MyProgressDialog.showDialog(AddBuyerPropertyActivity.this, "Loading. . .");
                sendDataToServer(id);
            }
        });
        dialog.show();
    }

    public void getCatagoryList() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.CATAGORI_LIST_BUYER, new Response.Listener<String>() {
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
                        if (catogriList.size() > 0)
                            showDialog();
                    } else {
                        Toast.makeText(AddBuyerPropertyActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddBuyerPropertyActivity.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
