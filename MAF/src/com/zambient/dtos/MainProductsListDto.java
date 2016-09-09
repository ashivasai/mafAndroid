package com.zambient.dtos;

import java.io.Serializable;

/**
 * Created by zambient on 5/5/2016.
 */
public class MainProductsListDto implements Serializable {

    private AndroidProductsListDto androidProductsList;

    public AndroidProductsListDto getAndroidProductsList() {
        return androidProductsList;
    }

    public void setAndroidProductsList(AndroidProductsListDto androidProductsList) {
        this.androidProductsList = androidProductsList;
    }


}
