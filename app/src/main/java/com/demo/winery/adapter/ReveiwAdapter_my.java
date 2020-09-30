package com.demo.winery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.demo.winery.R;
import com.demo.winery.model.BaseCompanyInfo;

import java.util.List;

public class ReveiwAdapter_my extends ArrayAdapter<BaseCompanyInfo> implements View.OnClickListener {

    List<String> l;
    Context mContext;
    EditText reply_review;
    Button reply_btn;
    /**
     *
     * Constructor
     *
     * @param context  The current context.
     * @param object The resource ID for a layout file containing a TextView to use when
     */
    public ReveiwAdapter_my(@NonNull Context context, List<BaseCompanyInfo> object) {
        super(context, 0, object);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.list_item_review_my,null);

        LinearLayout ly_review = (LinearLayout) convertView.findViewById(R.id.ly_review);
        TextView tv_review = (TextView) convertView.findViewById(R.id.tv_review);
        TextView tv_review_detail = (TextView) convertView.findViewById(R.id.tv_review_detail);
        RatingBar review_rating = (RatingBar) convertView.findViewById(R.id.review_rating);
        reply_review = (EditText) convertView.findViewById(R.id.reply_review);
        reply_btn = (Button) convertView.findViewById(R.id.reply_btn);
        reply_btn.setOnClickListener(this);

        tv_review.setText(getItem(position).getreview_text());
        tv_review_detail.setText(getItem(position).getusername()+"--"+getItem(position).getemail_review());
        review_rating.setNumStars((int) getItem(position).getstars());
        return convertView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.e("reviewadapter", "xxxxxx"+reply_review.getText().toString());
    }
}
