package com.aktechzone.propertydeal.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.DrawerFragments.RecyclerProp.AgentMangerRecycler;
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

public class AllAgentActivity extends Activity {

    RecyclerView recycleAllAgent;
    List<Modelclass> listofAgent;
    ImageView backArrow;
    TextView changeHeading;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_manager_agent);
        init();
        progressDialog = MyProgressDialog.showDialog(this, "Loading . .");
        getRequestFromServer("agent");
        setListener();
    }


    private void setListener() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        backArrow = findViewById(R.id.backarrowToMain);
        changeHeading = findViewById(R.id.changeHeading);
        changeHeading.setText("Sale Executives");
        recycleAllAgent = findViewById(R.id.all_manger);
    }

    public void setRecycler(List<Modelclass> list, String flage) {

        AgentMangerRecycler allMangerRecycler = new AgentMangerRecycler(this, list, flage);
        recycleAllAgent.setLayoutManager(new LinearLayoutManager(this));
        recycleAllAgent.addItemDecoration(new DividerItemDecoration(recycleAllAgent.getContext(), DividerItemDecoration.VERTICAL));
        recycleAllAgent.setAdapter(allMangerRecycler);
    }

    public void getRequestFromServer(final String flagee) {
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrls.GET_ALL_AGENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    listofAgent = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.wtf("status", status);
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Modelclass modelclass = new Modelclass();
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            modelclass.setId(jsonObjectCat.getString("id"));
                            modelclass.setName(jsonObjectCat.getString("name"));
                            modelclass.setPhone(jsonObjectCat.getString("phone"));
                            modelclass.setPassword(jsonObjectCat.getString("password"));
                            modelclass.seteMail(jsonObjectCat.getString("email"));
                            listofAgent.add(modelclass);
                        }
                        setRecycler(listofAgent, flagee);

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
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
