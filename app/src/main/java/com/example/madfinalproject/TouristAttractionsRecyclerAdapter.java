package com.example.madfinalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Map;

public class TouristAttractionsRecyclerAdapter extends RecyclerView.Adapter<TouristAttractionsRecyclerAdapter.ViewHolder>{
    private ArrayList<AttractionPlaces> attractionPlaces = new ArrayList<>();
    private final Context context;

    public TouristAttractionsRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TouristAttractionsRecyclerAdapter.ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        holder.placeName.setText(attractionPlaces.get(position).getName());
        holder.placeCity.setText(attractionPlaces.get(position).getCity());
        String displayImgUrl = "";

        for ( Map.Entry<String, String> entry : attractionPlaces.get(position).getImages().entrySet()) {
            displayImgUrl = entry.getValue();
            break;
        }

        Glide.with(context)
                .asBitmap()
                .load(displayImgUrl)
                .into(holder.placeImgId);

        holder.visitPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TouristAttractionsView.class);
                intent.putExtra("placeId", attractionPlaces.get(itemPosition).getPlaceId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attractionPlaces.size();
    }

    public void setAttractionPlaces(ArrayList<AttractionPlaces> attractionPlaces) {
        this.attractionPlaces = attractionPlaces;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView placeImgId;
        private final TextView placeName, placeCity;
        private final Button visitPlaceBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImgId = itemView.findViewById(R.id.placeImgId);
            placeName = itemView.findViewById(R.id.placeName);
            placeCity = itemView.findViewById(R.id.placeCity);
            visitPlaceBtn = itemView.findViewById(R.id.visitPlaceBtn);
        }
    }

}
