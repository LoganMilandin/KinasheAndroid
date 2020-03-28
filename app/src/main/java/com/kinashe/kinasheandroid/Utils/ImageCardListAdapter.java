package com.kinashe.kinasheandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.NearbyAllActivity;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.place_name.setText(mData.get(position).getText());
        holder.place_img.setImageResource(mData.get(position).getImage());
        holder.image_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = v.findViewById(R.id.place_name_id);
                String cardText = (String) text.getText();
                int image = mData.get(position).getImage(); //get the image we're clicking on
                mData = new ImageCardHelper(cardText).get();
                if (mData != null) {
                    TopBarHelper.setTopText(cardText, mContext);
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mContext, NearbyAllActivity.class);
                    intent.putExtra("image", image);
                    intent.putExtra("topText", cardText);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView place_name;
        ImageView place_img;
        CardView image_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            place_name = (TextView) itemView.findViewById(R.id.place_name_id);
            place_img = (ImageView) itemView.findViewById(R.id.image_id);
            image_card = (CardView) itemView.findViewById(R.id.image_card_id);
        }
    }
}
