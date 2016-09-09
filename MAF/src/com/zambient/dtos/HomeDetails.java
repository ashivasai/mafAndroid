package com.zambient.dtos;

import java.io.Serializable;

/**
 * Created by zambient on 2/20/2016.
 */
public class HomeDetails implements Serializable{

    private String bgImage;

    private String exBgImage;

    private String title;

    private String headerText;

    private String email;

    private String footerText;

    private String description;

    private Integer id;

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getExBgImage() {
        return exBgImage;
    }

    public void setExBgImage(String exBgImage) {
        this.exBgImage = exBgImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
