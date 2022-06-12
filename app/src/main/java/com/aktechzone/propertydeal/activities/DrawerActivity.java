package com.aktechzone.propertydeal.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.Authentications.ChangePassword;
import com.aktechzone.propertydeal.Authentications.SignIn;
import com.aktechzone.propertydeal.DrawerFragments.MainFragmentForAllProperty;
import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.MyUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Modelclass> catogriList;
    ListView allCatList;
    List<String> catName;
    ArrayAdapter arrayAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_admin);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /////////////////

        Fragment allManger = new MainFragmentForAllProperty();
        setFragment(allManger);
    }//oncreate

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.main_home) {
            Fragment allManger = new MainFragmentForAllProperty();
            setFragment(allManger);
        } else if (id == R.id.all_manger) {
            startActivity(new Intent(DrawerActivity.this, AllMangerActivity.class));
        } else if (id == R.id.add_employee) {
            startActivity(new Intent(DrawerActivity.this, AddEmployeeActivity.class));
        } else if (id == R.id.all_agent) {
            startActivity(new Intent(DrawerActivity.this, AllAgentActivity.class));
        } else if (id == R.id.search_filter) {
            startActivity(new Intent(DrawerActivity.this, SearchFilterActivity.class));
        }else if (id == R.id.changePasswordAdmin) {
            startActivity(new Intent(DrawerActivity.this, ChangePassword.class));
        } else if (id == R.id.add_property) {
            Button btAddSelle, btAddBuyer;
            LayoutInflater layoutInflater = getLayoutInflater();
            Dialog dialog = new Dialog(this);
            View view = layoutInflater.inflate(R.layout.custom_add_property_type, null);
            btAddBuyer = view.findViewById(R.id.add_byuer);
            btAddSelle = view.findViewById(R.id.add_seller);
            btAddBuyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DrawerActivity.this, AddBuyerPropertyActivity.class));
                }
            });
            btAddSelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DrawerActivity.this, AddSellerPropertyActivity.class));
                }
            });
            dialog.setContentView(view);
            dialog.show();

        } else if (id == R.id.all_catagory) {
            TextView heading;
            Button btAddSelle, btAddBuyer;
            LayoutInflater layoutInflater = getLayoutInflater();
            Dialog dialog = new Dialog(this);
            View view = layoutInflater.inflate(R.layout.custom_add_property_type, null);
            heading = view.findViewById(R.id.tvforheading);
            allCatList = view.findViewById(R.id.listOfAllCatagories);
            allCatList.setVisibility(View.VISIBLE);
            heading.setText("Select Catagory For Delete");
            btAddBuyer = view.findViewById(R.id.add_byuer);
            btAddSelle = view.findViewById(R.id.add_seller);
            btAddBuyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = MyProgressDialog.showDialog(DrawerActivity.this, "Loading . .");
                    getCatagoryList(ServiceUrls.CATAGORI_LIST_BUYER);
                    allCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final String id = catogriList.get(i).getId();


                            if (MyUtils.isNetworkAvailable(DrawerActivity.this)) {
                                DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog = MyProgressDialog.showDialog(DrawerActivity.this, "Loading . .");
                                        dellCatagory(ServiceUrls.DELETE_CAT_BUYER, id, i);
                                    }
                                };
                                MyUtils.showAlertDialog(DrawerActivity.this, dialogInterface);
                            } else {
                                MyUtils.showSnackBar(DrawerActivity.this);
                            }

                        }
                    });

                }
            });
            btAddSelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = MyProgressDialog.showDialog(DrawerActivity.this, "Loading . .");
                    getCatagoryList(ServiceUrls.CATAGORI_LIST_SELLER);
                    allCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final String id = catogriList.get(i).getId();
                            if (MyUtils.isNetworkAvailable(DrawerActivity.this)) {
                                DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog = MyProgressDialog.showDialog(DrawerActivity.this, "Loading...");
                                        dellCatagory(ServiceUrls.DELETE_CAT_SELLER, id, i);
                                    }
                                };
                                MyUtils.showAlertDialog(DrawerActivity.this, dialogInterface);
                            } else {
                                MyUtils.showSnackBar(DrawerActivity.this);
                            }

                        }
                    });
                }
            });
            dialog.setContentView(view);
            dialog.show();

        } else if (id == R.id.add_catagory) {
            startActivity(new Intent(DrawerActivity.this, CatagoryActivity.class));
        } else if (id == R.id.new_requests) {
            startActivity(new Intent(DrawerActivity.this, RequestsActivity.class));
        } else if (id == R.id.logout) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(DrawerActivity.this, SignIn.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dellCatagory(String deleteCatBuyer, final String catId, final int i) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteCatBuyer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        catName.remove(i);
                        arrayAdapter = new ArrayAdapter(DrawerActivity.this, android.R.layout.simple_list_item_1, catName);
                        allCatList.setAdapter(arrayAdapter);
                        Toast.makeText(DrawerActivity.this, "Category Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DrawerActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DrawerActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", catId);
                return params;
            }
        };
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getCatagoryList(String uri) {
        StringRequest request = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
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
                        catName = new ArrayList<>();
                        for (int i = 0; i < catogriList.size(); i++) {
                            catName.add(catogriList.get(i).getName());
                        }
                        arrayAdapter = new ArrayAdapter(DrawerActivity.this, android.R.layout.simple_list_item_1, catName);
                        arrayAdapter.notifyDataSetChanged();
                        allCatList.setAdapter(arrayAdapter);
                    } else {
                        Toast.makeText(DrawerActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DrawerActivity.this, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baseLayout, fragment);
        transaction.commit();
    }
}
