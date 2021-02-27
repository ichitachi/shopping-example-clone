package com.teikaiz.quiz.share;

import android.content.Context;

public class DimensUtils {

    public static int getResourceDimensToInt(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }

    public static float getResourceDimensToFloat(Context context, int id) {
        return  context.getResources().getDimension(id);
    }
}
