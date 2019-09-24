package com.kbeanie.multipicker;

import com.google.common.truth.Truth;
import com.kbeanie.multipicker.utils.BitmapUtils;

import org.junit.Test;

/**
 * Created by kbibek on 3/18/16.
 */
public class BitmapUtilsTestCase {

    @Test
    public void testGetScaledDimensionsNoScaling() {
        int imageWidth = 100;
        int imageHeight = 200;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Truth.assertThat(dimensions).isNotNull();
        Truth.assertThat(dimensions[0]).isEqualTo(imageWidth);
        Truth.assertThat(dimensions[1]).isEqualTo(imageHeight);
    }

    @Test
    public void testGetScaledDimensionsLargeHeight() {
        int imageWidth = 100;
        int imageHeight = 500;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Truth.assertThat(dimensions).isNotNull();
        Truth.assertThat(dimensions[0]).isEqualTo((int) (((float) 100 / 500) * 300));
        Truth.assertThat(dimensions[1]).isEqualTo(maxHeight);
    }

    @Test
    public void testGetScaledDimensionsLargeWidth() {
        int imageWidth = 700;
        int imageHeight = 200;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Truth.assertThat(dimensions).isNotNull();
        Truth.assertThat(dimensions[0]).isEqualTo(maxWidth);
        Truth.assertThat(dimensions[1]).isEqualTo((int) (((float) 200 / 700) * 300));
    }


    @Test
    public void testGetScaledDimensionsLargeBoth() {
        int imageWidth = 800;
        int imageHeight = 800;
        int maxWidth = 300;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Truth.assertThat(dimensions).isNotNull();
        Truth.assertThat(dimensions[0]).isEqualTo(300);
        Truth.assertThat(dimensions[1]).isEqualTo(300);
    }

    @Test
    public void testGetScaledDimensionsLargeBoth2() {
        int imageWidth = 800;
        int imageHeight = 1200;
        int maxWidth = 200;
        int maxHeight = 300;

        int[] dimensions = BitmapUtils.getScaledDimensions(imageWidth, imageHeight, maxWidth, maxHeight);
        Truth.assertThat(dimensions).isNotNull();
        Truth.assertThat(dimensions[0]).isEqualTo((int) ((800 / (float) 1200) * 300));
        Truth.assertThat(dimensions[1]).isEqualTo(300);
    }
}
