package com.example.madfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelImageSliderAdapter extends SliderViewAdapter<HotelImageSliderAdapter.Holder> {
    private HashMap<String, String> inputUrls;

    private ArrayList<String> imageUrls = new ArrayList<>();
    private final Context context;

    public HotelImageSliderAdapter(Context context) {
        this.context = context;
    }

    public void setInputUrls(HashMap<String, String> inputUrls) {
        this.inputUrls = inputUrls;
    }

    @Override
    public HotelImageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.img_slider_item,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(HotelImageSliderAdapter.Holder viewHolder, int position) {
        for ( Map.Entry<String, String> entry : inputUrls.entrySet()) {
              imageUrls.add(entry.getValue());
        }

        Glide.with(context)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(viewHolder.hotelSliderImg);
    }

    @Override
    public int getCount() {
        return inputUrls.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        private final ImageView hotelSliderImg;
        public Holder(View itemView) {
            super(itemView);
            hotelSliderImg = itemView.findViewById(R.id.hotelSliderImg);
        }
    }
}
