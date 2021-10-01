package com.example.madfinalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PackageRecViewAdapter extends RecyclerView.Adapter<PackageRecViewAdapter.ViewHolder>{

    private ArrayList<Package> packages = new ArrayList<>();

    private final Context context;

    public PackageRecViewAdapter(Context context){

        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()) . inflate(R.layout.package_list_items, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemPosition = holder.getAdapterPosition();
        holder.packageName.setText(packages.get(position).getName());
        holder.packageGuCount.setText(packages.get(position).getNuGuest() + " Guest/Room");
        holder.packagePrice.setText( "LKR " +packages.get(position).getFee() + "/Day");

        holder.visitPackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PackageMainView.class);
                intent.putExtra("uuid", packages.get(itemPosition).getUuid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //private  CardView parent;
        private final TextView packageName;
        private final TextView packageGuCount;
        private final TextView packagePrice;
        private final Button visitPackageBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            packageName = itemView.findViewById(R.id.package_name);
            packageGuCount = itemView.findViewById(R.id.package_guest);
            packagePrice = itemView.findViewById(R.id.package_price);
            visitPackageBtn = itemView.findViewById(R.id.package_visit);
            //parent = itemView.findViewById(R.id.parent);


        }
    }


}
