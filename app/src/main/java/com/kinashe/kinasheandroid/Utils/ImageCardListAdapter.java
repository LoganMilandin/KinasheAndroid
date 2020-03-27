package com.kinashe.kinasheandroid.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.ImageCard;
import com.kinashe.kinasheandroid.R;

import java.util.List;

public class ImageCardListAdapter extends RecyclerView.Adapter<ImageCardListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ImageCard> mData;

    public ImageCardListAdapter(Context mContext, List<ImageCard> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.place_name.setText(mData.get(position).getText());
        holder.place_img.setImageResource(mData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView place_name;
        ImageView place_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            place_name = itemView.findViewById(R.id.place_name_id);
            place_img = itemView.findViewById(R.id.image_id);
        }
    }
}
