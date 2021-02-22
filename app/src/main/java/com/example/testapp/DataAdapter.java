package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder > {
    private final List<DataItem> mDataItem;
    Context context;

    public DataAdapter(ArrayList<DataItem> mDataItem, Context context) {
        this.mDataItem = mDataItem;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data,parent,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataViewHolder holder, int position) {
        holder.textViewName.setText(mDataItem.get(position).getName());
        holder.textViewCountry.setText(mDataItem.get(position).getCountry());
        new DownloadImage(holder.imageView)
                .execute("https://3lhbo71cnadi83snz267qfx1-wpengine.netdna-ssl.com/wp-content/uploads/2017/03/location.png");
    }

    @Override
    public int getItemCount() {
        return mDataItem.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewCountry;
        ImageView imageView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCountry = itemView.findViewById(R.id.textViewCountry);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataItem dataItem = mDataItem.get(getLayoutPosition());
                    Intent intent = new Intent(context, GeoMap.class);
                    intent.putExtra("id", dataItem.getId());
                    intent.putExtra("name", dataItem.getName());
                    intent.putExtra("country", dataItem.getCountry());
                    double lat, lon;
                    try {lat = Double.parseDouble(dataItem.getLat());}
                    catch (NumberFormatException e){
                        lat =0;
                    }
                    try {lon = Double.parseDouble(dataItem.getLon());}
                    catch (NumberFormatException e){
                        lon =0;
                    }
                    intent.putExtra("lat", lat);
                    intent.putExtra("lon", lon);
                    context.startActivity(intent);
                }
            });
        }
    }
    @Override
    public boolean onFailedToRecycleView(@NonNull DataAdapter.DataViewHolder holder) {
        return true;
    }
}
