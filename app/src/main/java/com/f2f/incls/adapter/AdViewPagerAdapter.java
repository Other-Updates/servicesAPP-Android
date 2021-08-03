package com.f2f.incls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.f2f.incls.R;
import com.f2f.incls.activity.CustomerDashBoard;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AdViewPagerAdapter extends PagerAdapter {
    Context context;
    private List<String> imagePaths;
    private LayoutInflater inflater;
    ImageView ads_image;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    Integer[] imageId;
    String[] imageName;

    public AdViewPagerAdapter(Context context, List<String> imagePaths) {
        this.context=context;
        this.imagePaths=imagePaths;
    }

    public AdViewPagerAdapter(Context context, Integer[] imageId, String[] imagesName) {
        this.context=context;
        this.imageId=imageId;
        this.imageName=imagesName;
    }

    @Override
    public int getCount() {

        return imagePaths.size();
    }


    @NonNull
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        View view=LayoutInflater.from(container.getContext())
                .inflate(R.layout.ad_images_view,container,false);
        imgDisplay = (ImageView) view.findViewById(R.id.ads_image);
        String path=imagePaths.get(position);

        if (path.isEmpty()){

        }else {
            Picasso.get()
                    .load(path)
                    .into(imgDisplay);
        }


        (container).addView(view);
        Log.d("Success","Glide_image::"+imagePaths.get(position));
        return view;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }



}
