package com.demo.winery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.winery.R;
import com.demo.winery.event.Claim_event;
import com.demo.winery.fragment.AdsFragment;
import com.demo.winery.fragment.EventFragment;
import com.demo.winery.fragment.FestivalFragment;
import com.demo.winery.fragment.TourFragment;
import com.demo.winery.fragment.WineryFragment;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_Before_My;
import com.demo.winery.setting.activity_Setting;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_Main extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Fragment fragment;
    FloatingActionButton fab_My, fab_Festival, fab_Tour, fab_Event, fab_Winery, fab_ADs;
    ResponseModel responseModel;
    Intent Askpay_intent;
    TextView tv_close, tv_setting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_main);

        fab_My = findViewById(R.id.fab_My);
        fab_My.setOnClickListener(this);
        fab_Festival = findViewById(R.id.fab_Festival);
        fab_Festival.setOnClickListener(this);
        fab_Tour = findViewById(R.id.fab_Tour);
        fab_Tour.setOnClickListener(this);
        fab_Event = findViewById(R.id.fab_Event);
        fab_Event.setOnClickListener(this);
        fab_Winery = findViewById(R.id.fab_Winery);
        fab_Winery.setOnClickListener(this);
        fab_ADs = findViewById(R.id.fab_ADs);
        fab_ADs.setOnClickListener(this);

        fab_Winery.setVisibility(View.GONE);
        fab_Event.setVisibility(View.GONE);
        fab_Tour.setVisibility(View.GONE);
        fab_Festival.setVisibility(View.GONE);
        fab_ADs.setVisibility(View.GONE);

        tv_close = findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);
        tv_setting = findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }
    public void onBackPressed() {

    }
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragment = null;
        switch (item.getItemId()){
            case R.id.navigation_winery:
                fragment = new WineryFragment();
                fab_Winery.setVisibility(View.VISIBLE);
                fab_Event.setVisibility(View.GONE);
                fab_Tour.setVisibility(View.GONE);
                fab_Festival.setVisibility(View.GONE);
                fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_event:
                fragment = new EventFragment();
                fab_Winery.setVisibility(View.GONE);
                fab_Event.setVisibility(View.VISIBLE);
                fab_Tour.setVisibility(View.GONE);
                fab_Festival.setVisibility(View.GONE);
                fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_tour:
                fragment = new TourFragment();
                fab_Winery.setVisibility(View.GONE);
                fab_Event.setVisibility(View.GONE);
                fab_Tour.setVisibility(View.VISIBLE);
                fab_Festival.setVisibility(View.GONE);
                fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_festival:
                fragment = new FestivalFragment();
                fab_Winery.setVisibility(View.GONE);
                fab_Event.setVisibility(View.GONE);
                fab_Tour.setVisibility(View.GONE);
                fab_ADs.setVisibility(View.GONE);
                fab_Festival.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_ads:
                fragment = new AdsFragment();
                fab_Winery.setVisibility(View.GONE);
                fab_Event.setVisibility(View.GONE);
                fab_Tour.setVisibility(View.GONE);
                fab_Festival.setVisibility(View.GONE);
                fab_ADs.setVisibility(View.VISIBLE);
                break;
        }
        return loadFragment(fragment);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_My:
                Intent my_intent = new Intent(activity_Main.this, Activity_Before_My.class);
                startActivity(my_intent);
                break;
            case R.id.fab_Winery:
                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
                Askpay_intent.putExtra(constant.PAYMONEY, "winery");
                startActivity(Askpay_intent);
                break;
            case R.id.fab_Event:
                Enable_add_event();
                break;
            case R.id.fab_Tour:
                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
                Askpay_intent.putExtra(constant.PAYMONEY, "tour");
                startActivity(Askpay_intent);
                break;
            case R.id.fab_Festival:
                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
                Askpay_intent.putExtra(constant.PAYMONEY, "festival");
                startActivity(Askpay_intent);
                break;
            case R.id.fab_ADs:
                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
                Askpay_intent.putExtra(constant.PAYMONEY, "ads");
                startActivity(Askpay_intent);
                break;
            case R.id.tv_setting:
                Intent setting_intent = new Intent(activity_Main.this, activity_Setting.class);
                startActivity(setting_intent);
                break;
            case R.id.tv_close:
                finish();
                break;
        }
    }
    private void Enable_add_event() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.ENABLE_ADD_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);
                        if(responseModel.getStatus().equals("200")){
                            Toast.makeText(activity_Main.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent event_intent = new Intent(activity_Main.this, Claim_event.class);
                            startActivity(event_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), activity_Main.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_Main.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srStatus);
    }
    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",constant.Auth_UserName, constant.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

}