package com.kinashe.kinasheandroid.Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.kinashe.kinasheandroid.R;

public class TopBarHelper {
    public static void setTopText(String text, Context context) {
        Activity thisActivity = (Activity) context;
        TextView topText = thisActivity.findViewById(R.id.toptext);
        topText.setText(text);
    }
}
