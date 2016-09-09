package com.zambient.dtos;

import java.util.ArrayList;

/**
 * Created by zambient on 3/17/2016.
 */
public class ReviewsBeanListDto {

    public Integer reviewId;

    public Integer businessId;

    public String title;

    public String image;

    public String comment;

    public String rating;

    public String url;

    public ArrayList<ReviewsList> reviewsList;

    public FeatureDetailsDto featureDetails;

}
