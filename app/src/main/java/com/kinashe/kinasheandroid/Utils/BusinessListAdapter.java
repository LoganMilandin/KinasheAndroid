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
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.SingleBusinessFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * manages the list of businesses that appear on the homepage. This implementation should
 * be reasonably scalable because it loads views as you scroll instead of loading them all at once
 */
public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder> {

    private static final String TAG = "BusinessListAdapter";

    private MainActivity context;
    private CustomFragment fragment;
    private List<BusinessInfo> businesses;
    private int count;

    /**
     * inner class represents a single view in your list, i.e. the thing that
     * repeats
     */
    static class BusinessViewHolder extends RecyclerView.ViewHolder {
        ImageView businessPhoto;
        TextView companyName;
        TextView businessType;
        TextView distance;
        ImageView navLogo;
        ImageView phoneLogo;
        ImageView searchLogo;


        public BusinessViewHolder(@NonNull View businessView) {
            super(businessView);
            businessPhoto = businessView.findViewById(R.id.businessPhoto);
            companyName = businessView.findViewById(R.id.companyName);
            businessType = businessView.findViewById(R.id.businessType);
            distance = businessView.findViewById(R.id.distance);
            navLogo = businessView.findViewById(R.id.directions);
            phoneLogo = businessView.findViewById(R.id.phone);
            searchLogo = businessView.findViewById(R.id.website);
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
        this.count = 0;
    }

    @Override
    public BusinessListAdapter.BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View businessView = inflater.inflate(R.layout.layout_business, parent, false);
        BusinessViewHolder view = new BusinessViewHolder(businessView);
        return view;
    }

    @Override
    public void onBindViewHolder(final BusinessViewHolder holder, int position) {
        Log.d(TAG, "creating views: " + count++);
        final BusinessInfo business = businesses.get(position);
        List<String> photos = business.getPhotos();
        //not sure if failed photo submissions will show up as null or
        //empty on the read request so this is just to be safe
        if (photos != null && !photos.isEmpty()) {
            GlideApp.with(context).load(photos.get(0))
                    .into(holder.businessPhoto);
            //Picasso.get().load(photos.get(0)).into(holder.businessPhoto);
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
        View.OnTouchListener actionListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ImageView)view).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView)view).clearColorFilter();
                    if (view == holder.navLogo) {
                        startNavigation(business);
                    } else if (view == holder.phoneLogo) {
                        startPhoneCall(business);
                    } else if (view == holder.searchLogo) {
                        openWebsite(business);
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ((ImageView)view).clearColorFilter();
                    return false;

                }
                return false;
            }
        };
        holder.navLogo.setOnTouchListener(actionListener);
        holder.phoneLogo.setOnTouchListener(actionListener);
        holder.searchLogo.setOnTouchListener(actionListener);

        //now we just set onClick listeners for all the other components of the view to open
        //that business
        View.OnClickListener businessClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View photo) {
                CustomFragment newFragment = new SingleBusinessFragment();
                //connect fragments
                newFragment.setParent(fragment);
                Log.d(TAG, "child null? " + (newFragment.getParent() == null));
                fragment.setChild(newFragment);
                Log.d(TAG, "child null? " + (fragment.getChild() == null));
                Bundle businessWrapper = new Bundle();
                businessWrapper.putSerializable("businessInfo", business);
                newFragment.setArguments(businessWrapper);
                context.navigationManager.handleNewFragmentCreated(newFragment);
            }
        };
        holder.businessPhoto.setOnClickListener(businessClickListener);
        holder.companyName.setOnClickListener(businessClickListener);
        holder.businessType.setOnClickListener(businessClickListener);
        holder.distance.setOnClickListener(businessClickListener);
    }

    private void startNavigation(BusinessInfo business) {
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + business.getLat() + "," + business.getLon()));
        context.startActivity(directionsIntent);
    }

    private void startPhoneCall(BusinessInfo business) {
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
        phoneCallIntent.setData(Uri.parse("tel:" + business.getPhone()));
        PermissionUtils.makePhoneCall(context, phoneCallIntent, MainActivity.CALL_REQUEST_CODE);
    }

    private void openWebsite(BusinessInfo business) {
        String website = business.getWebsite().toLowerCase();
        Intent browserIntent;
        if (website.contains("http")) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        } else {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + website));
        }
        context.startActivity(browserIntent);
    }







    public int getItemCount() {
        return businesses.size();
    }

    public void changeList(List<BusinessInfo> newList) {
        this.businesses = newList;
        notifyDataSetChanged();
    }
}