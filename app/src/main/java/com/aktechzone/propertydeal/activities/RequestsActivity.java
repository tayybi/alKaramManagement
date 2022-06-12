package com.aktechzone.propertydeal.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aktechzone.propertydeal.DrawerFragments.RecyclerProp.NewRequestRecycler;
import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {

    RecyclerView recyclerOfRequest;
    Button mangerRequest, agentRequest;
    List<Modelclass> listOfRequests;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requests);
        init();
        progressDialog = MyProgressDialog.showDialog(this,"Loading . .");
        getRequestFromServer(ServiceUrls.GET_MANAGER_REQUEST,"manager");
        setListener();
    }

    public void init() {
        mangerRequest = findViewById(R.id.btnMangerRequests);
        agentRequest = findViewById(R.id.btnAgentRequests);
        recyclerOfRequest = findViewById(R.id.recyclerForAllRequests);
    }

    public void initRecycler(List<Modelclass> list,String flagetype) {
        NewRequestRecycler adapter;
        adapter = new NewRequestRecycler(list, this,flagetype);
        adapter.notifyDataSetChanged();
        recyclerOfRequest.setHasFixedSize(true);
        recyclerOfRequest.setLayoutManager(new LinearLayoutManager(this));
        recyclerOfRequest.addItemDecoration(new DividerItemDecoration(recyclerOfRequest.getContext(), DividerItemDecoration.VERTICAL));
        recyclerOfRequest.setAdapter(adapter);
    }

    public void setListener() {
        mangerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mangerRequest.setBackground(getResources().getDrawable(R.drawable.buttonstraight));
                agentRequest.setBackground(getResources().getDrawable(R.drawable.border));
                mangerRequest.setTextColor(getResources().getColor(R.color.colorAccent));
                agentRequest.setTextColor(getResources().getColor(R.color.colorPrimary));
                progressDialog = MyProgressDialog.showDialog(RequestsActivity.this,"Loading . .");
                getRequestFromServer(ServiceUrls.GET_MANAGER_REQUEST,"manager");
            }
        });
        agentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mangerRequest.setBackground(getResources().getDrawable(R.drawable.border));
                agentRequest.setBackground(getResources().getDrawable(R.drawable.buttonstraight));
                mangerRequest.setTextColor(getResources().getColor(R.color.colorPrimary));
                agentRequest.setTextColor(getResources().getColor(R.color.colorAccent));
                progressDialog = MyProgressDialog.showDialog(RequestsActivity.this,"Loading . .");
                getRequestFromServer(ServiceUrls.GET_AGENT_REQUEST,"agent");
            }
        });
    }

    public void getRequestFromServer(String uri, final String flagetypee) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    listOfRequests = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("statusRequests", status);
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Modelclass modelclass = new Modelclass();
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            modelclass.setId(jsonObjectCat.getString("id"));
//                           modelclass.setIdCatagory(jsonObjectCat.getString("buyer_categorie_id"));
                            modelclass.setName(jsonObjectCat.getString("name"));
                            modelclass.setPhone(jsonObjectCat.getString("phone"));
                            modelclass.setPassword(jsonObjectCat.getString("password"));
                            modelclass.seteMail(jsonObjectCat.getString("email"));
                            listOfRequests.add(modelclass);
                        }
                        Log.wtf("statusRequests", listOfRequests.size()+"");
                        initRecycler(listOfRequests,flagetypee);

                    } else {
                        Toast.makeText(RequestsActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RequestsActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }
}
