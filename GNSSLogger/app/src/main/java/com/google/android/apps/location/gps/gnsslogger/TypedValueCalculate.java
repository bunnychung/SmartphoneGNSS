
package com.google.android.apps.location.gps.gnsslogger;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by uiseok on 2016-09-17.
 */
public class TypedValueCalculate {
    public static int dp2pixel(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int pixel2dp(int pixel, Context context) {
        return (int) (pixel / context.getResources().getDisplayMetrics().density / DisplayMetrics.DENSITY_DEFAULT);
    }
}
