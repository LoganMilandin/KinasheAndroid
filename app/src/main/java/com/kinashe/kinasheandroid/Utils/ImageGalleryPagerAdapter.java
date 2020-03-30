package com.kinashe.kinasheandroid.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.kinashe.kinasheandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageGalleryPagerAdapter extends PagerAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<String> imageUrls;


    public ImageGalleryPagerAdapter(Context context, LayoutInflater inflater, List<String> imageUrls) {
        this.context = context;
        this.inflater = inflater;
        this.imageUrls = imageUrls;

    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View imageContainer = inflater.inflate(R.layout.layout_image_for_gallery, container, false);
        ImageView image = imageContainer.findViewById(R.id.image_view);
        Picasso.get().load(imageUrls.get(position)).into(image);
        container.addView(imageContainer);
        return imageContainer;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView((LinearLayout) obj);
    }
}
