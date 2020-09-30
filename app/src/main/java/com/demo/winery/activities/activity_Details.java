package com.demo.winery.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.adapter.TabAdapter;
import com.demo.winery.fragment.GoogleMapFragment;
import com.demo.winery.fragment.Tab1Fragment;
import com.demo.winery.fragment.Tab2Fragment;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.demo.winery.utils.constant.MAPVIEW_BUNDLE_KEY;

public class activity_Details extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView distance_view;
    private ImageView mapPreview;
    String id;
    MapView mapView;
    String address, zip;
    float star_review;
    Button map_btn;
    RatingBar map_view_rate;
    GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    LatLng current_location;
    LatLng address_location;
    double current_latitude, current_longtitude, distance_value;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_details);

        Methods.closeProgress();

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        address = data.getStringExtra(constant.ADDRESS);
        zip = data.getStringExtra(constant.ZIP);
        star_review = data.getFloatExtra(constant.REVIEW_STAR,0.0f);

        mapView = (MapView) findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

        map_view_rate = findViewById(R.id.map_view_rate);
        map_view_rate.setRating(star_review);

        map_btn = (Button) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GoogleMapFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.google_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "About");
        adapter.addFragment(new Tab2Fragment(), "Review");
        //adapter.addFragment(new Tab3Fragment(), "Wine");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        distance_view = findViewById(R.id.map_distance);
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    public String id_selector(){
        return id;
    }
    @Override
    public void onClick(View v) {

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
        mMap = map;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mMap.setMyLocationEnabled(true);
        int height = 150;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener(){
            @Override
            public void onMyLocationChange(Location location) {

               current_latitude = location.getLatitude();
               current_longtitude = location.getLongitude();

            }
        });

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        address_location = getLocationFromAddress(activity_Details.this, zip);
        mMap.addMarker(new MarkerOptions().position(address_location).title("Marker"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
        mapPreview = (ImageView) findViewById(R.id.map_image);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // Make a snapshot when map's done loading
                    mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            Bitmap bmpGrayscale = Bitmap.createBitmap(bitmap.getWidth(),
                                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                            Canvas canvas = new Canvas(bmpGrayscale);
                            Paint paint = new Paint();

                            ColorMatrix cm = new ColorMatrix();
                            cm.setSaturation(2);
                            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                            paint.setColorFilter(f);
                            canvas.drawBitmap(bitmap, 0, 0, paint);

                            mapPreview.setLayoutParams(new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                   ));
                            mapPreview.setImageBitmap(bmpGrayscale);
                            mapView.removeAllViews();
                            // If map won't be used afterwards, remove it's views
                            //     ((FrameLayout)findViewById(R.id.map)).removeAllViews();
                        }
                    });
                }
            });
        distance_value = distance(current_latitude, current_longtitude, address_location.latitude, address_location.longitude);
        distance_view.setText(String.valueOf(distance_value));
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

    //how to get Latitude and Longtitude from address
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

    private int distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        int dist_int = (int) dist;
        return (dist_int);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}