package com.demo.winery.tour;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.Company_content;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.winery.utils.constant.MAPVIEW_BUNDLE_KEY;

public class Tour_detail extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    MapView mapView;
    String id, zip;
    TextView tv_tourName, tv_des_con,tv_address_c, tv_city_c, tv_state_c, tv_zip_c, tv_phone_t, tv_email_t, tv_ads_c, tv_phone_c, tv_email_c;

    ScrollView scroll_content;
    RatingBar view_rate;
    String strtext;
    TextView no_rating;
    Company_content objRes;
    ImageView img_logo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Methods.closeProgress();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_tour_detail);

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        zip = data.getStringExtra(constant.ZIP);
        tour_info_progress(id);

        tv_tourName = findViewById(R.id.tv_tourName);
        tv_des_con = (TextView) findViewById(R.id.tv_des_con);
        tv_address_c = (TextView) findViewById(R.id.tv_address_c);
        tv_city_c = (TextView) findViewById(R.id.tv_city_c);
        tv_state_c = (TextView) findViewById(R.id.tv_state_c);
        tv_zip_c = (TextView) findViewById(R.id.tv_zip_c);
        tv_ads_c = (TextView) findViewById(R.id.tv_ads_c);
        tv_phone_t = (TextView) findViewById(R.id.tv_phone_t);
        tv_email_t = (TextView) findViewById(R.id.tv_email_t);
        tv_phone_c = (TextView) findViewById(R.id.tv_phone_c);
        tv_email_c = (TextView) findViewById(R.id.tv_email_c);
        view_rate = (RatingBar) findViewById(R.id.view_rate);
        no_rating = (TextView) findViewById(R.id.no_rating);
        img_logo = findViewById(R.id.img_logo);

        tv_phone_c.setOnClickListener(this);
        tv_email_c.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
    }

    private void tour_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.TOUR_GETINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            tv_tourName.setText(objRes.getCompany());
                            tv_des_con.setText(objRes.getDescription());
                            tv_address_c.setText(objRes.getAddress());
                            tv_city_c.setText(objRes.getCity());
                            tv_state_c.setText(objRes.getState());
                            tv_zip_c.setText(objRes.getZip());
                            tv_ads_c.setText(objRes.getWebsite());
                            tv_phone_t.setText(objRes.getPhone());
                            tv_email_t.setText(objRes.getEmail());
                            if (objRes.getstars() == 0){
                                view_rate.setVisibility(View.GONE);
                                no_rating.setText("No reviews");
                            }else {
                                view_rate.setRating(objRes.getstars());
                                no_rating.setVisibility(View.GONE);
                            }
                            byte[] imageBytes = Base64.decode(objRes.getlogo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_logo.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(Tour_detail.this, "No Review", Toast.LENGTH_LONG).show();
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
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Tour_detail.this);
        requestQueue.add(res_companyInfo);
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_phone_c:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+objRes.getPhone()));

                if (ActivityCompat.checkSelfPermission(Tour_detail.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.tv_email_c:
                String[] TO = {getString(constant.EMAIL)};
                String[] CC = {objRes.getEmail()};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    getFragmentManager().popBackStack();
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Tour_detail.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onMapReady(GoogleMap map) {

        int height = 150;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.tour_logo);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng address_location = getLocationFromAddress(Tour_detail.this, zip);
        map.addMarker(new MarkerOptions().position(address_location).title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> addresses;
        LatLng p1 = null;

        try
        {
            addresses = coder.getFromLocationName(strAddress, 5);
            if(addresses==null)
            {
                return null;
            }
            Address location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
    public String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
}