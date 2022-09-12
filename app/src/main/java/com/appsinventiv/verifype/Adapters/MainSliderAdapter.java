package com.appsinventiv.verifype.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.appsinventiv.verifype.Activites.LatestFrauds;
import com.appsinventiv.verifype.Activites.WebViewActivity;
import com.appsinventiv.verifype.Models.BannerModel;
import com.appsinventiv.verifype.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainSliderAdapter  extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BannerModel> picturesList;

    public MainSliderAdapter(Context context, ArrayList<BannerModel> picturesList) {

        this.context = context;
        this.picturesList = picturesList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.main_product_slider, container, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView sliderText = view.findViewById(R.id.sliderText);
        Glide.with(context).load(picturesList.get(position).getImageUrl()).into(imageView);
        container.addView(view);
        sliderText.setText(picturesList.get(position).getMessage());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, WebViewActivity.class);
                i.putExtra("url",picturesList.get(position).getUrl());
                context.startActivity(i);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return picturesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);

    }

}
