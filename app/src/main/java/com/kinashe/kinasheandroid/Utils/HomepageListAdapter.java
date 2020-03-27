package com.kinashe.kinasheandroid.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomepageListAdapter extends RecyclerView.Adapter<HomepageListAdapter.BusinessViewHolder> {

    private List<BusinessInfo> businesses;

    public static class BusinessViewHolder extends RecyclerView.ViewHolder {
        public ImageView businessPhoto;
        public TextView companyName;
        public TextView businessType;
        public TextView distance;

        public BusinessViewHolder(@NonNull View businessView) {
            super(businessView);
            this.businessPhoto = businessView.findViewById(R.id.businessPhoto);
            this.companyName = businessView.findViewById(R.id.companyName);
            this.businessType = businessView.findViewById(R.id.businessType);
            this.distance = businessView.findViewById(R.id.distance);
        }

    }

    public HomepageListAdapter(List<BusinessInfo> businesses) {
        this.businesses = businesses;
    }

    @Override
    public HomepageListAdapter.BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //now, we inflate a custom layout file with the markup for an individual business
        View businessView = inflater.inflate(R.layout.business_layout, parent, false);
        BusinessViewHolder view = new BusinessViewHolder(businessView);
        return view;
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        BusinessInfo business = businesses.get(position);
        List<String> photos = business.getPhotos();
        //not sure if failed photo submissions will show up as null or
        //empty on the read request so this is just to be safe
        if (photos != null && !photos.isEmpty()) {
            Picasso.get().load(photos.get(0)).into(holder.businessPhoto);
        }
        holder.companyName.setText(business.getCompanyName());
        holder.businessType.setText(business.getBusinessType());
        holder.distance.setText("15793.0 km");
    }

    public int getItemCount() {
        return businesses.size();
    }
}