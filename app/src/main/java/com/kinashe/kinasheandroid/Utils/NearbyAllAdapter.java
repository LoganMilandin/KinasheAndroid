package com.kinashe.kinasheandroid.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.R;

import java.util.List;

public class NearbyAllAdapter extends RecyclerView.Adapter<NearbyAllAdapter.MyViewHolder> {

    private Context mContext;
    private int image;

    public NearbyAllAdapter(Context mContext, int image) {
        this.mContext = mContext;
        this.image = image;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.activity_nearby_all, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.nearbyAllImage.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView nearbyAllImage;
        CardView topButton;
        CardView bottomButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            nearbyAllImage = (ImageView) itemView.findViewById(R.id.nearbyAllImage);
            topButton = (CardView) itemView.findViewById(R.id.topButton);
            bottomButton = (CardView) itemView.findViewById(R.id.bottomButton);
        }
    }
}