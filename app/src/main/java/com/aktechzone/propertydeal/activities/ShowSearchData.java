package com.aktechzone.propertydeal.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aktechzone.propertydeal.DrawerFragments.RecyclerProp.MainPropertyRecycler;
import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.R;

import java.util.ArrayList;
import java.util.List;

public class ShowSearchData extends Activity {
    RecyclerView recyclerView;
    List<String> buyerMultiDeleteIDs, sellerMultiDeleteIDs;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search_data);
        recyclerView = findViewById(R.id.recyclerViewShowSearchData);
        initRecycler(getIntent().getExtras().getString("propType"), getIntent().getExtras().getString("layoutType"));
    }

    public void initRecycler(String typeflag, String layoutFlag) {
        buyerMultiDeleteIDs = new ArrayList<>();
        sellerMultiDeleteIDs = new ArrayList<>();
        MainPropertyRecycler adapter;
        if (typeflag.equals("buyer")) {
            Log.wtf("typeflag", typeflag);
            adapter = new MainPropertyRecycler(SearchFilterActivity.listData, ShowSearchData.this, layoutFlag, typeflag, new MainPropertyRecycler.OnItemCheckListener() {
                @Override
                public void onItemCheck(Modelclass item) {
                    buyerMultiDeleteIDs.add(item.getId());
                }

                @Override
                public void onItemUncheck(Modelclass item) {
                    buyerMultiDeleteIDs.remove(item.getId());
                }
            });
        } else {
            Log.wtf("typeflags", typeflag);
            adapter = new MainPropertyRecycler(SearchFilterActivity.listData, ShowSearchData.this, layoutFlag, typeflag, new MainPropertyRecycler.OnItemCheckListener() {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowSearchData.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
