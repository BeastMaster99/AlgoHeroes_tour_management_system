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

public class placeImageSliderAdapter extends SliderViewAdapter<placeImageSliderAdapter.Holder> {
    private HashMap<String, String> inputUrls;

    private ArrayList<String> imageUrls = new ArrayList<>();
    private final Context context;

    public placeImageSliderAdapter(Context context) {
        this.context = context;
    }

    public void setInputUrls(HashMap<String, String> inputUrls) {
        this.inputUrls = inputUrls;
    }

    @Override
    public placeImageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_image_slider,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(placeImageSliderAdapter.Holder viewHolder, int position) {
        for ( Map.Entry<String, String> entry : inputUrls.entrySet()) {
            imageUrls.add(entry.getValue());
        }

        Glide.with(context)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(viewHolder.placeSliderImg);
    }

    @Override
    public int getCount() {
        return inputUrls.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        private final ImageView placeSliderImg;
        public Holder(View itemView) {
            super(itemView);
            placeSliderImg = itemView.findViewById(R.id.placeSliderImg);
        }
    }

}
