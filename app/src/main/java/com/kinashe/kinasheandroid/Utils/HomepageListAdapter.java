package com.kinashe.kinasheandroid.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.R;

import java.util.List;

public class HomepageListAdapter extends RecyclerView.Adapter<HomepageListAdapter.BusinessViewHolder> {

    private List<String> businesses;

    public static class BusinessViewHolder extends RecyclerView.ViewHolder {
        public TextView companyName;
        public TextView businessType;

        public BusinessViewHolder(@NonNull View businessView) {
            super(businessView);
            this.companyName = businessView.findViewById(R.id.companyName);
            this.businessType = businessView.findViewById(R.id.businessType);
        }

    }

    public HomepageListAdapter(List<String> businesses) {
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
        holder.companyName.setText(businesses.get(position));
        holder.businessType.setText(businesses.get(position % 2));
    }

    public int getItemCount() {
        return businesses.size();
    }
}