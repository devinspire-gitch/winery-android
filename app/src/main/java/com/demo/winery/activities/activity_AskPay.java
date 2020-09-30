package com.demo.winery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.winery.R;
import com.demo.winery.stripe.stripe_main;
import com.demo.winery.utils.Config;
import com.demo.winery.utils.constant;
import com.paypal.android.sdk.payments.PayPalConfiguration;

public class activity_AskPay extends AppCompatActivity implements View.OnClickListener {

    LinearLayout plan5;
    String from;
    TextView tv_money;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.layout_askmoney);


        Bundle extras = getIntent().getExtras();
        from = extras.getString(constant.PAYMONEY);
        Log.e("money", from);

        tv_money = findViewById(R.id.tv_money);
        if (from.equals("winery")){
            tv_money.setText("$5 per month \n1. Update my winery information \n2. Reply to reviews made by users\n3. Add events that will show on their winery listing page and on the events section of the app");
        }else if (from.equals("tour")){
            tv_money.setText("$10 per month \n1. Update my Tour information \n");
        }else if (from.equals("festival")){
            tv_money.setText("$100 onece submit festival \n");
        }else if (from.equals("ads")){
            tv_money.setText("$10 once submit Ads \n1. Update their Ads information \n");
        }

        plan5 = findViewById(R.id.plan5);
        plan5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.plan5:
                processPayment();
                break;
        }
    }
    private void processPayment() {
        Intent progressIntent = new Intent(activity_AskPay.this, stripe_main.class);
        progressIntent.putExtra("PLAN_FLAG", from);
        startActivity(progressIntent);
        finish();
    }
}