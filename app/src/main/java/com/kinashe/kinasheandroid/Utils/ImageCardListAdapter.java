package com.kinashe.kinasheandroid.Utils;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
    private TextView title;
    private ImageView backButton;

    private List<ImageCard> cards;

    private String currentTitle;
    private String previousTitle;

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
        this.title = myView.findViewById(R.id.title);
        this.backButton = myView.findViewById(R.id.back_button);
        this.cards = cards;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.layout_category_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Log.d(TAG, "binding image card adapter");
        final ImageCard card = cards.get(position);
        holder.place_name.setText(card.getText());
        holder.place_img.setImageResource(card.getImage());
        holder.place_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View gridbox, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "clicked grid");
                    holder.place_img.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.place_img.clearColorFilter();
                    if (event.getX() > gridbox.getLeft() && event.getX() < gridbox.getRight()
                        && event.getY() > gridbox.getTop() && event.getY() < gridbox.getBottom()) {
                        TextView selectedTextView = holder.image_card.findViewById(R.id.place);
                        String newTitle = (String) selectedTextView.getText();
                        TextView titleTextView = myView.findViewById(R.id.title);
                        String oldTitle = (String) titleTextView.getText();
                        List<ImageCard> newCards = ((PlacesOrTransportationFragment) fragment).getCardHelper().getCards(newTitle);
                        if (newCards != null) {
                            cards = newCards;
                            ImageCardListAdapter.this.previousTitle = oldTitle;
                            gridItemSelectedHelper(newTitle);
                            notifyDataSetChanged();
                        } else {
                            NearbyAllFragment newFragment = new NearbyAllFragment();
                            newFragment.setParent(fragment);
                            fragment.setChild(newFragment);
                            ImageCard selectedImage = cards.get(position);
                            int image = selectedImage.getImage();
                            Bundle categoryContainer = new Bundle();
                            context.navigationManager.setFragmentNavbarIndex(newFragment);
                            categoryContainer.putString("title", newTitle);
                            categoryContainer.putInt("image", image);
                            newFragment.setArguments(categoryContainer);
                            context.navigationManager.handleNewFragmentCreated(newFragment);
                        }
                        return true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    holder.place_img.clearColorFilter();
                    return false;

                }
                return false;
            }
        });
    }

    private void gridItemSelectedHelper(String newTitle) {
        title.setText(newTitle);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View backButton) {
                //simply go back to the screen corresponding to previousTitle
                goBack();
            }
        });
    }

    public void goBack() {
        //if (previousTitle != null) {
        cards = ((PlacesOrTransportationFragment)fragment).getCardHelper().getCards(previousTitle);
        title.setText(previousTitle);
        if (previousTitle.equals("Places | ቦታዎች") || previousTitle.equals("Transportation | መጓጓዣ")) {
            backButton.setVisibility(View.INVISIBLE);
        }
        notifyDataSetChanged();
        previousTitle = null;
    }

    public String getPreviousTitle() {
        return previousTitle;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }





}
