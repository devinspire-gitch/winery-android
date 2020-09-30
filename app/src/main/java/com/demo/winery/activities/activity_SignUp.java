package com.demo.winery.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.Validation;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class activity_SignUp extends AppCompatActivity implements View.OnClickListener {
    Button btn_Signup;
    EditText edt_fName, edt_lName, edt_Email, edt_Address, edt_City, edt_State, edt_Zip;
    String t_fName;
    String t_lName;
    String t_Email;
    String t_Address;
    String t_City;
    String t_State;
    String t_Zip;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_signup);

        btn_Signup = findViewById(R.id.btn_Signup);
        btn_Signup.setOnClickListener(this);

        edt_fName = findViewById(R.id.edt_fName);
        edt_lName = findViewById(R.id.edt_lName);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Address = findViewById(R.id.edt_Address);
        edt_City = findViewById(R.id.edt_City);
        edt_State = findViewById(R.id.edt_State);
        edt_Zip = findViewById(R.id.edt_Zip);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Chk_online();
    }

    private void Chk_online() {

        t_fName = edt_fName.getText().toString().trim();
        t_lName = edt_lName.getText().toString().trim();
        t_Email = edt_Email.getText().toString().trim();
        t_Address = edt_Address.getText().toString().trim();
        t_City = edt_City.getText().toString().trim();
        t_State = edt_State.getText().toString().trim();
        t_Zip = edt_Zip.getText().toString().trim();

        if (Methods.isOnline(activity_SignUp.this)) {
            if (TextUtils.isEmpty(t_fName) && TextUtils.isEmpty(t_lName) && TextUtils.isEmpty(t_Email) && TextUtils.isEmpty(t_Address) && TextUtils.isEmpty(t_City) && TextUtils.isEmpty(t_State) && TextUtils.isEmpty(t_Zip)) {
                Methods.showAlertDialog("Enter Sign Up Details.", this);
            } else {
                if (Validation.isValidEmail(t_Email)) {
                    Methods.showProgress(activity_SignUp.this);
                    registeration();
                } else {
                    Methods.showAlertDialog("Please enter valid EmailId", this);
                }
            }
        } else {
            Methods.showAlertDialog("Please check Internet connection.", this);
        }

    }

    public void registeration() {
        Methods.closeProgress();
        StringRequest srReg = new StringRequest(Request.Method.POST, constant.USER_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                        if(objRes!=null){
                            if (objRes.getStatus().equals("200")) {
                                Methods.showAlertDialog(objRes.getMsg(), activity_SignUp.this);
                            }else{
                                Methods.showAlertDialog(objRes.getMsg(), activity_SignUp.this);
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
                params.put(constant.FNAME, t_fName);
                params.put(constant.LNAME, t_lName);
                params.put(constant.EMAIL, t_Email);
                params.put(constant.ADDRESS, t_Address);
                params.put(constant.CITY, t_City);
                params.put(constant.STATE, t_State);
                params.put(constant.ZIP, t_Zip);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(srReg);
    }
}
