package com.aktechzone.propertydeal.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aktechzone.propertydeal.Authentications.ChangePassword;
import com.aktechzone.propertydeal.Authentications.SignIn;
import com.aktechzone.propertydeal.DrawerFragments.MainFragmentForAllProperty;
import com.aktechzone.propertydeal.DrawerFragments.MainFragmentForAllPropertyAgent;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.SharedPrefManager;

public class DrawerAgentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_agent);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment allManger = new MainFragmentForAllPropertyAgent();
        setFragment(allManger);
    }

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
        getMenuInflater().inflate(R.menu.drawer_agent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        if (id == R.id.main_homeag) {
            Fragment allManger = new MainFragmentForAllProperty();
            setFragment(allManger);
        }
        if (id == R.id.changePasswordAgent) {
            startActivity(new Intent(DrawerAgentActivity.this, ChangePassword.class));
        } else if (id == R.id.add_propertyag) {
            Button btAddSelle, btAddBuyer;
            LayoutInflater layoutInflater = getLayoutInflater();
            Dialog dialog = new Dialog(this);
            View view = layoutInflater.inflate(R.layout.custom_add_property_type, null);
            btAddBuyer = view.findViewById(R.id.add_byuer);
            btAddSelle = view.findViewById(R.id.add_seller);

            btAddBuyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent44 = new Intent(DrawerAgentActivity.this, AddBuyerPropertyActivity.class);
                    startActivity(intent44);
                }
            });
            btAddSelle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent5 = new Intent(DrawerAgentActivity.this, AddSellerPropertyActivity.class);
                    startActivity(intent5);
                }
            });
            dialog.setContentView(view);
            dialog.show();
        } else if (id == R.id.logoutag) {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(DrawerAgentActivity.this, SignIn.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baseLayout, fragment);
        transaction.commit();
    }
}
