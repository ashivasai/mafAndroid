package com.zambient.dtos;

import java.io.Serializable;

/**
 * Created by zambient on 5/7/2016.
 */
public class MainPromotionsListDto implements Serializable{

    private AndroidPromotionsList androidPromotionsList;

    public AndroidPromotionsList getAndroidPromotionsList() {
        return androidPromotionsList;
    }

    public void setAndroidPromotionsList(AndroidPromotionsList androidPromotionsList) {
        this.androidPromotionsList = androidPromotionsList;
    }
}
