package com.bookshopweb.utils;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class DateHelper {
    // Phương thức để định dạng thời gian từ Timestamp thành chuỗi theo định dạng "HH:mm dd/MM/yyyy"
    public static String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(timestamp);
    }
}
