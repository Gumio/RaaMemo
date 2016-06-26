package com.gumio_inf.android.raamemo.model;

import android.graphics.Bitmap;

/**
 * Created by gumio_inf on 16/06/21.
 */
//テスト用にviewに入れるデータのsetter/getter
public class RaamenData {
    private Bitmap imageData;
    private String textData;
    private String textData2;
    private String textData3;

    public void setImagaData(Bitmap image) {
        imageData = image;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setTextData(String text) {
        textData = text;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData2(String text) {
        textData2 = text;
    }

    public String getTextData2() {
        return textData2;
    }

    public void setTextData3(String text) {
        textData3 = text;
    }

    public String getTextData3() {
        return textData3;
    }
}
