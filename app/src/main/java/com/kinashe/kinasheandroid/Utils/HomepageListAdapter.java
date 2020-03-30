package com.kinashe.kinasheandroid.Utils;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomepageListAdapter extends RecyclerView.Adapter<HomepageListAdapter.BusinessViewHolder> {

    private static final String TAG = "HomepageListAdapter";

    private final Location myLocation;
    private MainActivity context;

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

    public HomepageListAdapter(List<BusinessInfo> businesses, MainActivity context, Location location) {
        this.context = context;
        this.myLocation = location;
        Collections.sort(businesses, new Comparator<BusinessInfo>() {
            @Override
            public int compare(BusinessInfo first, BusinessInfo second) {
                if (first.getMonthlyPayment() > second.getMonthlyPayment()) {
                    return -1;
                } else if (second.getMonthlyPayment() > first.getMonthlyPayment()) {
                    return 1;
                } else {
                    Location firstLocation = new Location("");
                    firstLocation.setLatitude(Double.parseDouble(first.getLat()));
                    firstLocation.setLongitude(Double.parseDouble(first.getLon()));
                    Location secondLocation = new Location("");
                    secondLocation.setLatitude(Double.parseDouble(second.getLat()));
                    secondLocation.setLongitude(Double.parseDouble(second.getLon()));
                    Log.d(TAG, String.valueOf(myLocation.distanceTo(secondLocation)));
                    return (int) (myLocation.distanceTo(firstLocation) - myLocation.distanceTo(secondLocation));
                }
            }
        });
        this.businesses = businesses;
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
        if (myLocation != null) {
            Location targetLocation = new Location("");
            targetLocation.setLatitude(Double.parseDouble(business.getLat()));
            targetLocation.setLongitude(Double.parseDouble(business.getLon()));
            //calculation and rounding done all at once
            double distanceInKm = (int) (myLocation.distanceTo(targetLocation) / 10.0) / 100.0;
            holder.distance.setText(distanceInKm + " km");
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
        holder.businessPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View photo) {
                //open new activity
                Log.d(TAG, "clicked business");
                context.navigationManager.selectSingleBusinessFromHomepage(business);
            }
        });
        holder.companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View company) {
                //open new activity
                Log.d(TAG, "clicked business");
                context.navigationManager.selectSingleBusinessFromHomepage(business);
            }
        });
        holder.businessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View type) {
                //open new activity
                Log.d(TAG, "clicked business");
                context.navigationManager.selectSingleBusinessFromHomepage(business);
            }
        });
        holder.distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View distance) {
                //open new activity
                Log.d(TAG, "clicked business");
                context.navigationManager.selectSingleBusinessFromHomepage(business);
            }
        });
    }
    public int getItemCount() {
        return businesses.size();
    }
}