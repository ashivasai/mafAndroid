package com.zambient.dtos;

import java.util.ArrayList;

/**
 * Created by zambient on 5/10/2016.
 */
public class AskExpertBean {

    private String askExpertId;

    private String businessId;

    private String title;

    private String question;

    private String bgImage;

    private String experts;

    private String expertId;

    private ArrayList<ExpertsList> expertsList;

    private FeatureDetailsDto featureDetails;

    public String getAskExpertId() {
        return askExpertId;
    }

    public void setAskExpertId(String askExpertId) {
        this.askExpertId = askExpertId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getExperts() {
        return experts;
    }

    public void setExperts(String experts) {
        this.experts = experts;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public ArrayList<ExpertsList> getExpertsList() {
        return expertsList;
    }

    public void setExpertsList(ArrayList<ExpertsList> expertsList) {
        this.expertsList = expertsList;
    }

    public FeatureDetailsDto getFeatureDetails() {
        return featureDetails;
    }

    public void setFeatureDetails(FeatureDetailsDto featureDetails) {
        this.featureDetails = featureDetails;
    }
}
