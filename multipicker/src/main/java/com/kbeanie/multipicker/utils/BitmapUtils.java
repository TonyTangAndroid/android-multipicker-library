package com.kbeanie.multipicker.utils;

/**
 * Created by kbibek on 3/18/16.
 */
public class BitmapUtils {
    public static int[] getScaledDimensions(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
        int[] values = new int[2];
        if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
            values[0] = originalWidth;
            values[1] = originalHeight;
        } else {
            if (originalHeight >= maxHeight && originalWidth <= maxWidth) {
                float ratio = (float) originalWidth / originalHeight;
                int outWidth = (int) (ratio * (float) maxHeight);
                values[0] = outWidth;
                values[1] = maxHeight;
            } else if (originalHeight <= maxHeight) {
                float ratio = (float) originalWidth / originalHeight;
                int outHeight = (int) ((float) maxWidth / ratio);
                values[0] = maxWidth;
                values[1] = outHeight;
            } else {
                float ratio = (float) originalWidth / originalHeight;
                int outHeight = (int) ((float) maxWidth / ratio);
                values[0] = maxWidth;
                values[1] = outHeight;
            }
        }
        return values;
    }
}
