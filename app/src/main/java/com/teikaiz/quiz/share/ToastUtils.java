package com.teikaiz.quiz.share;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
