package com.zambient.dtos;

import java.util.ArrayList;

/**
 * Created by zambient on 5/7/2016.
 */
public class AndroidPromotionsList {

    private ArrayList<PromotionList> promotionList;

    private FeatureDetailsDto featureDetails;

    public ArrayList<PromotionList> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(ArrayList<PromotionList> promotionList) {
        this.promotionList = promotionList;
    }

    public FeatureDetailsDto getFeatureDetails() {
        return featureDetails;
    }

    public void setFeatureDetails(FeatureDetailsDto featureDetails) {
        this.featureDetails = featureDetails;
    }
}
