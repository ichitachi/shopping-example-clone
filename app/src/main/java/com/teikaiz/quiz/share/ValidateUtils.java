package com.teikaiz.quiz.share;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.teikaiz.quiz.model.Products;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ValidateUtils {

    public static boolean validate(String name, String phone, String pwd, Context context, boolean isRegister) {
        return (!isRegister || isEmpty(name, "Please write your name", context))
                && (isEmpty(phone, "Please write your phone", context)
                && isEmpty(pwd, "Please write your password", context));
    }


    private static boolean isEmpty(Object value, String msg, Context context) {
        if((value instanceof String && TextUtils.isEmpty((String) value))
        || Objects.isNull(value)) {
            ToastUtils.showToast(msg, context);
            return false;
        }
        return true;
    }

    public static boolean validate(Products product, Context context) {
        return isEmpty(product.getTitle(), "Please write product title... ", context)
                && isEmpty(product.getDescription(), "Please write product description", context)
                && isEmpty(product.getPrice(), "Please write product price", context);
    }
}
