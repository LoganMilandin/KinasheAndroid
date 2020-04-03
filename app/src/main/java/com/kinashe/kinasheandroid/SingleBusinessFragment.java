package com.kinashe.kinasheandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Firebase.Coupon;
import com.kinashe.kinasheandroid.Utils.CustomFragment;
import com.kinashe.kinasheandroid.Utils.ImageGalleryPagerAdapter;
import com.kinashe.kinasheandroid.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleBusinessFragment extends CustomFragment {

    private static final String TAG = "SingleBusinessFragment";
    private int navbarIndex;

    public MainActivity context;

    private View thisView;
    private TextView companyName;
    private TextView description;
    private TextView hours;
    private TextView dealHeader;
    private String hoursFormat;
    private ViewPager imagePager;
    private ImageGalleryPagerAdapter imageAdapter;

    private static String[] daysEnglish = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static String[] daysAmharic = {"ሰኞ", "ማክሰኞ", "እሮብ", "ሐሙስ", "አርብ", "ቅዳሜ", "እሁድ"};

    private BusinessInfo myInfo;
    private List<Coupon> myCoupons;
    private boolean isTranslated;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        myInfo = (BusinessInfo) getArguments().getSerializable("businessInfo");
        myCoupons = removeInactiveOrExpired(myInfo.getCoupons());
        thisView = inflater.inflate(R.layout.fragment_single_business, container, false);
        companyName = thisView.findViewById(R.id.company_name);
        description = thisView.findViewById(R.id.description);
        hours = thisView.findViewById(R.id.hours);
        dealHeader = thisView.findViewById(R.id.deal_header);
        //not really translated to start, but we re-use the translate method
        //to set up initially so if it thinks we're translated already it'll
        //initialize to english, which is what we want
        this.isTranslated = true;
        populateWithData(inflater);
        setupImagePager(inflater);
        setButtonClickListeners();
        return thisView;
    }

    private void populateWithData(LayoutInflater inflater) {
        companyName.setText(myInfo.getCompanyName());
        LinearLayout scrollableContainer = thisView.findViewById(R.id.scrollable_part);
        if (myCoupons != null) {
            for (int i = 0; i < myCoupons.size(); i++) {
                View couponMarkup = inflater.inflate(R.layout.layout_coupon, scrollableContainer, false);
                couponMarkup.setId(i);
                setCouponText(couponMarkup, myCoupons.get(i));
                scrollableContainer.addView(couponMarkup);
            }
        }
        List<List<String>> hours = myInfo.getHours();
        hoursFormat = "";
        for (int i = 0; i < hours.size(); i++) {
            String open = hours.get(i).get(0);
            String close = hours.get(i).get(1);
            String openFormatted = Integer.parseInt(open.substring(0, open.indexOf(':'))) % 12
                    + ":" + open.substring(open.indexOf(':') + 1);
            String closeFormatted = Integer.parseInt(close.substring(0, close.indexOf(':'))) % 12
                    + ":" + close.substring(close.indexOf(':') + 1);
            hoursFormat += "[day]: " + openFormatted + " AM - " + closeFormatted + " PM\n";
        }
        hoursFormat = hoursFormat.substring(0, hoursFormat.length() - 1);
        this.translateText();
    }

    private void translateText() {
        String[] daysToUse;
        if (this.isTranslated) {
            //set english text
            description.setText(myInfo.getDescription());
            if (myCoupons == null || myCoupons.isEmpty()) {
                dealHeader.setText("No Deals, Sorry!");
            } else if (myCoupons.size() == 1) {
                dealHeader.setText("Deal");
            } else {
                dealHeader.setText("Deals");
            }
            daysToUse = daysEnglish;
        } else {
            description.setText(myInfo.getDescriptionTrans());
            if (myCoupons == null || myCoupons.isEmpty()) {
                dealHeader.setText("ምንም ቅናሾች የሉም!");
            } else if (myCoupons.size() == 1) {
                dealHeader.setText("ቅናሾ");
            } else {
                dealHeader.setText("ቅናሾች");
            }
            daysToUse = daysAmharic;
        }
        String hoursText = hoursFormat;
        for (int i = 0; i < daysToUse.length; i++) {
            hoursText = hoursText.replaceFirst("\\[day\\]", daysToUse[i]);
        }
        hours.setText(hoursText);
        LinearLayout scrollableContainer = thisView.findViewById(R.id.scrollable_part);
        if (myCoupons != null) {
            for (int i = 0; i < myCoupons.size(); i++) {
                View couponMarkup = scrollableContainer.findViewById(i);
                setCouponText(couponMarkup, myCoupons.get(i));
            }
        }
        this.isTranslated = !this.isTranslated;
    }

    private void setCouponText(View couponMarkup, Coupon data) {
        TextView title = couponMarkup.findViewById(R.id.title);
        TextView deal = couponMarkup.findViewById(R.id.deal);
        TextView availability = couponMarkup.findViewById(R.id.availability);
        TextView expiration = couponMarkup.findViewById(R.id.expiration);
        if (this.isTranslated) {
            //set english text
            title.setText("Coupon: " + data.getTitle());
            deal.setText("Deal: " + data.getDeal());
            availability.setText("Availability: " + data.getAvailability());
            expiration.setText("Expires: " + data.getExpiration());
        } else {
            title.setText("ኩፖን: " + data.getTitleTrans());
            deal.setText("ቅናሽ: " + data.getDealTrans());
            availability.setText("የሚገኝ ሲሆን: " + data.getAvailabilityTrans());
            expiration.setText("ጊዜው ያልፍበታል: " + data.getExpiration());
        }
    }

    private List<Coupon> removeInactiveOrExpired(List<Coupon> coupons) {
        if (coupons == null) {
            return null;
        }
        List<Coupon> activeCoupons = new ArrayList<>();
        long currentTime = new Date().getTime();
        for (Coupon coupon : coupons) {
            if (coupon.isActive() && (coupon.getExpTimestamp() - currentTime > 0)) {
                activeCoupons.add(coupon);
            }
        }
        return activeCoupons;
    }

    private void setButtonClickListeners() {
        View backButton = thisView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                context.navigationManager.handleBackClicked(SingleBusinessFragment.this);
            }
        });
        View translateButton = thisView.findViewById(R.id.translate_button);
        translateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View button, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "clicked grid");
                    ((ImageView)button).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "released");
                    ((ImageView)button).clearColorFilter();
                    if (event.getX() > 0 && event.getY() < button.getBottom()) {
                        translateText();
                    }
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ((ImageView)button).clearColorFilter();
                    return false;

                }
                return false;
            }
        });
        View navLogo = thisView.findViewById(R.id.directions);
        View phoneLogo = thisView.findViewById(R.id.phone);
        View searchLogo = thisView.findViewById(R.id.website);
        navLogo.setOnTouchListener(new View.OnTouchListener() {
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
                            Uri.parse("google.navigation:q=" + myInfo.getLat() + "," + myInfo.getLon()));
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
        phoneLogo.setOnTouchListener(new View.OnTouchListener() {
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
                    phoneCallIntent.setData(Uri.parse("tel:" + myInfo.getPhone()));
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
        if (myInfo.getWebsite() == null || myInfo.getWebsite().equals("")) {
            searchLogo.setVisibility(View.INVISIBLE);
        } else {
            searchLogo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View web, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.d(TAG, "clicked grid");
                        ((ImageView) web).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.d(TAG, "released");
                        ((ImageView) web).clearColorFilter();
                        String website = myInfo.getWebsite().toLowerCase();
                        Intent browserIntent;
                        if (website.contains("http")) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                        } else {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + website));
                        }
                        context.startActivity(browserIntent);
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        ((ImageView) web).clearColorFilter();
                        return false;
                    }
                    return false;
                }
            });
        }
    }

    private void setupImagePager(LayoutInflater inflater) {
        TextView photoCount = thisView.findViewById(R.id.photo_count);
        List<String> photos = myInfo.getPhotos();
        if (photos != null) {
            photoCount.setText(String.valueOf(photos.size()));
            imagePager = thisView.findViewById(R.id.photo_gallery);
            imageAdapter = new ImageGalleryPagerAdapter(context, inflater, myInfo.getPhotos());
            imagePager.setAdapter(imageAdapter);
            return;
        }
        photoCount.setText("0");

    }
}
