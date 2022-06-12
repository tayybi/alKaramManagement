package com.aktechzone.propertydeal.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btsearchFilter;
    private ImageView ivbackarrow;
    private RadioGroup radioGroup, radioGroup2;
    private RadioButton rbBuyer, rbSeller;
    private Spinner spinnerCat;
    private EditText etMinPrice, etMaxPrice;
    ProgressDialog progressDialog;
    List<Integer> catIdBuyer, catIDSeller;
    private LinearLayout linearLayoutCat;
    public static List<Modelclass> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        init();
        setListener();
    }

    private void init() {
        btsearchFilter = findViewById(R.id.btsearch_filter);
        ivbackarrow = findViewById(R.id.back__arrow);
        rbBuyer = findViewById(R.id.rbBuyerSearch);
        rbSeller = findViewById(R.id.rbSellerSearch);
        spinnerCat = findViewById(R.id.spinnerCatSearch);
        etMaxPrice = findViewById(R.id.etPriceMaxSearch);
        etMinPrice = findViewById(R.id.etPriceMinSearch);
        radioGroup = findViewById(R.id.rgSearch);
        radioGroup2 = findViewById(R.id.rgSecondSearch);
        linearLayoutCat = findViewById(R.id.linearLayoutCatSearch);
        ivbackarrow = findViewById(R.id.back__arrow);
    }

    private void setListener() {
        rbBuyer.setOnClickListener(this);
        rbSeller.setOnClickListener(this);
        btsearchFilter.setOnClickListener(this);
        ivbackarrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btsearch_filter:
                String selectedTypeID = "";
                int id = radioGroup.getCheckedRadioButtonId();
                if (id != -1) {
                    RadioButton radioButton = findViewById(id);
                    String selectedType = radioButton.getText().toString();
                    if (selectedType.equalsIgnoreCase("Buyer")) {
                        selectedTypeID = "1";
                    } else {
                        selectedTypeID = "0";
                    }
                    Log.wtf("selectedTypeID", selectedTypeID);
                }
                String selectedCat = "";
                if (spinnerCat.getSelectedItem() != null) {
                    int selectedCatPos = spinnerCat.getSelectedItemPosition();
                    if (selectedCatPos == 0) {
                        selectedCat = "";
                    } else {
                        if (selectedTypeID.equalsIgnoreCase("1")) {
                            selectedCat = catIdBuyer.get(selectedCatPos - 1).toString();
                        } else {
                            selectedCat = catIDSeller.get(selectedCatPos - 1).toString();
                        }
                    }
                    Log.wtf("selectedCatPos", selectedCatPos + "");
                    Log.wtf("selectedCat", selectedCat + "");

                }
                String minPrice = "";
                if (!etMinPrice.getText().toString().equals("")) {
                    minPrice = etMinPrice.getText().toString();
                } else {
                    minPrice = "1";
                }
                String maxPrice = "";
                if (!etMaxPrice.getText().toString().equals("")) {
                    maxPrice = etMaxPrice.getText().toString();
                } else {
                    maxPrice = "999999999";
                }
                String checkedPerson = "";
                int rgId = radioGroup2.getCheckedRadioButtonId();
                if (rgId != -1) {
                    RadioButton radioButton = findViewById(rgId);
                    if (radioButton.getText().toString().equalsIgnoreCase("Admin")) {
                        checkedPerson = "admin";
                    } else if (radioButton.getText().toString().equalsIgnoreCase("Manager")) {
                        checkedPerson = "manager";
                    } else {
                        checkedPerson = "agent";
                    }
                    Log.wtf("checkedPerson", checkedPerson);
                }
                progressDialog = MyProgressDialog.showDialog(this, "Loading . .");
                sendDataToServer(selectedTypeID, selectedCat, minPrice, maxPrice, checkedPerson);
                break;
            case R.id.rbBuyerSearch:
                progressDialog = MyProgressDialog.showDialog(this, "Loading . .");
                getBuyerCatagoryList();
                break;
            case R.id.rbSellerSearch:
                progressDialog = MyProgressDialog.showDialog(this, "Loading . .");
                getCategoryList();
                break;
            case R.id.spinnerCatSearch:
                if (spinnerCat != null & spinnerCat.getSelectedItem() != null) {
                } else {
                    Toast.makeText(SearchFilterActivity.this, "select type first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back__arrow:
                finish();
                break;
        }
    }

    public void getBuyerCatagoryList() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.CATAGORI_LIST_BUYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    List<String> catogriListBuyer = new ArrayList<>();
                    catogriListBuyer.add("Select Category");
                    catIdBuyer = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            catogriListBuyer.add(jsonObjectCat.getString("name"));
                            catIdBuyer.add(Integer.parseInt(jsonObjectCat.getString("id")));
                        }
                        if (catogriListBuyer.size() > 0) {
                            ArrayAdapter adapter = new ArrayAdapter(SearchFilterActivity.this, android.R.layout.simple_list_item_1, catogriListBuyer);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCat.setAdapter(adapter);
                            linearLayoutCat.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(SearchFilterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SearchFilterActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    public void getCategoryList() {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.CATAGORI_LIST_SELLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    List<String> categoryListSeller = new ArrayList<>();
                    categoryListSeller.add("Select category");
                    catIDSeller = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            categoryListSeller.add(jsonObjectCat.getString("name"));
                            catIDSeller.add(Integer.parseInt(jsonObjectCat.getString("id")));
                        }
                        Log.wtf("status", categoryListSeller.size() + "");
                        if (categoryListSeller.size() > 0) {
                            ArrayAdapter adapter = new ArrayAdapter(SearchFilterActivity.this, android.R.layout.simple_list_item_1, categoryListSeller);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCat.setAdapter(adapter);
                            linearLayoutCat.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(SearchFilterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SearchFilterActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void sendDataToServer(final String typeId, final String catID, final String minPrice, final String maxPrice, final String checkedPerson) {
        StringRequest
                stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.FILTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    listData = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("response", response);
                    if (status.equals("true")) {
                        JSONArray jsonArrayOuter = jsonObject.getJSONArray("data");
                        if (typeId.equals("0")) {
                            for (int i = 0; i < jsonArrayOuter.length(); i++) {
                                JSONObject jsonObjectOfOuter = jsonArrayOuter.getJSONObject(i);
                                Modelclass modelclass = new Modelclass();
                                modelclass.setId(jsonObjectOfOuter.getString("id"));
                                modelclass.setIdCatagory(jsonObjectOfOuter.optString("buyer_categorie_id"));
                                modelclass.setCity(jsonObjectOfOuter.getString("city"));
                                modelclass.setName(jsonObjectOfOuter.getString("name"));
                                modelclass.setPhase(jsonObjectOfOuter.getString("phase"));
                                modelclass.setBlock(jsonObjectOfOuter.getString("block"));
                                modelclass.setPlot(jsonObjectOfOuter.getString("plot"));
                                modelclass.setDemand(jsonObjectOfOuter.getString("demand"));
                                modelclass.setRefrence(jsonObjectOfOuter.getString("refrence"));
                                modelclass.setContact(jsonObjectOfOuter.getString("contact"));
                                modelclass.setDate(jsonObjectOfOuter.getString("date"));
                                modelclass.setCreatedBy(jsonObjectOfOuter.getString("activity"));
                                modelclass.setNote(jsonObjectOfOuter.getString("note"));
                                JSONObject jsonObjectCat = jsonObjectOfOuter.getJSONObject("seller_categories");
                                modelclass.setCatName(jsonObjectCat.getString("name"));
                                SearchFilterActivity.listData.add(modelclass);
                            }
                            Intent intent = new Intent(SearchFilterActivity.this, ShowSearchData.class);
                            intent.putExtra("propType", "seller");
                            intent.putExtra("layoutType", "single");
                            startActivity(intent);
                        } else {
                            for (int i = 0; i < jsonArrayOuter.length(); i++) {
                                JSONObject jsonObjectOfOuter = jsonArrayOuter.getJSONObject(i);
                                Modelclass modelclass = new Modelclass();
                                modelclass.setId(jsonObjectOfOuter.getString("id"));
                                modelclass.setIdCatagory(jsonObjectOfOuter.getString("buyer_categorie_id"));
                                modelclass.setName(jsonObjectOfOuter.getString("name"));
                                modelclass.setContact(jsonObjectOfOuter.getString("contact"));
                                modelclass.setBudjet(jsonObjectOfOuter.getString("budject"));
                                modelclass.setDate(jsonObjectOfOuter.getString("date"));
                                modelclass.setRefrence(jsonObjectOfOuter.getString("refrence"));
                                modelclass.setArea(jsonObjectOfOuter.getString("area"));
                                modelclass.setNote(jsonObjectOfOuter.getString("note"));
                                modelclass.setCreatedBy(jsonObjectOfOuter.getString("activity"));
                                JSONObject jsonObjectCat = jsonObjectOfOuter.getJSONObject("buyer_categories");
                                modelclass.setCatName(jsonObjectCat.getString("name"));
                                SearchFilterActivity.listData.add(modelclass);
                            }
                            Intent intent = new Intent(SearchFilterActivity.this, ShowSearchData.class);
                            intent.putExtra("propType", "buyer");
                            intent.putExtra("layoutType", "single");
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(SearchFilterActivity.this, message, Toast.LENGTH_SHORT).show();
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
//              Toast.makeText(SignUp.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", catID);
                params.put("type", typeId);
                params.put("min_price", minPrice);
                params.put("max_price", maxPrice);
                params.put("activity", checkedPerson);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
