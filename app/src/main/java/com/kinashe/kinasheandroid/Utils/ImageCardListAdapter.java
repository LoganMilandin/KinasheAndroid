package com.kinashe.kinasheandroid.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.CustomFragment;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.NearbyAllFragment;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;

import java.util.List;

public class ImageCardListAdapter extends RecyclerView.Adapter<ImageCardListAdapter.MyViewHolder> {

    private static final String TAG = "ImageCardListAdapter";

    private MainActivity context;
    private CustomFragment fragment;
    private View myView;
    private List<ImageCard> cards;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView place_name;
        ImageView place_img;
        CardView image_card;

        public MyViewHolder(View itemView) {
            super(itemView);
            place_name = itemView.findViewById(R.id.place);
            place_img = itemView.findViewById(R.id.image);
            image_card = itemView.findViewById(R.id.image_card);
        }
    }

    public ImageCardListAdapter(View myView, MainActivity context, PlacesOrTransportationFragment fragment, List<ImageCard> cards) {
        this.context = context;
        this.fragment = fragment;
        this.myView = myView;
        this.cards = cards;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.layout_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Log.d(TAG, "binding image card adapter");
        final ImageCard card = cards.get(position);
        holder.place_name.setText(card.getText());
        holder.place_img.setImageResource(card.getImage());
        holder.image_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View gridbox) {
                Log.d(TAG, "clicked grid");
                TextView selectedTextView = gridbox.findViewById(R.id.place);
                String selectedText = (String) selectedTextView.getText();
                TextView titleTextView = myView.findViewById(R.id.title);
                String titleText = (String) titleTextView.getText();
                List<ImageCard> newCards = ((PlacesOrTransportationFragment)fragment).getCardHelper().getCards(selectedText);
                if (newCards != null) {
                    cards = newCards;
                    gridItemSelectedHelper(selectedText, titleText);
                    notifyDataSetChanged();
                } else {
                    NearbyAllFragment newFragment = new NearbyAllFragment();
                    newFragment.setParent(fragment);
                    fragment.setChild(newFragment);
                    ImageCard selectedImage = cards.get(position);
                    int image = selectedImage.getImage();
                    Bundle categoryContainer = new Bundle();
                    context.navigationManager.setFragmentNavbarIndex(newFragment);
                    categoryContainer.putString("title", selectedText);
                    categoryContainer.putInt("image", image);
                    newFragment.setArguments(categoryContainer);
                    context.navigationManager.handleNewFragmentCreated(newFragment);
                }
            }
        });
    }

    private void gridItemSelectedHelper(String newTitle, final String oldTitle) {
        final TextView title = myView.findViewById(R.id.title);
        title.setText(newTitle);
        ImageView backButton = myView.findViewById(R.id.back_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View backButton) {
                //simply go back to the screen corresponding to currentTitle
                cards = ((PlacesOrTransportationFragment)fragment).getCardHelper().getCards(oldTitle);
                title.setText(oldTitle);
                if (oldTitle.equals("Places | ቦታዎች") || oldTitle.equals("Transportation | መጓጓዣ")) {
                    backButton.setVisibility(View.INVISIBLE);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

}
