package com.example.pop_the_balloon;

import android.content.Context;
import android.util.TypedValue;

public class PixelHelper {

    public static int pixelsToDp(int px, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics());
    }
}
