package com.khasang.russiaworldcup2018.rss;

/**
 * Created by aleksandrlihovidov on 18.07.16.
 */
public class RSSItem {
    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mImageUrl;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "mTitle='" + mTitle + "\'" +
                ",\nmLink='" + mLink + "\'" +
                ",\nmDescription='" + mDescription + "\'" +
                ",\nmImageUrl='" + mImageUrl + "\'" +
                "}\n";
    }
}
