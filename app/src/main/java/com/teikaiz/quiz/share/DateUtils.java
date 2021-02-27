package com.teikaiz.quiz.share;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DATE_STRING_FORMAT = "MMM dd, yyyy";
    public static final String TIME_STRING_FORMAT = "HH:mm:ss a";

    public static String dateToString(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}
