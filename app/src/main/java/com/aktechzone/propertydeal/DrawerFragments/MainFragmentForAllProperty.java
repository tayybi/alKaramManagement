package com.aktechzone.propertydeal.DrawerFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aktechzone.propertydeal.DrawerFragments.RecyclerProp.MainPropertyRecycler;
import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.MyUtils;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.aktechzone.propertydeal.activities.SearchFilterActivity;
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

public class MainFragmentForAllProperty extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    Button btBuyerProMain, btSellerPropMain, btnSelect, btnPropertyMultiDelete, btSearch;
    private String FLAG_TAB = "buyer";
    List<Modelclass> listBuyer, listSeller;
    private View mView;
    List<String> buyerMultiDeleteIDs, sellerMultiDeleteIDs;
    ProgressDialog progressDialog;
    DialogInterface.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        setListeners();
        progressDialog = MyProgressDialog.showDialog(getContext(), "Loading . .");
        if (MyUtils.isNetworkAvailable(getContext())) {
            getDataFromServerForBuyer("buyer", "single");
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return mView;
    }

    private void setListeners() {
        btnSelect.setOnClickListener(this);
        btSellerPropMain.setOnClickListener(this);
        btBuyerProMain.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btnPropertyMultiDelete.setOnClickListener(this);
    }

    private void init() {
        mRecyclerView = mView.findViewById(R.id.recyclerMainFragment);
        btnSelect = mView.findViewById(R.id.select_plus);
        btBuyerProMain = mView.findViewById(R.id.btnBuyerPropMain);
        btSellerPropMain = mView.findViewById(R.id.btnSellerPropMain);
        btSearch = mView.findViewById(R.id.btSearch);
        btnPropertyMultiDelete = mView.findViewById(R.id.btnPropertyMultiDelete);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_plus:
                if (btnSelect.getText().toString().equals("select+")) {
                    btnSelect.setText("UnSelect");
                    btnPropertyMultiDelete.setVisibility(View.VISIBLE);
                    if (FLAG_TAB.equals("buyer")) {
                        initRecycler("buyer", "multiple");
                    } else {
                        initRecycler("seller", "multiple");
                    }
                } else {
                    btnSelect.setText("select+");
                    btnPropertyMultiDelete.setVisibility(View.GONE);
                    if (FLAG_TAB.equals("buyer")) {
                        initRecycler("buyer", "single");
                    } else {
                        initRecycler("seller", "single");
                    }
                }
                break;
            case R.id.btnSellerPropMain:
                FLAG_TAB = "seller";
                btBuyerProMain.setBackground(getResources().getDrawable(R.drawable.border));
                btSellerPropMain.setBackground(getResources().getDrawable(R.drawable.buttonstraight));
                btBuyerProMain.setTextColor(getResources().getColor(R.color.colorPrimary));
                btSellerPropMain.setTextColor(getResources().getColor(R.color.colorAccent));
                btnPropertyMultiDelete.setVisibility(View.GONE);
                btnSelect.setText("select+");
                progressDialog = MyProgressDialog.showDialog(getContext(), "Loading . .");
                getDataFromServerForSeller("seller", "single");
                break;
            case R.id.btnBuyerPropMain:
                FLAG_TAB = "buyer";
                btBuyerProMain.setBackground(getResources().getDrawable(R.drawable.buttonstraight));
                btSellerPropMain.setBackground(getResources().getDrawable(R.drawable.border));
                btBuyerProMain.setTextColor(getResources().getColor(R.color.colorAccent));
                btSellerPropMain.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnPropertyMultiDelete.setVisibility(View.GONE);
                btnSelect.setText("select+");
                progressDialog = MyProgressDialog.showDialog(getContext(), "Loading . .");
                getDataFromServerForBuyer("buyer", "single");
                break;
            case R.id.btSearch:
                startActivity(new Intent(getActivity(), SearchFilterActivity.class));
                break;
            case R.id.btnPropertyMultiDelete:
                if (MyUtils.isNetworkAvailable(getContext())) {
                    onClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String allIDsBuyer = "";
                            if (FLAG_TAB.equals("buyer")) {
                                for (int i = 0; i < buyerMultiDeleteIDs.size(); i++) {
                                    if (i == buyerMultiDeleteIDs.size() - 1) {
                                        allIDsBuyer = allIDsBuyer + buyerMultiDeleteIDs.get(i);
                                    } else {
                                        allIDsBuyer = allIDsBuyer + buyerMultiDeleteIDs.get(i) + ",";
                                    }
                                }
                                Log.wtf("multiDelBuyer", allIDsBuyer);
                                progressDialog = MyProgressDialog.showDialog(getContext(), "Loading . .");
                                deleteMultiRecords(allIDsBuyer, ServiceUrls.DELETE_BUYER_MULTIPLE_PROPERTY, FLAG_TAB);
                            } else {
                                String allIDsSeller = "";
                                for (int i = 0; i < sellerMultiDeleteIDs.size(); i++) {
                                    if (i == sellerMultiDeleteIDs.size() - 1) {
                                        allIDsSeller = allIDsSeller + sellerMultiDeleteIDs.get(i);
                                    } else {
                                        allIDsSeller = allIDsSeller + sellerMultiDeleteIDs.get(i) + ",";
                                    }
                                }
                                Log.wtf("multiDelSeller", allIDsSeller);
                                progressDialog = MyProgressDialog.showDialog(getContext(), "Loading . .");
                                deleteMultiRecords(allIDsSeller, ServiceUrls.DELETE_SELLER_MULIPLE_PROPERTY, FLAG_TAB);
                            }
                        }
                    };
                    MyUtils.showAlertDialog(getContext(), onClickListener);
                } else {
                    MyUtils.showSnackBar(getContext());
                }
                break;
        }
    }

    public void deleteMultiRecords(final String ids, String url, final String typeTabs) {
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
                        if (typeTabs.equals("buyer")) {
                            for (int i = 0; i < buyerMultiDeleteIDs.size(); i++) {
                                for (int j = 0; j < listBuyer.size(); j++) {
                                    if (buyerMultiDeleteIDs.get(i).equals(listBuyer.get(j).getId())) {
                                        listBuyer.remove(j);
                                    }
                                }
                            }
                            initRecycler(FLAG_TAB, "single");
                        } else {
                            for (int i = 0; i < sellerMultiDeleteIDs.size(); i++) {
                                for (int j = 0; j < listSeller.size(); j++) {
                                    if (sellerMultiDeleteIDs.get(i).equals(listSeller.get(j).getId())) {
                                        listSeller.remove(j);
                                    }
                                }
                            }
                            initRecycler(FLAG_TAB, "single");
                        }
                        btnPropertyMultiDelete.setVisibility(View.GONE);
                        btnSelect.setText("select+");
                        Toast.makeText(getActivity(), "items deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error " + error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ids", ids);
                return params;
            }
        };
        SingletonVolley.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void initRecycler(String typeflag, String layoutFlag) {
        buyerMultiDeleteIDs = new ArrayList<>();
        sellerMultiDeleteIDs = new ArrayList<>();
        MainPropertyRecycler adapter;
        if (typeflag.equals("buyer")) {
            Log.wtf("typeflag", typeflag);
            adapter = new MainPropertyRecycler(listBuyer, getActivity(), layoutFlag, typeflag, new MainPropertyRecycler.OnItemCheckListener() {
                @Override
                public void onItemCheck(Modelclass item) {
                    buyerMultiDeleteIDs.add(item.getId());
                    Log.wtf("checkedBuyer", "true");
                    Log.wtf("checkedBuyerList1", buyerMultiDeleteIDs.size() + "");
                }

                @Override
                public void onItemUncheck(Modelclass item) {
                    int id = buyerMultiDeleteIDs.indexOf(item.getId());
                    buyerMultiDeleteIDs.remove(id);
                    Log.wtf("checkedBuyer", "false");
                    Log.wtf("checkedBuyerList2", buyerMultiDeleteIDs.size() + "");
                }
            });
        } else {
            Log.wtf("typeflags", typeflag);
            adapter = new MainPropertyRecycler(listSeller, getActivity(), layoutFlag, typeflag, new MainPropertyRecycler.OnItemCheckListener() {
                @Override
                public void onItemCheck(Modelclass item) {
                    sellerMultiDeleteIDs.add(item.getId());
                }

                @Override
                public void onItemUncheck(Modelclass item) {
                    sellerMultiDeleteIDs.remove(item.getId());
                }
            });
        }
        adapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
    }

    public void getDataFromServerForBuyer(final String typeFlag, final String layoutFlag) {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.GET_BUYER_PROP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    listBuyer = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Modelclass modelclass = new Modelclass();
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            modelclass.setId(jsonObjectCat.getString("id"));
                            modelclass.setIdCatagory(jsonObjectCat.getString("buyer_categorie_id"));
                            modelclass.setName(jsonObjectCat.getString("name"));
                            modelclass.setContact(jsonObjectCat.getString("contact"));
                            modelclass.setBudjet(jsonObjectCat.getString("budject"));
                            modelclass.setDate(jsonObjectCat.getString("date"));
                            modelclass.setRefrence(jsonObjectCat.getString("refrence"));
                            modelclass.setArea(jsonObjectCat.getString("area"));
                            modelclass.setNote(jsonObjectCat.getString("note"));
                            modelclass.setCreatedBy(jsonObjectCat.getString("activity"));
                            JSONObject jsonObjectCatName = jsonObjectCat.getJSONObject("buyer_categories");
                            String catName = jsonObjectCatName.getString("name");
                            modelclass.setCatName(catName);
                            listBuyer.add(modelclass);
                        }
                        initRecycler(typeFlag, layoutFlag);

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    public void getDataFromServerForSeller(final String typeFlag, final String layoutFlag) {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrls.GET_SELLER_PROP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    listSeller = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Modelclass modelclass = new Modelclass();
                            JSONObject jsonObjectCat = jsonArray.getJSONObject(i);
                            modelclass.setId(jsonObjectCat.getString("id"));
                            modelclass.setIdCatagory(jsonObjectCat.optString("buyer_categorie_id"));
                            modelclass.setCity(jsonObjectCat.getString("city"));
                            modelclass.setName(jsonObjectCat.getString("name"));
                            modelclass.setPhase(jsonObjectCat.getString("phase"));
                            modelclass.setBlock(jsonObjectCat.getString("block"));
                            modelclass.setPlot(jsonObjectCat.getString("plot"));
                            modelclass.setDemand(jsonObjectCat.getString("demand"));
                            modelclass.setRefrence(jsonObjectCat.getString("refrence"));
                            modelclass.setContact(jsonObjectCat.getString("contact"));
                            modelclass.setDate(jsonObjectCat.getString("date"));
                            modelclass.setCreatedBy(jsonObjectCat.getString("activity"));
                            modelclass.setNote(jsonObjectCat.getString("note"));
                            JSONObject jsonObjectCatName = jsonObjectCat.getJSONObject("seller_categories");
                            String catName = jsonObjectCatName.getString("name");
                            modelclass.setCatName(catName);
                            listSeller.add(modelclass);
                        }
                        initRecycler(typeFlag, layoutFlag);
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        SingletonVolley.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }
}
