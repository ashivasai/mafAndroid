package com.zambient.dtos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zambient on 5/5/2016.
 */
public class AndroidProductsListDto implements Serializable{

    private ArrayList<ProductList> productList;

    private FeatureDetailsDto featureDetails;

    public ArrayList<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductList> productList) {
        this.productList = productList;
    }

    public FeatureDetailsDto getFeatureDetails() {
        return featureDetails;
    }

    public void setFeatureDetails(FeatureDetailsDto featureDetails) {
        this.featureDetails = featureDetails;
    }
}
