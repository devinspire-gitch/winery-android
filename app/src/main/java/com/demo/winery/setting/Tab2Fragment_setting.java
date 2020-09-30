package com.demo.winery.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Tab2Fragment_setting extends Fragment implements View.OnClickListener {

    Switch toggleButton;
    RadioButton chk_25, chk_50, chk_75, chk_100, chk_200;
    Button btn_Submit_setting;
    int xMiles; String t_xMile;
    Boolean email_notify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_setting, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleButton = view.findViewById(R.id.toggleButton);
        chk_25 = view.findViewById(R.id.chk_25);
        chk_50 = view.findViewById(R.id.chk_50);
        chk_75 = view.findViewById(R.id.chk_75);
        chk_100 = view.findViewById(R.id.chk_100);
        chk_200 = view.findViewById(R.id.chk_200);
        btn_Submit_setting = view.findViewById(R.id.btn_Submit_setting);

        btn_Submit_setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Submit_setting:
                if (chk_25.isChecked()){
                    xMiles = 25;
                }else if (chk_50.isChecked()){
                    xMiles = 50;
                }else if (chk_75.isChecked()){
                    xMiles = 75;
                }else if (chk_100.isChecked()){
                    xMiles = 100;
                }else if (chk_200.isChecked()){
                    xMiles = 200;
                }
                email_notify = toggleButton.isChecked();
                if (email_notify){
                    t_xMile = "Y";
                }else {
                    t_xMile = "N";
                }
                setting_process();
        }
    }

    private void setting_process() {
        StringRequest srReg = new StringRequest(Request.Method.POST, constant.USER_SETTING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                        if(objRes!=null){
                            if (objRes.getStatus().equals("200")) {
                                Methods.showAlertDialog(objRes.getMsg(), getContext());
                            }else{
                                Methods.showAlertDialog(objRes.getMsg(), getContext());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_SignUp.this);
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                params.put("xMiles", String.valueOf(xMiles));
                params.put("notification", t_xMile);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(srReg);
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

}