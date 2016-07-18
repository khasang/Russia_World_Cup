package com.khasang.russiaworldcup2018.rss;

/**
 * Created by aleksandrlihovidov on 18.07.16.
 */
public class RSSItem {
    private String title;
    private String link;
    private String description;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.imageUrl = mImageUrl;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "title='" + title + "\'" +
                ",\nlink='" + link + "\'" +
                ",\ndescription='" + description + "\'" +
                ",\nimageUrl='" + imageUrl + "\'" +
                "}\n";
    }
}
