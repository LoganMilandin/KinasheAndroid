package com.kinashe.kinasheandroid.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.NearbyAllFragment;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.SingleBusinessFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder> {

    private static final String TAG = "BusinessListAdapter";

    private MainActivity context;
    public CustomFragment fragment;



    private List<BusinessInfo> businesses;

    public static class BusinessViewHolder extends RecyclerView.ViewHolder {
        public ImageView businessPhoto;
        public TextView companyName;
        public TextView businessType;
        public TextView distance;
        public ImageView navLogo;
        public ImageView phoneLogo;
        public ImageView searchLogo;


        public BusinessViewHolder(@NonNull View businessView) {
            super(businessView);
            this.businessPhoto = businessView.findViewById(R.id.businessPhoto);
            this.companyName = businessView.findViewById(R.id.companyName);
            this.businessType = businessView.findViewById(R.id.businessType);
            this.distance = businessView.findViewById(R.id.distance);
            this.navLogo = businessView.findViewById(R.id.directions);
            this.phoneLogo = businessView.findViewById(R.id.phone);
            this.searchLogo = businessView.findViewById(R.id.website);
        }

    }

    public BusinessListAdapter(List<BusinessInfo> businesses, MainActivity context, CustomFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        if (businesses != null) {
            this.businesses = businesses;
        } else {
            this.businesses = new ArrayList<>();
        }
    }

    @Override
    public BusinessListAdapter.BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "constructing list adapter");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //now, we inflate a custom layout file with the markup for an individual business
        View businessView = inflater.inflate(R.layout.layout_business, parent, false);
        BusinessViewHolder view = new BusinessViewHolder(businessView);
        return view;
    }

    @Override
    public void onBindViewHolder(final BusinessViewHolder holder, int position) {
        Log.d(TAG, "binding homepage adapter");
        final BusinessInfo business = businesses.get(position);
        List<String> photos = business.getPhotos();
        //not sure if failed photo submissions will show up as null or
        //empty on the read request so this is just to be safe
        if (photos != null && !photos.isEmpty()) {
            Picasso.get().load(photos.get(0)).into(holder.businessPhoto);
        }
        holder.companyName.setText(business.getCompanyName());
        holder.businessType.setText(business.getBusinessType());
        if (context.location != null) {
            holder.distance.setText(business.getDistance() + " km");
        } else {
            holder.distance.setText("location services disabled or unavailable");
        }
        setOnClickListeners(business, holder);
    }

    private void setOnClickListeners(final BusinessInfo business, final BusinessViewHolder holder) {
        //listen for click to open google maps
        holder.navLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View nav, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "clicked grid");
                    ((ImageView)nav).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "released");
                    ((ImageView)nav).clearColorFilter();
                    Intent directionsIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + business.getLat() + "," + business.getLon()));
                    context.startActivity(directionsIntent);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ((ImageView)nav).clearColorFilter();
                    return false;

                }
                return false;
            }
        });
        //listen for click to open phone
        holder.phoneLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View phone, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "clicked grid");
                    ((ImageView)phone).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "released");
                    ((ImageView)phone).clearColorFilter();
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                    phoneCallIntent.setData(Uri.parse("tel:" + business.getPhone()));
                    PermissionUtils.makePhoneCall(context, phoneCallIntent, MainActivity.CALL_REQUEST_CODE);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ((ImageView)phone).clearColorFilter();
                    return false;

                }
                return false;
            }
        });
        //again we expect an empty string here but it never hurts to be safe
        if (business.getWebsite() == null || business.getWebsite().equals("")) {
            holder.searchLogo.setVisibility(View.INVISIBLE);
        } else {
            holder.searchLogo.setVisibility(View.VISIBLE);
            holder.searchLogo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View web, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.d(TAG, "clicked grid");
                        ((ImageView)web).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.d(TAG, "released");
                        ((ImageView)web).clearColorFilter();
                        String website = business.getWebsite().toLowerCase();
                        Intent browserIntent;
                        if (website.contains("http")) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                        } else {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + website));
                        }
                        context.startActivity(browserIntent);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        ((ImageView)web).clearColorFilter();
                        return false;
                    }
                    return false;
                }
            });
        }
        //now we just set onClick listeners for all the other components of the view to open
        //that business
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View photo) {
                CustomFragment newFragment = new SingleBusinessFragment();
                //connect fragments
                newFragment.setParent(fragment);
                Log.d(TAG, "child null? " + (newFragment.getParent() == null));
                fragment.setChild(newFragment);
                Log.d(TAG, "child null? " + (fragment.getChild() == null));
                Bundle businessWrapper = new Bundle();
                context.navigationManager.setFragmentNavbarIndex(newFragment);
                businessWrapper.putSerializable("businessInfo", business);
                newFragment.setArguments(businessWrapper);
                context.navigationManager.handleNewFragmentCreated(newFragment);
            }
        };
        holder.businessPhoto.setOnClickListener(listener);
        holder.companyName.setOnClickListener(listener);
        holder.businessType.setOnClickListener(listener);
        holder.distance.setOnClickListener(listener);
    }
    public int getItemCount() {
        return businesses.size();
    }

    public void changeList(List<BusinessInfo> newList) {
        this.businesses = newList;
        notifyDataSetChanged();
    }
}