package com.zambient.dtos;

/**
 * Created by zambient on 5/7/2016.
 */
public class StoreLocatorBean {

    private String storeId;

    private String businessId;

    private String title;

    private String description;

    private String address;

    private String gpsLocation;

    private String image;
    
    private String addressLine1;
    
    private String addressLine2;
    
    private FeatureDetailsDto featureDetails;
    
    

    public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FeatureDetailsDto getFeatureDetails() {
        return featureDetails;
    }

    public void setFeatureDetails(FeatureDetailsDto featureDetails) {
        this.featureDetails = featureDetails;
    }
}
