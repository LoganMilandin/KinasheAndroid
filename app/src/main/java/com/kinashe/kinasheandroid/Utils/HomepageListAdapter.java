package com.kinashe.kinasheandroid.Utils;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.HomeFragment;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.SearchBusinessFragment;
import com.kinashe.kinasheandroid.SingleBusinessFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomepageListAdapter extends RecyclerView.Adapter<HomepageListAdapter.BusinessViewHolder> {

    private static final String TAG = "HomepageListAdapter";

    private MainActivity context;
    private Fragment fragment;

    private int totalBinds;

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

    public HomepageListAdapter(List<BusinessInfo> businesses, MainActivity context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        if (businesses != null) {
            this.businesses = businesses;
        } else {
            this.businesses = new ArrayList<>();
        }
        totalBinds = 0;
    }

    @Override
    public HomepageListAdapter.BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "constructing list adapter");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //now, we inflate a custom layout file with the markup for an individual business
        View businessView = inflater.inflate(R.layout.layout_business, parent, false);
        BusinessViewHolder view = new BusinessViewHolder(businessView);
        return view;
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
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

    private void setOnClickListeners(final BusinessInfo business, BusinessViewHolder holder) {
        //listen for click to open google maps
        holder.navLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View navIcon) {
                Intent directionsIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + business.getLat() + "," + business.getLon()));
                context.startActivity(directionsIntent);
            }
        });
        //listen for click to open phone
        holder.phoneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View phoneIcon) {
                Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                phoneCallIntent.setData(Uri.parse("tel:" + business.getPhone()));
                PermissionUtils.makePhoneCall(context, phoneCallIntent, MainActivity.CALL_REQUEST_CODE);
            }
        });
        //again we expect an empty string here but it never hurts to be safe
        if (business.getWebsite() == null || business.getWebsite().equals("")) {
            holder.searchLogo.setVisibility(View.INVISIBLE);
        } else {
            //listen for click to open website
            holder.searchLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View websiteIcon) {
                    String website = business.getWebsite().toLowerCase();
                    Intent browserIntent;
                    if (website.contains("http")) {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    } else {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + website));
                    }
                    context.startActivity(browserIntent);
                }
            });
        }
        //now we just set onClick listeners for all the other components of the view to open
        //that business
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View photo) {
                Log.d(TAG, "clicked");
                SingleBusinessFragment newFragment = new SingleBusinessFragment();
                Bundle businessWrapper = new Bundle();
                businessWrapper.putSerializable("businessInfo", business);
                businessWrapper.putString("parent type", fragment instanceof HomeFragment?"home":"search");
                newFragment.setArguments(businessWrapper);
                if (fragment instanceof HomeFragment) {
                    context.navigationManager.handleSingleBusinessSelectedFromHomepage(newFragment);
                } else if (fragment instanceof SearchBusinessFragment) {
                    context.navigationManager.handleSingleBusinessSelectedFromSearch(newFragment);
                }
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