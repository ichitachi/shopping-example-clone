package com.teikaiz.quiz.share;

import android.text.TextUtils;
import android.widget.EditText;

import java.math.BigDecimal;

public class EditTextUtils {
    public static String getText(EditText editText) {
        return editText.getText().toString();
    }

    public static Double getDouble(EditText editText) {
        String value = getText(editText);
        return TextUtils.isEmpty(value) ? null : Double.parseDouble(value);
    }
}
